package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
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
        val file = File(json)
        val jsonString = file.readText()
        val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

        // Parse main components
        val farms = jsonObject["farms"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()

        parseFarms(farms)

        for (farm in simulationData.farms.values) {
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
        farmIDs = simulationData.farms.values.map { it.getId() }
        farmNames = simulationData.farms.values.map { it.getName() }
    }

    private fun parseFarm(farm: JsonObject): Farm {
        val id = farm["id"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (id < 0) throw ValidationException()

        val name = farm["name"]?.jsonPrimitive?.content ?: throw ValidationException()
        if (name.isEmpty()) throw ValidationException()

        val farmsteads = farm["farmsteads"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        val farmsteadTiles = farmsteads.map { farmstead ->
            val tileID = farmstead.jsonPrimitive.int
            simulationData.tiles[tileID] ?: throw ValidationException()
        }

        val fields = farm["fields"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        val fieldTiles = fields.map { field ->
            val tileID = field.jsonPrimitive.int
            simulationData.tiles[tileID] ?: throw ValidationException()
        }

        val plantations = farm["plantations"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        val plantationTiles = plantations.map { plantation ->
            val tileID = plantation.jsonPrimitive.int
            simulationData.tiles[tileID] ?: throw ValidationException()
        }

        parseMachines(farm["machines"]?.jsonPrimitive?.jsonArray ?: throw ValidationException())

        val machines = farm["machines"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        val allMachines = machines.map { machine ->
            val machineID = machine.jsonPrimitive.int
            simulationData.machines[machineID] ?: throw ValidationException()
        }

        parseSowingPlans(farm["sowingPlans"]?.jsonPrimitive?.jsonArray ?: throw ValidationException())

        val sowingPlans = farm["sowingPlans"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        val allSowingPlans = sowingPlans.map { sowingPlan ->
            val sowingPlanID = sowingPlan.jsonPrimitive.int
            simulationData.sowingPlans[sowingPlanID] ?: throw ValidationException()
        }
        val sowingPlanMap: MutableMap<Int, List<SowingPlan>> = mutableMapOf()
        allSowingPlans.forEachIndexed { index, sowingPlanList ->
            sowingPlanMap[index] = sowingPlanList
        }

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

    private fun parseMachines(machines: JsonArray) {
        val mapOfMachines = mutableMapOf<Int, Machine>()
        for (machine in machines) {
            val machine = parseMachine(machine as JsonObject)
            mapOfMachines[machine.id] = machine
        }
        simulationData.setMachines(mapOfMachines)
        machineIDs = simulationData.machines.values.map { it.id }
        machineNames = simulationData.machines.values.map { it.name }
    }

    private fun parseMachine(m: JsonObject): Machine {
        val id = m["id"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (id < 0) throw ValidationException()

        val name = m["name"]?.jsonPrimitive?.content ?: throw ValidationException()
        if (name.isEmpty()) throw ValidationException()

        val actions = m["actions"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
        if (actions.isEmpty()) throw ValidationException()
        val listOfActions: List<ActionType> = actions.map { action ->
            ActionType.valueOf(action.jsonPrimitive.content.uppercase())
        }

        val plants = m["plants"]?.jsonArray?.jsonArray ?: throw ValidationException()
        if (plants.isEmpty()) throw ValidationException()
        val listOfPlants: List<PlantType> = plants.map { plant ->
            PlantType.valueOf(plant.jsonPrimitive.content.uppercase())
        }

        val duration = m["duration"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (duration <= 0 || duration > FOURTEEN) throw ValidationException()

        val location = m["location"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (location < 0) throw ValidationException()
        val tile: Tile = simulationData.tiles[location] ?: throw ValidationException()

        val isValid = validateMachineLocation(location) && validateActionsAndPlants(actions, plants)
        if (!isValid) {
            throw ValidationException()
        }

        return Machine(id, name, duration, tile, listOfActions, listOfPlants, homeShed = tile)
    }

    private fun parseSowingPlans(plans: JsonArray) {
        val mapOfSowingPlans: MutableMap<Int, List<SowingPlan>> = mutableMapOf()
        for (plan in plans) {
            val sowingPlan = parseSowingPlan(plan as JsonObject)
            // Group sowing plans by tick
            val tick = sowingPlan.getTick()
            if (!mapOfSowingPlans.containsKey(tick)) {
                mapOfSowingPlans[tick] = mutableListOf()
            }
            mapOfSowingPlans[tick]
        }
        simulationData.setSowingPlans(mapOfSowingPlans)
        sowingPlanIDs = simulationData.sowingPlans.values.flatten().map { it.getId() }
        sowingPlanTicks = simulationData.sowingPlans
    }

    private fun parseSowingPlan(plan: JsonObject): SowingPlan {
        val id = plan["id"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (id < 0) throw ValidationException()

        val tick = plan["tick"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (tick < 0) throw ValidationException()

        val plant = plan["plant"]?.jsonPrimitive?.content ?: throw ValidationException()
        val plantType = PlantType.valueOf(plant.uppercase())

        val tiles: List<Tile> = if (plan["location"] != null && plan["radius"] != null) {
            val location = plan["location"]?.jsonPrimitive?.int ?: throw ValidationException()
            val radius = plan["radius"]?.jsonPrimitive?.int ?: throw ValidationException()

            if (location < 0 || radius < 0) throw ValidationException()

            val centerTile = simulationData.tiles[location] ?: throw ValidationException()
            val radiusTiles = simulationData.map.getTilesByRadius(centerTile, radius)

            if (!validateSowingPlanLocation(radiusTiles, true)) {
                throw ValidationException()
            }
        } else if (plan["tiles"] != null) {
            val tileArray = plan["tiles"]?.jsonPrimitive?.jsonArray ?: throw ValidationException()
            val arrayOfTiles = tileArray.map { tileJson ->
                val tileID = tileJson.jsonPrimitive.int
                simulationData.tiles[tileID] ?: throw ValidationException()
            }
            if (!validateSowingPlanLocation(arrayOfTiles, false)) {
                throw ValidationException()
            }
        }

        return SowingPlan(id, plantType, tick, tiles)
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
        val tile = simulationData.tiles[tileID]
        return tile?.category == TileType.FARMSTEAD && tile.shed == true
    }

    private fun validateUniqueAttributes(): Boolean {
        // Check unique farm attributes
        val farmIds = mutableSetOf<Int>()
        val farmNames = mutableSetOf<String>()

        for (farm in simulationData.farms.values) {
            if (!farmIds.add(farm.getId()) || !farmNames.add(farm.getName())) {
                return false
            }
        }

        // Check unique machine attributes
        val machineIds = mutableSetOf<Int>()
        val machineNames = mutableSetOf<String>()

        for (machine in simulationData.machines.values) {
            if ((!machineIds.add(machine.id)) || !machineNames.add(machine.name)) {
                return false
            }
        }

        // Check unique sowing plan attributes
        val sowingPlanIds = mutableSetOf<Int>()

        for (plan in simulationData.sowingPlans.values) {
            if (!sowingPlanIds.add(plan.id)) { return false }
        }
        return true
    }

    private fun crossValidateFarmMachine(): Boolean {
        for (farm in simulationData.farms.values) {
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
    private fun validateSowingPlanLocation(tiles: List<Tile>, fields: Boolean): Boolean {
        val list: MutableList<Int> = mutableListOf<Int>()
        if (fields) {
            for (tile in tiles) {
                if (tile.category != TileType.FIELD) { return false }
                val farmIDofTile = tile.farmID ?: return false
                list.add(farmIDofTile)
            }
            // Check if all farmIDs in the list are the same
            return list.distinct().size == 1
        } else {
            var fieldExists = false
            for (tile in tiles) {
                if (tile.category == TileType.FIELD) {
                    fieldExists = true
                    val farmIDofTile = tile.farmID ?: return false
                    list.add(farmIDofTile)
                }
            }
            if (!fieldExists) return false
            return list.distinct().size == 1
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

/**
 * This is a custom exception
 */
class ValidationException : Exception("Validation Exception, missing or invalid fields")
