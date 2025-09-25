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
const val ID = "id"
const val FIELDS = "fields"
const val LOCATION = "location"
const val MISSING_TILE_ID = "Specified tile with ID not found"

/**
 * parser for farm config file
 */
class FarmParser(private val simulationData: SimulationData) {
    private lateinit var farmIDs: List<Int>
    private lateinit var farmNames: List<String>
    private lateinit var machineIDs: List<Int>
    private lateinit var machineNames: List<String>
    private val sowingPlanIDs: MutableList<Int> = mutableListOf()
    private val sowingPlanTicks: MutableMap<Int, MutableList<SowingPlan>> = mutableMapOf()

    private val tilesInJsonIDs: MutableSet<Int> = mutableSetOf()

    /**
     * main parse function
     */
    fun parse(json: String) {
        try {
            val file = File(json)
            val jsonString = file.readText()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

            // Parse main components
            val farms = jsonObject["farms"]?.jsonArray ?: throw ValidationException()

            parseFarms(farms)

            // CHECK DONE IN parseFarmstead, parseFields...
            /*for (farm in simulationData.getFarms()) {
                validateFarmTiles(farm)
            }*/

            // Validates uniqueness of IDs and Names
            validateUniqueAttributes()

            // CHECK DONE IN validateMachineLocation
            /*crossValidateFarmMachine()*/

            // Checks if all the tiles belonging to farms on the map have been accounted for
            validateTileFarms()

            // Do the farmstead neighbor test
            for (farm in simulationData.getFarms()) {
                if (!checkFarmsteadNeighbors(farm)) {
                    throw ValidationException("Farmstead has a field/plantation invalid neighbor")
                }
            }

            for (tick in this.sowingPlanTicks.keys) {
                val plans = sowingPlanTicks[tick] ?: continue
                simulationData.addSowingPlansToMapping(plans, tick)
            }
            // logging should be done here as file valid
            Logger.logParsing(file.name)
        } catch (exception: ValidationException) {
            throw ValidationException(exception, json)
        }
    }

    /**
     * parses farms
     */
    private fun parseFarms(farms: JsonArray) {
        /*val mapOfFarms = mutableMapOf<Int, Farm>()*/
        for (farmJson in farms) {
            val farmObject = farmJson.jsonObject
            val farm = parseFarm(farmObject)
            // gets ID and then sets one by one to mapOfFarms
            /*mapOfFarms[farm.getId()] = farm*/
            simulationData.addFarmToMapping(farm)
        }

        // There has to be at least one farm in the simulation
        if (simulationData.getFarms().isEmpty()) {
            throw ValidationException("Zero farms in the simulation")
        }

        farmIDs = simulationData.getFarms().map { it.getId() }
        farmNames = simulationData.getFarms().map { it.getName() }
    }

    /**
     * parses single farm object
     */
    private fun parseFarm(farm: JsonObject): Farm {
        // Uniqueness checked using validateUniqueAttributes function
        val id = farm[ID]?.jsonPrimitive?.int ?: throw ValidationException("Farm ID not specified")
        if (id < 0) throw ValidationException("ID should be non-negative")

        val name = farm["name"]?.jsonPrimitive?.content ?: throw ValidationException("Farm name not specified")
        // Name can be empty string
        // if (name.isEmpty()) throw ValidationException("Empty farm name")

        // Each function must check whether the tiles specified in the json are actually what they are supposed to be
        // A tile with id n in the fields list has to be a field
        // Each farmstead, field, plantation has to belong to exactly one farm.
        // Checks using the farmID on the tile + adds to a set to find the tiles unaccounted for the map
        val farmsteadTiles = parseFarmsteadTiles(farm, id)
        val fieldTiles = parseFieldTiles(farm, id)
        val plantationTiles = parsePlantationTiles(farm, id)

        // A farm needs to have at least one PLANTATION OR one FIELD
        if (fieldTiles.isEmpty() && plantationTiles.isEmpty()) {
            throw ValidationException("No FIELD tiles AND PLANTATION tiles")
        }

        val allMachines = parseMachines(farm, id)

        val sowingPlanMap = parseSowingPlans(farm, id)
        val parsedFarm = Farm(
            id,
            name,
            farmsteadTiles,
            fieldTiles,
            plantationTiles,
            allMachines,
            sowingPlanMap,
            mutableMapOf()
        )

        // Validates that only PLANTS the farm can sow can be used in sowingPlans
        validateSowingPlanPlants(parsedFarm)
        // Initializes the ownerFarm ID in each machine
        updateMachineFarmID(parsedFarm)

        return parsedFarm
    }

    /**
     * Helper function to parse farmsteads
     * Creates and returns a list of farmstead tiles belonging to a farm
     * ADD CHECK
     */
    private fun parseFarmsteadTiles(farm: JsonObject, id: Int): MutableList<Tile> {
        val farmsteads = farm["farmsteads"]?.jsonArray
            ?: throw ValidationException("Farmsteads not specified")

        val farmsteadTiles = farmsteads.map { farmstead ->
            val tileID = farmstead.jsonPrimitive.int
            val tile = simulationData.getTileById(tileID)
                ?: throw ValidationException(MISSING_TILE_ID + tileID.toString())

            // Make sure the tile specified is actually a farmstead tile
            if (tile.category != TileType.FARMSTEAD) {
                throw ValidationException("Tile specified as a Farmstead isn't really a Farmstead.")
            }

            // Checks that the tile hasn't been added before
            if (tilesInJsonIDs.contains(tileID) || tile.farmID != id) {
                throw ValidationException("FARMSTEAD belongs to more than one farm.")
            }
            // Add this farmstead tile to the set so that we can catch if another farm claims it
            tilesInJsonIDs.add(tileID)

            tile
        }.toMutableList()

        return farmsteadTiles
    }

    /**
     * Helper function to parse field tiles
     * Creates and returns a list of field tiles belonging to a farm
     * ADD CHECK
     */
    private fun parseFieldTiles(farm: JsonObject, id: Int): MutableList<Tile> {
        val fields = farm[FIELDS]?.jsonArray
            ?: throw ValidationException("Fields not specified")

        val fieldTiles = fields.map { field ->
            val tileID = field.jsonPrimitive.int
            val tile = simulationData.getTileById(tileID)
                ?: throw ValidationException("Specified tile with ID $tileID not found")

            // Make sure the tile specified is actually a field tile
            if (tile.category != TileType.FIELD) {
                throw ValidationException("Tile specified as a Field isn't really a Field.")
            }

            // Checks that the tile hasn't been added before
            if (tilesInJsonIDs.contains(tileID) || tile.farmID != id) {
                throw ValidationException("FIELD is belongs to more than one farm.")
            }
            // Add this field tile to the set so that we can catch if another farm claims it
            tilesInJsonIDs.add(tileID)

            tile
        }.toMutableList()

        return fieldTiles
    }

    /**
     * helper function to parse plantation tiles
     * Creates and returns a list of plantation tiles belonging to a farm
     * ADD CHECK
     */
    private fun parsePlantationTiles(farm: JsonObject, id: Int): MutableList<Tile> {
        val plantations = farm["plantations"]?.jsonArray ?: throw ValidationException("Plantations not specified")

        val plantationTiles = plantations.map { plantation ->
            val tileID = plantation.jsonPrimitive.int
            val tile = simulationData.getTileById(tileID)
                ?: throw ValidationException("Specified tile with ID $tileID not found")

            // Make sure that the tile specified is actually a plantation tile
            if (tile.category != TileType.PLANTATION) {
                throw ValidationException("Tile specified as a Plantation isn't really a Plantation.")
            }

            // Checks that the tile hasn't been added before
            if (tilesInJsonIDs.contains(tileID) || tile.farmID != id) {
                throw ValidationException("PLANTATION is belongs to more than one farm.")
            }
            // Add this plantation tile to the set so that we can catch if another farm claims it
            tilesInJsonIDs.add(tileID)

            tile
        }.toMutableList()

        return plantationTiles
    }

    /**
     * helper function that parses machine array
     */
    private fun parseMachines(farm: JsonObject, farmID: Int): List<Machine> {
        val machines = farm["machines"]?.jsonArray ?: throw ValidationException("Machines not specified")
        val mapOfMachines = mutableMapOf<Int, Machine>()
        for (machine in machines) {
            val parsedMachine = parseMachine(machine.jsonObject, farmID)
            mapOfMachines[parsedMachine.id] = parsedMachine
            simulationData.addMachineToMapping(parsedMachine)
        }
        machineIDs = simulationData.getMachines().map { it.id }
        machineNames = simulationData.getMachines().map { it.name }
        return mapOfMachines.values.toList().sortedBy { it.id }
    }

    /**
     * parses single machine
     */
    private fun parseMachine(m: JsonObject, farmID: Int): Machine {
        // Uniqueness checked using validateUniqueAttributes function
        val id = m[ID]?.jsonPrimitive?.int ?: throw ValidationException("Machine ID missing")
        if (id < 0) throw ValidationException(" ID must be non-negative")

        val name = m["name"]?.jsonPrimitive?.content ?: throw ValidationException("Machine name missing")
        // Name can be empty string - Assumption like for farms
        // if (name.isEmpty()) throw ValidationException("Empty machine name")

        // These functions check that a machine has at least one action, one plant and that the duration is 1..14
        val actions = parseMachineActions(m)
        val plants = parseMachinePlants(m)
        val duration = parseDuration(m)

        val tile = parseMachineLocation(m)
        // Checks that this tile is a farmstead, contains a shed and belongs to the farm owing this machine
        validateMachineLocation(tile.id, farmID)

        // CHECK IF THIS CHECK IS EXPECTED
        /*validateActionsAndPlants(actions, plants)*/

        return Machine(id, name, duration, tile, actions, plants, homeShed = tile)
    }

    /**
     * parses machine duration
     */
    private fun parseDuration(machine: JsonObject): Int {
        val duration = machine["duration"]?.jsonPrimitive?.int ?: throw ValidationException("Duration missing")
        if (duration !in 1..FOURTEEN) throw ValidationException("Invalid duration $duration")
        return duration
    }

    /**
     * helper function to parse machine actions
     */
    private fun parseMachineActions(machine: JsonObject): List<ActionType> {
        val actions = machine["actions"]?.jsonArray ?: throw ValidationException("Actions not specified")
        if (actions.isEmpty()) throw ValidationException("Empty actions list")
        val actionsArray: List<String> = actions.map { action ->
            action.jsonPrimitive.content.uppercase()
        }
        for (action in actionsArray) {
            if (action !in ActionType.entries.map { it.name }) {
                throw ValidationException("Action $action is invalid")
            }
        }
        val listOfActions = actionsArray.map { ActionType.valueOf(it) }
        return listOfActions
    }

    /**
     * helper function to parse plants specified in machine
     */
    private fun parseMachinePlants(machine: JsonObject): List<PlantType> {
        val plants = machine["plants"]?.jsonArray ?: throw ValidationException("Plants not specified")
        if (plants.isEmpty()) throw ValidationException()
        val plantsArray = plants.map { it.jsonPrimitive.content.uppercase() }
        for (plant in plantsArray) {
            if (plant !in PlantType.entries.map { it.name }) {
                throw ValidationException("Plant $plant is invalid")
            }
        }
        val listOfPlants: List<PlantType> = plantsArray.map { plant ->
            PlantType.valueOf(plant)
        }
        return listOfPlants
    }

    /**
     * helper function to parse the machine location
     */
    private fun parseMachineLocation(machine: JsonObject): Tile {
        val location = machine[LOCATION]?.jsonPrimitive?.int ?: throw ValidationException("Location missing")
        if (location < 0) throw ValidationException()
        val tile: Tile = simulationData.getTileById(location)
            ?: throw ValidationException("Tile $location not found")
        return tile
    }

    /**
     * parses sowing plan array of the farm
     */
    private fun parseSowingPlans(farm: JsonObject, farmID: Int): MutableMap<Int, MutableList<SowingPlan>> {
        val plans = farm["sowingPlans"]?.jsonArray ?: throw ValidationException()
        val mapOfSowingPlans: MutableMap<Int, MutableList<SowingPlan>> = mutableMapOf()
        for (plan in plans) {
            val sowingPlan = parseSowingPlan(plan as JsonObject, farmID)
            // Group sowing plans by tick
            val tick = sowingPlan.getTick()
            val listOfPlans = mapOfSowingPlans.getOrPut(tick) { mutableListOf() }
            listOfPlans.add(sowingPlan)
            mapOfSowingPlans[tick] = listOfPlans
        }
//         simulationData.setSowingPlanMapping(mapOfSowingPlans)
//         sowingPlanIDs = simulationData.getSowingPlans().map { it.getId() }
        return mapOfSowingPlans
    }

    /**
     * parses a single sowing plan
     */
    private fun parseSowingPlan(plan: JsonObject, farmID: Int): SowingPlan {
        // Uniqueness checked using validateUniqueAttributes function
        val id = plan[ID]?.jsonPrimitive?.int ?: throw ValidationException("Sowing plan ID missing")
        if (id < 0) throw ValidationException("ID must be non-negative")

        val tick = plan["tick"]?.jsonPrimitive?.int ?: throw ValidationException("Tick missing")
        if (tick < 0) throw ValidationException("Tick must be non-negative")

        val plantType = parsePlant(plan)

        val affectedTiles = if (plan[FIELDS] != null) {
            parseSowingPlanTiles(plan, true, farmID)
        } else {
            parseSowingPlanTiles(plan, false, farmID)
        }
        // Adding sowingPlans to the list of all sowingPlans (from all farms)
        val sowingPlan = SowingPlan(id, plantType, tick, affectedTiles)
        this.sowingPlanIDs.add(sowingPlan.getId())
        val list = this.sowingPlanTicks.getOrPut(sowingPlan.getTick()) { mutableListOf() }
        list.add(sowingPlan)
        this.sowingPlanTicks[sowingPlan.getTick()] = list
        return sowingPlan
    }

    /**
     * helper function to parse the plant type of the sowing plan
     */
    private fun parsePlant(plan: JsonObject): PlantType {
        val plant = plan["plant"]?.jsonPrimitive?.content
            ?: throw ValidationException("Plant missing for sowing plan")
        if (plant.uppercase() !in setOf(PlantType.entries.toString())) {
            throw ValidationException("Invalid plant $plant")
        }
        val plantType = PlantType.valueOf(plant.uppercase())
        return plantType
    }

    /**
     * helper function that parses the affected tiles of the sowing plan
     */
    private fun parseSowingPlanTiles(plan: JsonObject, fields: Boolean, farmID: Int): List<Tile> {
        if (fields) return parseSowingPlanFields(plan, farmID)

        // need to check that both location AND radius are present
        // Negation -> OR
        if (plan[LOCATION] == null || plan["radius"] == null) {
            throw ValidationException("Both location and radius must be specified")
        }

        val location = plan[LOCATION]?.jsonPrimitive?.int ?: throw ValidationException("Missing location")
        val radius = plan["radius"]?.jsonPrimitive?.int ?: throw ValidationException("Missing radius")

        if (location < 0 || radius < 0) throw ValidationException()

        val centerTile = simulationData.getTileById(location) ?: throw ValidationException()
        val radiusTiles = simulationData.map.getTilesByRadius(centerTile, radius)
        val tiles = radiusTiles.plus(centerTile)
        validateSowingPlanLocation(tiles, false, farmID)
        return tiles
    }

    /**
     * helper function that parses field tiles of the sowing plan
     */
    private fun parseSowingPlanFields(plan: JsonObject, farmID: Int): List<Tile> {
        val tileArray = plan[FIELDS]?.jsonArray ?: throw ValidationException()
        val fieldTileIDs = tileArray.map { it.jsonPrimitive.int }
        val fieldTiles = fieldTileIDs.map {
            val tile = simulationData.getTileById(it)
                ?: throw ValidationException("Specified tile $it not found")
            if (tile.category != TileType.FIELD) {
                throw ValidationException("Tile specified in sowing plan with fields is not type FIELD")
            }
            tile
        }
        validateSowingPlanLocation(fieldTiles, true, farmID)
        return fieldTiles
    }

    /**
     * Checks if the machine does SOW/WEED that it can work on some FIELD plant and if the machine does CUT/MOW
     * that it can work on some PLANTATION plant
     * COMMENTED OUT -> ASK FORUM/TUTOR IF THIS CHECK IS REQUIRED
     */
    /*private fun validateActionsAndPlants(actions: List<ActionType>, plants: List<PlantType>) {
        if (actions.contains(ActionType.SOWING) || actions.contains(ActionType.WEEDING)) {
            val fieldPlants = plants.filter { PlantType.isFieldPlant(it.toString()) }
            if (fieldPlants.isNotEmpty()) {
                throw ValidationException("Mismatch of machine action and plant type")
            }
        }
        if (actions.contains(ActionType.CUTTING) || actions.contains(ActionType.MOWING)) {
            val plantationPlants = plants.filter { PlantType.isPlantationPlant(it.toString()) }
            if (plantationPlants.isNotEmpty()) {
                throw ValidationException("Mismatch of machine action and plant type")
            }
        }
    }*/

    /**
     * helper function to validate machine location
     */
    private fun validateMachineLocation(tileID: Int, farmID: Int) {
        val tile = simulationData.getTileById(tileID)
        if (tile?.category != TileType.FARMSTEAD || tile.shed != true || tile.farmID != farmID) {
            throw ValidationException("Invalid machine location on tile ${tile?.id ?: "null id"}")
        }
    }

    /**
     * validates unique IDs and names
     */
    private fun validateUniqueAttributes() {
        // Check unique IDs and names
        var duplicates = farmIDs.filter { element -> farmIDs.count { it == element } > 1 }.distinct()
        if (duplicates.isNotEmpty()) { throw ValidationException("Duplicate farm id") }
        duplicates = sowingPlanIDs.filter { element -> sowingPlanIDs.count { it == element } > 1 }.distinct()
        if (duplicates.isNotEmpty()) { throw ValidationException("Duplicate sowing plan id") }
        duplicates = machineIDs.filter { element -> machineIDs.count { it == element } > 1 }.distinct()
        if (duplicates.isNotEmpty()) { throw ValidationException("Duplicate machine id") }
        var duplicateNames = farmNames.filter { element -> farmNames.count { it == element } > 1 }.distinct()
        if (duplicateNames.isNotEmpty()) { throw ValidationException("Duplicate farm names") }
        duplicateNames = machineNames.filter { element -> machineNames.count { it == element } > 1 }.distinct()
        if (duplicateNames.isNotEmpty()) { throw ValidationException("Duplicate machine names") }
    }

    /**
     * helper function to cross-validate machine and farm IDs
     * CHECK HANDLED BY validateMachineLocation
     */
    /*private fun crossValidateFarmMachine() {
        for (farm in simulationData.getFarms()) {
            // Get all farmstead tiles with sheds on them
            val shedTiles: List<Tile> = farm.getFarmstead().filter { it.shed == true }
            for (machine in farm.getMachines()) {
                // Check if the machine's home shed is in the farm's shed tiles
                if (!shedTiles.contains(machine.homeShed)) {
                    throw ValidationException("Machine shed must belong to the same farm")
                }
            }
        }
    }*/

    /**
     * validateSowingPlanLocation checks if the field tiles on the list given in the parameter all belong to one farm.
     * There is also a boolean, fields, that if true, just checks if the farmIDs of the tiles are all the same (since if
     * fields == true, meaning that the list only contains field tiles), and if false, checks if there is at least one
     *     field tile in the list and also checks the farmIDs.
     *
     *     Assumption: At least one tile has to belong to the owner farm
     */
    private fun validateSowingPlanLocation(tiles: List<Tile>, fields: Boolean, farmID: Int) {
        if (fields) {
            for (tile in tiles) {
                if (tile.category != TileType.FIELD) { throw ValidationException("All tiles must be FIELD") }
            }
            // Check if at least one tile belongs to the owner farm
            if (tiles.none {
                    it.farmID == farmID
                }
            ) { throw ValidationException("Sowing Plan does not contain a tile that belongs to the farm") }
        } else {
            val fieldTiles = tiles.filter { it.category == TileType.FIELD }
            if (fieldTiles.isEmpty()) throw ValidationException("At least one tile must be a field")
            // Check if at least one tile belongs to the owner farm
            if (fieldTiles.none {
                    it.farmID == farmID
                }
            ) { throw ValidationException("Sowing Plan does not contain a tile that belongs to the farm") }
        }

        // Change added -> Assume that the tiles don't have to belong to one farm
        /*val farmIDs = tiles.filter { it.farmID != null }.map { it.farmID }
        if (farmIDs.distinct().size != 1) {
            throw ValidationException("All fields must belong to the same farm")
        }*/
    }

    /**
     * cross validates farm and its tile IDs
     * CHECK DONE IN parseFarmstead, parseFields...
     */
    /*private fun validateFarmTiles(f: Farm) {
        // Get all tiles and store into one list
        val tilesOfFarm: List<Tile> = f.getFarmstead() + f.getFields() + f.getPlantation()
        for (tile in tilesOfFarm) {
            val farmIDofTile: Int? = tile.farmID
            if (farmIDofTile != f.getId()) {
                throw ValidationException("Mismatch of the actual and tile-specified farm IDs")
            }
        }
    }*/

    /**
     * validates that each tile has a valid farmID specified
     */
    private fun validateTileFarms() {
        // Version 2
        val tiles = simulationData.getTiles()
        for (tile in tiles) {
            val farmID = tile.farmID
            if (farmID != null) {
                if (tile.id !in tilesInJsonIDs) {
                    throw ValidationException("Tile unaccounted for")
                }
            }
        }

        // Doesn't check if the ids of each tile appeared in the lists of tiles in each farm
        /*val tiles = simulationData.getTiles()
        for (tile in tiles) {
            val farmID = tile.farmID
            if (farmID != null) {
                if (!farmIDs.contains(farmID)) {
                    throw ValidationException("Invalid Farm ID $farmID specified by tile ${tile.id}")
                }
            }
        }*/
    }

    /**
     * helper function to validate sowing plan plant types matching the possible plants of the farm
     */
    private fun validateSowingPlanPlants(farm: Farm) {
        // Version 2, set difference
        // Collect all plants that the farm can grow from all fields
        val possiblePlantsOnFarm: Set<PlantType> = farm.getFields().flatMap { it.possiblePlants.orEmpty() }.toSet()

        // Collect all plants used in sowing plans across all ticks
        val plantsInSowingPlans: Set<PlantType> = farm.getSowingPlans().values.flatten().map { it.getPlant() }.toSet()

        val invalidPlants = plantsInSowingPlans - possiblePlantsOnFarm
        if (invalidPlants.isNotEmpty()) {
            throw ValidationException("Sowing Plan contains plant that is not sowable by farm")
        }

        /*val plantTypes = farm.getSowingPlans().values.flatten().map { it.getPlant() }
        val fields = farm.getFields()
        for (field in fields) {
            val possiblePlants = field.possiblePlants
                ?: throw ValidationException("Field must have possible plants")
            if (!possiblePlants.containsAll(plantTypes)) {
                throw ValidationException("Sowing plans must match possible plants of the farm")
            }
        }*/
    }

    /**
     * sets the farm ID in the machines
     */
    private fun updateMachineFarmID(farm: Farm) {
        for (m in farm.getMachines()) {
            m.farmID = farm.getId()
        }
    }

    /**
     * Checks if a farmstead has a neighbor which belongs to another farm
     */
    private fun checkFarmsteadNeighbors(farm: Farm): Boolean {
        for (farmstead in farm.getFarmstead()) {
            val neighborTiles = simulationData.map.getTilesByRadius(farmstead, 1)
            if (neighborTiles.any {
                    (it.category == TileType.FIELD || it.category == TileType.PLANTATION) &&
                        it.farmID != farm.getId()
                }
            ) {
                return false
            }
        }
        return true
    }
}
