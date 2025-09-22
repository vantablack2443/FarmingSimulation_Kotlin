package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

const val FOURTEEN = 14

class FarmParser(private val simulationData: SimulationData) {
    private lateinit var farmIDs: List<Int>
    private lateinit var farmNames: List<String>
    private lateinit var machineIDs: List<Int>
    private lateinit var machineNames: List<String>
    private lateinit var sowingPlanIDs: List<Int>
    private lateinit var sowingPlanTicks: MutableMap<Int, List<SowingPlan>>
    // TODO add a function that initializes machines' farmID when parsing is done

    fun parse(json: String) {
        try {
            val file = File(json)
            val jsonString = file.readText()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

            // Parse main components
            val farms = jsonObject["farms"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()

            parseFarms(farms)

            for (farm in simulationData.getFarms()) {
                if (!validateFarmTiles(farm)) {
                    throw ValidationException()
                }
            }

            if (!validateUniqueAttributes()) {
                throw ValidationException()
            }
            if (!crossValidateFarmMachine()) {
                throw ValidationException()
            }
            // logging should be done here as file valid
            Logger.logParsing(true, file.name)
        } catch (exception: ValidationException) {
            throw ValidationException(exception, json)
        }
    }

    private fun parseFarms(farms: JsonArray) {
        val mapOfFarms = mutableMapOf<Int, Farm>()
        for (farmJson in farms) {
            val farmObject = farmJson.jsonObject
            val farm = parseFarm(farmObject)
            // gets ID and then sets one by one to mapofFarms
            mapOfFarms[farm.getId()] = farm
        }
        simulationData.setFarms(mapOfFarms)
        farmIDs = simulationData.getFarms().map { it.getId() }
        farmNames = simulationData.getFarms().map { it.getName() }
    }

    private fun parseFarm(farm: JsonObject): Farm {
        val id = farm["id"]?.jsonPrimitive?.int ?: throw ValidationException("Farm ID not specified")
        if (id < 0) throw ValidationException("ID should be non-negative")

        val name = farm["name"]?.jsonPrimitive?.content ?: throw ValidationException("Farm name not specified")
        if (name.isEmpty()) throw ValidationException("Empty farm name")

        val farmsteads = farm["farmsteads"]?.jsonPrimitive?.jsonArray
            ?: throw ValidationException("Farmsteads not specified")
        val farmsteadTiles = farmsteads.map { farmstead ->
            val tileID = farmstead.jsonPrimitive.int
            simulationData.getTileById(tileID)
                ?: throw ValidationException("Specified tile with ID $tileID not found")
        }

        val fields = farm["fields"]?.jsonPrimitive?.jsonArray
            ?: throw ValidationException("Fields not specified")
        val fieldTiles = fields.map { field ->
            val tileID = field.jsonPrimitive.int
            simulationData.getTileById(tileID)
                ?: throw ValidationException("Specified tile with ID $tileID not found")
        }.toMutableList()

        val plantations = farm["plantations"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        val plantationTiles = plantations.map { plantation ->
            val tileID = plantation.jsonPrimitive.int
            simulationData.getTileById(tileID)
                ?: throw ValidationException("Specified tile with ID $tileID not found")
        }.toMutableList()

        val allMachines = parseMachines(
            farm["machines"]?.jsonPrimitive?.jsonArray
                ?: throw ValidationException("Machines not specified")
        )

        val sowingPlanMap = parseSowingPlans(
            farm["sowingPlans"]?.jsonPrimitive?.jsonArray
                ?: throw ValidationException()
        )

        return Farm(
            id,
            name,
            farmsteadTiles,
            fieldTiles,
            plantationTiles,
            allMachines,
            sowingPlanMap,
            mutableMapOf()
        ) // harvestPerPlant is done at the end
    }

    private fun parseMachines(machines: JsonArray): List<Machine> {
        val mapOfMachines = mutableMapOf<Int, Machine>()
        for (machine in machines) {
            val machine = parseMachine(machine as JsonObject)
            mapOfMachines[machine.id] = machine
        }
        simulationData.setMachines(mapOfMachines)
        machineIDs = simulationData.getMachines().map { it.id }
        machineNames = simulationData.getMachines().map { it.name }
        return mapOfMachines.values.toList().sortedBy { it.id }
    }

    private fun parseMachine(m: JsonObject): Machine {
        val id = m["id"]?.jsonPrimitive?.int ?: throw ValidationException("Machine ID missing")
        if (id < 0) throw ValidationException(" ID must be non-negative")

        val name = m["name"]?.jsonPrimitive?.content ?: throw ValidationException("Machine name missing")
        if (name.isEmpty()) throw ValidationException("Empty machine name")

        val actions = m["actions"]?.jsonPrimitive?.jsonArray
            ?: throw ValidationException("Actions not specified")
        if (actions.isEmpty()) throw ValidationException("Empty actions list")
        val actionsArray: List<String> = actions.map { action ->
            action.jsonPrimitive.content.uppercase()
        }
        for (action in actionsArray) {
            if (action !in setOf(ActionType.entries.toString())) {
                throw ValidationException("Action $action is invalid")
            }
        }
        val listOfActions = actionsArray.map { ActionType.valueOf(it) }

        val plants = m["plants"]?.jsonArray?.jsonArray ?: throw ValidationException("Plants not specified")
        if (plants.isEmpty()) throw ValidationException()
        val plantsArray = plants.map { it.jsonPrimitive.content.uppercase() }
        for (plant in plantsArray) {
            if (plant !in setOf(PlantType.entries.toString())) {
                throw ValidationException("Plant $plant is invalid")
            }
        }
        val listOfPlants: List<PlantType> = plantsArray.map { plant ->
            PlantType.valueOf(plant)
        }

        val duration = m["duration"]?.jsonPrimitive?.int ?: throw ValidationException("Duration missing")
        if (duration !in 1..FOURTEEN) throw ValidationException("Invalid duration $duration")

        val location = m["location"]?.jsonPrimitive?.int ?: throw ValidationException("Location missing")
        if (location < 0) throw ValidationException()
        val tile: Tile = simulationData.getTileById(location)
            ?: throw ValidationException("Tile $location not found")

        val isValid = validateMachineLocation(location) && validateActionsAndPlants(actions, plants)
        if (!isValid) {
            throw ValidationException("Invalid machine location $location")
        }

        return Machine(id, name, duration, tile, listOfActions, listOfPlants, homeShed = tile)
    }

    private fun parseSowingPlans(plans: JsonArray): MutableMap<Int, MutableList<SowingPlan>> {
        val mapOfSowingPlans: MutableMap<Int, MutableList<SowingPlan>> = mutableMapOf()
        for (plan in plans) {
            val sowingPlan = parseSowingPlan(plan as JsonObject)
            // Group sowing plans by tick
            val tick = sowingPlan.getTick()
            val listOfPlans = mapOfSowingPlans.getOrPut(tick) { mutableListOf() }
            listOfPlans.add(sowingPlan)
            mapOfSowingPlans[tick] = listOfPlans
        }
        // simulationData.setSowingPlanMapping(mapOfSowingPlans)
        // sowingPlanIDs = simulationData.getSowingPlans().map { it.getId() }
        return mapOfSowingPlans
    }

    private fun parseSowingPlan(plan: JsonObject): SowingPlan {
        val id = plan["id"]?.jsonPrimitive?.int ?: throw ValidationException("Sowing plan ID missing")
        if (id < 0) throw ValidationException("ID must be non-negative")

        val tick = plan["tick"]?.jsonPrimitive?.int ?: throw ValidationException("Tick missing")
        if (tick < 0) throw ValidationException("Tick must be non-negative")

        val plant = plan["plant"]?.jsonPrimitive?.content
            ?: throw ValidationException("Plant missing for sowing plan")
        if (plant.uppercase() !in setOf(PlantType.entries.toString())) {
            throw ValidationException("Invalid plant $plant")
        }
        val plantType = PlantType.valueOf(plant.uppercase())

        val affectedTiles = if (plan["fields"] != null) {
            parseSowingPlanTiles(plan, true)
        } else {
            parseSowingPlanTiles(plan, false)
        }

        return SowingPlan(id, plantType, tick, affectedTiles)
    }

    /**
     * helper function that parses the affected tiles of the sowing plan
     */
    private fun parseSowingPlanTiles(plan: JsonObject, fields: Boolean): List<Tile> {
        if (fields) {
            val tileArray = plan["fields"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
            val fieldTileIDs = tileArray.map { it.jsonPrimitive.int }
            val fieldTiles = fieldTileIDs.map {
                simulationData.getTileById(it)
                    ?: throw ValidationException("Specified tile $it not found")
            }
            validateSowingPlanLocation(fieldTiles, true)
            return fieldTiles
        }

        // need to check that both location and radius are present
        if (plan["location"] == null && plan["radius"] == null) {
            throw ValidationException("Both location and radius must be specified")
        }

        val location = plan["location"]?.jsonPrimitive?.int ?: throw ValidationException("Missing location")
        val radius = plan["radius"]?.jsonPrimitive?.int ?: throw ValidationException("Missing radius")

        if (location < 0 || radius < 0) throw ValidationException()

        val centerTile = simulationData.getTileById(location) ?: throw ValidationException()
        val radiusTiles = simulationData.map.getTilesByRadius(centerTile, radius)
        val tiles = radiusTiles.plus(centerTile)
        validateSowingPlanLocation(tiles, false)
        return tiles
    }

    private fun validateActionsAndPlants(actions: JsonArray, plants: JsonArray): Boolean {
        val actionList: List<ActionType> = actions.map { action ->
            ActionType.valueOf(action.jsonPrimitive.content.uppercase())
        }
        val plantList: List<PlantType> = plants.map { plant ->
            PlantType.valueOf(plant.jsonPrimitive.content.uppercase())
        }
        actionList.forEach { action ->
            when (action) {
                ActionType.SOW, ActionType.WEED -> {
                    if (!plantList.contains(PlantType.WHEAT) ||
                        !plantList.contains(PlantType.OAT) ||
                        !plantList.contains(PlantType.PUMPKIN) ||
                        !plantList.contains(PlantType.POTATO)
                    ) { return false }
                }
                ActionType.CUT, ActionType.MOW -> {
                    if (!plantList.contains(PlantType.APPLE) ||
                        !plantList.contains(PlantType.GRAPE) ||
                        !plantList.contains(PlantType.CHERRY) ||
                        !plantList.contains(PlantType.ALMOND)
                    ) { return false }
                }
                else -> { return true }
            }
        }
        return true
    }

    private fun validateMachineLocation(tileID: Int): Boolean {
        val tile = simulationData.getTileById(tileID)
        return tile?.category == TileType.FARMSTEAD && tile.shed == true
    }

    private fun validateUniqueAttributes(): Boolean {
        // Check unique farm attributes
        val farmIds = mutableSetOf<Int>()
        val farmNames = mutableSetOf<String>()

        for (farm in simulationData.getFarms()) {
            if (!farmIds.add(farm.getId()) || !farmNames.add(farm.getName())) {
                return false
            }
        }

        // Check unique machine attributes
        val machineIds = mutableSetOf<Int>()
        val machineNames = mutableSetOf<String>()

        for (machine in simulationData.getMachines()) {
            if (!machineIds.add(machine.id) || !machineNames.add(machine.name)) {
                return false
            }
        }

        // Check unique sowing plan attributes
        val sowingPlanIds = mutableSetOf<Int>()

        for (plan in simulationData.getSowingPlans()) {
            if (!sowingPlanIds.add(plan.getId())) { return false }
        }
        return true
    }

    private fun crossValidateFarmMachine(): Boolean {
        for (farm in simulationData.getFarms()) {
            // Get all farmstead tiles with sheds on them
            val shedTiles: List<Tile> = farm.getFarmstead().filter { it.shed == true }
            for (machine in farm.getMachines()) {
                // Check if the machine's home shed is in the farm's shed tiles
                if (!shedTiles.contains(machine.homeShed)) { return false }
            }
        }
        return true
    }

    /**
     * validateSowingPlanLocation checks if the field tiles on the list given in the parameter all belong to one farm.
     * There is also a boolean, fields, that if true, just checks if the farmIDs of the tiles are all the same (since if
     * fields == true, meaning that the list only contains field tiles), and if false, checks if there is at least one
     *     field tile in the list and also checks the farmIDs.
     */
    private fun validateSowingPlanLocation(tiles: List<Tile>, fields: Boolean) {
        if (fields) {
            for (tile in tiles) {
                if (tile.category != TileType.FIELD) { throw ValidationException("All tiles must be FIELD") }
            }
        } else {
            val fields = tiles.filter { it.category == TileType.FIELD }
            if (fields.isEmpty()) throw ValidationException("At least one tile must be a field")
        }
        val farmIDs = tiles.filter { it.farmID != null }.map { it.farmID!! }
        if (farmIDs.distinct().size != 1) {
            throw ValidationException("All fields must belong to the same farm")
        }
    }

    private fun validateFarmTiles(f: Farm): Boolean {
        // Get all tiles and store into one list
        val tilesOfFarm: List<Tile> = f.getFarmstead() + f.getFields() + f.getPlantation()
        for (tile in tilesOfFarm) {
            val farmIDofTile: Int? = tile.farmID
            if (farmIDofTile != f.getId()) { return false }
        }
        return true
    }
}
