package de.unisaarland.cs.se.selab.parser

// import com.github.erosb.jsonsKema.IJsonObject
// import com.github.erosb.jsonsKema.IJsonValue
// import com.github.erosb.jsonsKema.JsonParser
// import com.github.erosb.jsonsKema.Schema
// import com.github.erosb.jsonsKema.SchemaLoader
// import com.github.erosb.jsonsKema.Validator
import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import de.unisaarland.cs.se.selab.incidents.BrokenMachine
import de.unisaarland.cs.se.selab.incidents.CityExpansion
import de.unisaarland.cs.se.selab.incidents.CloudCreation
import de.unisaarland.cs.se.selab.incidents.Drought
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.log.Logger
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
// import kotlin.collections.component1
// import kotlin.collections.component2

const val INCIDENT_STRING = "incidents"
const val CLOUDS_STRING = "clouds"
const val ID_STRING = "id"
const val TICK_STRING = "tick"
const val TYPE_STRING = "type"
const val LOCATION_STRING = "location"
const val RADIUS_STRING = "radius"
const val DURATION_STRING = "duration"
const val EFFECT_STRING = "effect"
const val MACHINEID_STRING = "machineId"
const val AMOUNT_STRING = "amount"

/**
 * Handles Parsing the Scenario File
 * */
class ScenarioParser(private val simData: SimulationData) {
    private val incidentIDs: MutableList<Int> = mutableListOf()
    private val cloudIDs: MutableList<Int> = mutableListOf()
    private val cloudToTile: MutableMap<Cloud, Tile> = mutableMapOf()

    // Map from tick to cloud creation incidents
    private val cloudCreationIncidents: MutableMap<Int, MutableList<Incident>> = mutableMapOf()

    // For double-checking against sowing plan tiles
    private val cityExpansionTiles: MutableList<Tile> = mutableListOf()
//    private val schema: Schema by lazy { loadFarmSchema() }
//
//    private fun loadFarmSchema(): Schema {
//        // Load all farm-related schemas
//        val cloudSchema = JsonParser(File("src/main/resources/schema/cloud.schema").readText()).parse()
//        val incidentSchema = JsonParser(File("src/main/resources/schema/incident.schema").readText()).parse()
//
//        val clouddefsNode = cloudSchema.requireObject()["\$defs"]?.requireObject()
//            ?: throw ValidationException()
//        val incidentdefsNode = incidentSchema.requireObject()["\$defs"]?.requireObject()
//            ?: throw ValidationException()
//
//        val allDefs = mutableMapOf<String, IJsonValue>()
//        fun merge(defs: IJsonObject<*, *>) {
//            // defs.properties: Map<IJsonString,IJsonValue>
//            defs.properties.forEach { (jsonKey, jsonVal) ->
//                val key: String = jsonKey.toString() // extract the string
//                allDefs[key] = jsonVal
//            }
//        }
//
//        merge(clouddefsNode)
//        merge(incidentdefsNode)
//
//        val defsJson = allDefs.entries.joinToString(
//            prefix = "{",
//            postfix = "}"
//        ) { (k, v) -> "\"$k\":$v" }
//
//        val scenarioSchemaJson = JsonParser(
//            """
//            {
//              "${'$'}schema": "https://json-schema.org/draft/2020-12/schema",
//              "type": "object",
//              "additionalProperties": false,
//              "properties": {
//                "clouds": {
//                  "type": "array",
//                  "items": {
//                    "${'$'}ref": "#/${'$'}defs/cloud"
//                  },
//                  "uniqueItems": true
//                },
//                "incidents": {
//                  "type": "array",
//                  "items": {
//                    "${'$'}ref": "#/${'$'}defs/incident"
//                  },
//                  "uniqueItems": true
//                }
//              },
//              "required": ["clouds", "incidents"],
//              "${'$'}defs": $defsJson
//            }
//            """
//        ).parse()
//
//        return SchemaLoader(scenarioSchemaJson).load()
//    }

    /**
     * Parses the scenario files and runs the checks validity
     */
    fun parse(jsonPath: String) {
        try {
            // Read file
            val jsonFile = File(jsonPath).readText()

//            val validator = Validator.forSchema(schema)
//            val validation = validator.validate(JsonParser(jsonFile).parse())
//            if (validation != null) {
//                throw ValidationException()
//            }

            // Parse file
            val jsonData = Json.parseToJsonElement(jsonFile).jsonObject
            // Get incidents array
            val incidents = jsonData[INCIDENT_STRING]?.jsonArray ?: throw ValidationException()
            // Get clouds array
            val clouds = jsonData[CLOUDS_STRING]?.jsonArray ?: throw ValidationException()

            // Parse incidents
            parseIncidents(incidents)
            // Parse clouds
            parseClouds(clouds)

            // Validate City Expansion Incidents
            checkValid(validateCityExpansion(simData.getIncidents().toList()))

            // Validate Cloud Creation Incidents for villages
            checkValid(validateCloudCreationWithVillages(simData.getIncidents().toList()))

            // Validate Cloud Creation and Overlapping clouds !!!!
            checkValid(checkOverlappingCloudCreation())

            // Cross-check sowing plans
            // Since there has to be at least one field tile till the end of the simulation, the function will compare
            // with all possible city expansion incidents
            val sowingPlanMap = simData.getSowingPlanMapping()
            for (sowingPlans in sowingPlanMap.values) {
                checkValid(checkSowingPlanFields(sowingPlans))
            }
            Logger.logParsing(File(jsonPath).name)
        } catch (exception: ValidationException) {
            throw ValidationException(exception, jsonPath)
        }
    }

    private fun parseIncidents(incidents: JsonArray) {
        val incidentMap: MutableMap<Int, Incident> = mutableMapOf()
        for (incident in incidents) {
            val i = parseIncident(incident.jsonObject)
            incidentMap[i.id] = i
            simData.addIncidentToMapping(i)
        }
    }

    private fun parseIncident(obj: JsonObject): Incident {
        // Get ID
        val id: Int = obj[ID_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Check if ID is not negative
        checkValid(id >= 0)
        // Checks if ID is unique
        checkValid(validateUniqueIncidentIDs(id))

        // Get tick
        val tick: Int = obj[TICK_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Check if tick is not negative
        checkValid(tick >= 0)

        // Get Incident Type
        val type: String = obj[TYPE_STRING]?.jsonPrimitive?.content ?: throw ValidationException()
        // Convert to enum - Throw error if no matching enum value
        if (type !in IncidentType.entries.map { it.name }) throw ValidationException()
        val incidentType = IncidentType.valueOf(type)

        // Will be set depending on IncidentType
        lateinit var incident: Incident

        when (incidentType) {
            IncidentType.ANIMAL_ATTACK -> {
                // Call ParseAnimalAttack Helper
                incident = parseAnimalAttack(obj, id, tick, incidentType)
            }

            IncidentType.BEE_HAPPY -> {
                // Call ParseBeeHappy Helper
                incident = parseBeeHappy(obj, id, tick, incidentType)
            }

            IncidentType.BROKEN_MACHINE -> {
                // Call ParseBrokenMachine Helper
                incident = parseBrokenMachine(obj, id, tick, incidentType)
            }

            IncidentType.CITY_EXPANSION -> {
                // Call ParseCityExpansion Helper
                incident = parseCityExpansion(obj, id, tick, incidentType)
            }

            IncidentType.CLOUD_CREATION -> {
                // call parseCloudCreation Helper
                incident = parseCloudCreation(obj, id, tick, incidentType)
            }

            IncidentType.DROUGHT -> {
                // call parseDrought Helper
                incident = parseDrought(obj, id, tick, incidentType)
            }
        }

        // Add incidentID to incidentIDs list
        incidentIDs.add(id)

        return incident
    }

    private fun parseAnimalAttack(obj: JsonObject, id: Int, tick: Int, incidentType: IncidentType): Incident {
        // Get location
        val tileID: Int = obj[LOCATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for non-negative id
        checkValid(tileID >= 0)

        // Check if tile exists and get tile
        val tile: Tile = simData.getTileById(tileID) ?: throw ValidationException()

        // Get Radius
        val radius: Int = obj[RADIUS_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for non-negative radius
        checkValid(radius >= 0)

        // Validate affected tiles - CHECK FOR FOREST
        checkValid(validateAnimalAttack(tile, radius))

        return AnimalAttack(id, tick, incidentType, tile, radius)
    }

    private fun parseBeeHappy(obj: JsonObject, id: Int, tick: Int, incidentType: IncidentType): BeeHappy {
        // Get location
        val tileID: Int = obj[LOCATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for non-negative id
        checkValid(tileID >= 0)

        // Check if tile exists and get tile
        val tile: Tile = simData.getTileById(tileID) ?: throw ValidationException()

        // Get Radius
        val radius: Int = obj[RADIUS_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for non-negative radius
        checkValid(radius >= 0)

        // Validate affected tiles - CHECK FOR MEADOW
        checkValid(validateBeeHappy(tile, radius))

        // Get pollination effect
        val effect: Int = obj[EFFECT_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Check if positive
        checkValid(effect > 0)

        return BeeHappy(id, tick, incidentType, effect, tile, radius)
    }

    private fun parseBrokenMachine(obj: JsonObject, id: Int, tick: Int, incidentType: IncidentType): BrokenMachine {
        // Gets duration
        val duration: Int = obj[DURATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for positive or -1 duration
        checkValid(duration > 0 || duration == -1)

        // Get machine ID
        val machineid: Int = obj[MACHINEID_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks if not negative
        checkValid(machineid >= 0)
        // Check if machine exists and get machine
        val machine = simData.getMachineById(machineid) ?: throw ValidationException()

        lateinit var durationObject: Duration
        if (duration == -1) {
            durationObject = Duration(-1, -1)
        } else {
            durationObject = Duration(tick + 1, tick + duration)
        }

        return BrokenMachine(id, tick, incidentType, machine, durationObject)
    }

    private fun parseCityExpansion(obj: JsonObject, id: Int, tick: Int, incidentType: IncidentType): CityExpansion {
        // Get location
        val tileID: Int = obj[LOCATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for if non-negative
        checkValid(tileID >= 0)
        // Check if tile exists and get tile
        val tile: Tile = simData.getTileById(tileID) ?: throw ValidationException()
        // Add to cityExpansionTiles to validate sowing Plans
        cityExpansionTiles.add(tile)

        // VALIDATE EXISTENCE OF ADJOINING VILLAGE AT POINT OF INCIDENCE AFTER COLLECTING ALL INCIDENTS

        // !! Check toList function
        return CityExpansion(id, tick, incidentType, tile, simData.getFarms())
    }

    private fun parseCloudCreation(obj: JsonObject, id: Int, tick: Int, incidentType: IncidentType): CloudCreation {
        // Gets duration
        val duration: Int = obj[DURATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for positive or -1 duration
        checkValid(duration > 0 || duration == -1)

        // Get location
        val tileID: Int = obj[LOCATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for if non-negative
        checkValid(tileID >= 0)
        // Check if tile exists and get tile
        val tile: Tile = simData.getTileById(tileID) ?: throw ValidationException()

        // Get Radius
        val radius: Int = obj[RADIUS_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for non-negative radius
        checkValid(radius >= 0)

        // Get amount of water on cloud
        val amount: Int = obj[AMOUNT_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks if positive
        checkValid(amount > 0)

        // VALIDATE WITH CITY EXPANSION TILES AND SOWING PLAN TILES AFTER COLLECTING ALL INCIDENTS

        val incident = CloudCreation(id, tick, incidentType, tile, radius, duration, amount)

        // Add to cloudCreationIncidents
        cloudCreationIncidents.getOrPut(tick) { mutableListOf() }.add(incident)
        return incident
    }

    private fun parseDrought(obj: JsonObject, id: Int, tick: Int, incidentType: IncidentType): Drought {
        // Get location
        val tileID: Int = obj[LOCATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for if non-negative
        checkValid(tileID >= 0)
        // Check if tile exists and get tile
        val tile: Tile = simData.getTileById(tileID) ?: throw ValidationException()

        // Get Radius
        val radius: Int = obj[RADIUS_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for non-negative radius
        checkValid(radius >= 0)

        // Validate affected tiles - CHECK FOR FIELD OR PLANTATION
        checkValid(validateDrought(tile, radius))

        // Initialize CloudHandler at a later point when simData is fully initialized !!!!
        return Drought(id, tick, incidentType, tile, radius)
    }

    private fun validateAnimalAttack(locationTile: Tile, radius: Int): Boolean {
        val affectedTiles: List<Tile> = simData.map.getTilesByRadius(locationTile, radius)
        return affectedTiles.any { it.category == TileType.FOREST }
    }

    private fun validateBeeHappy(tile: Tile, radius: Int): Boolean {
        val affectedTiles: List<Tile> = simData.map.getTilesByRadius(tile, radius)
        return affectedTiles.any { it.category == TileType.MEADOW }
    }

    private fun validateDrought(tile: Tile, radius: Int): Boolean {
        val affectedTiles: List<Tile> = simData.map.getTilesByRadius(tile, radius)
        return affectedTiles.any { it.category == TileType.FIELD || it.category == TileType.PLANTATION }
    }

    private fun validateCityExpansion(incidents: List<Incident>): Boolean {
        val cityExpansionIncidents: List<CityExpansion> = incidents.filterIsInstance<CityExpansion>().sortedWith(
            compareBy(
                CityExpansion::tick,
                CityExpansion::id
            )
        )

        // These should be all the new villages created up to the tick and then id
        // Since the cityExpansion incidents are ordered in this way, we will add villages to the list in this order
        // while iterating through the incidents
        val newVillages: MutableList<Tile> = mutableListOf()

        for (cityExpansionIncident in cityExpansionIncidents) {
            val tiles = simData.map.getTilesByRadius(cityExpansionIncident.tile, 1)
            // Find any overlap between newVillages and adjoining tiles
            val newVillagesTiles = tiles.intersect(newVillages).toList()

            if (tiles.none { it.category == TileType.VILLAGE } &&
                newVillagesTiles.none { it.category == TileType.VILLAGE }
            ) {
                return false
            }

            // Check that there is no adjoining forest - AhhWildBoarInCity?
            if (tiles.any { it.category == TileType.FOREST }) {
                return false
            }

            // Check if the incident Tile is a FIELDS or ROAD
            if (cityExpansionIncident.tile.category != TileType.FIELD &&
                cityExpansionIncident.tile.category != TileType.ROAD
            ) {
                return false
            }
            newVillages.add(cityExpansionIncident.tile)
        }
        return true
    }

    private fun validateCloudCreationWithVillages(incidents: List<Incident>): Boolean {
        val cloudCreationIncidents: List<CloudCreation> = incidents.filterIsInstance<CloudCreation>()
            .sortedBy { it.tick }
        val cityExpansionIncidents: List<CityExpansion> = incidents.filterIsInstance<CityExpansion>()
            .sortedBy { it.tick }

        for (cloudCreation in cloudCreationIncidents) {
            // Get affected tiles by cloud creation incident
            val tiles: List<Tile> = simData.map.getTilesByRadius(cloudCreation.tile, cloudCreation.radius)
            // Get the list of villages that will be created up to the point of the cloud creation incident
            val villagesUpToIncident: List<Tile> = newVillagesUpToIncident(
                cityExpansionIncidents,
                cloudCreation.tick,
                cloudCreation.id
            )

            // Get set difference of these tiles -> The affected tiles that don't have new villages on them
            val tilesToCheck: List<Tile> = tiles - villagesUpToIncident

            // Check if at least one of them is not a village -> Else return false
            // Which means if all tiles are villages then this must be rejected
            if (tilesToCheck.all { it.category == TileType.VILLAGE }) {
                return false
            }
        }
        return true
    }

    private fun newVillagesUpToIncident(
        cityExpansions: List<CityExpansion>,
        incidentTick: Int,
        incidentID: Int
    ): List<Tile> {
        val villageTiles: MutableList<Tile> = mutableListOf()
        for (cityExpansion in cityExpansions) {
            if (cityExpansion.tick < incidentTick ||
                (cityExpansion.tick == incidentTick && cityExpansion.id < incidentID)
            ) {
                villageTiles.add(cityExpansion.tile)
            }
        }
        return villageTiles
    }

    private fun checkOverlappingCloudCreation(): Boolean {
        // Use the tick to cloudCreationIncident map
        // Check for each tick available if there are overlaps in the affected tiles on the map

        for (cloudList in cloudCreationIncidents.values) {
            // Cloud Creation Incidents sorted by id per tick
            val cloudCreations: List<CloudCreation> = cloudList.filterIsInstance<CloudCreation>()
                .sortedWith(compareBy(CloudCreation::id))
            // ID set keeps track of tiles that will be affected. This will be updated in ascending order of id per tick
            val tileIDSet: MutableSet<Int> = mutableSetOf()
            // Iterate through incidents in tick adding the affected tiles to the set
            for (cloudCreation in cloudCreations) {
                val affectedTiles = simData.map.getTilesByRadius(cloudCreation.tile, cloudCreation.radius)
                if (!checkNoOverlap(tileIDSet, affectedTiles)) {
                    return false
                }
            }
        }
        return true
    }

    // Helper function for checkOverlappingCloudCreation
    private fun checkNoOverlap(
        tileIDSet: MutableSet<Int>,
        affectedTiles: List<Tile>
    ): Boolean {
        for (tile in affectedTiles) {
            // For each tile in affectedTile, if the Tile is already in set, that means that another cloud creation
            // event happened before during the same tick that would have affected the same tile.
            // This holds because the incidents are ordered by ID per tick.
            // Then there will be an overlap if one of these overlapping tiles is not a village
            // since clouds are not created on villages
            if (tile.id in tileIDSet && tile.category != TileType.VILLAGE) {
                return false
            } else {
                tileIDSet.add(tile.id)
            }
        }
        return true
    }

    private fun parseClouds(clouds: JsonArray) {
        val cloudMap: MutableMap<Int, Cloud> = mutableMapOf()
        for (cloud in clouds) {
            val c = parseCloud(cloud.jsonObject)
            cloudMap[c.id] = c
            simData.addCloudToMapping(c)
        }
    }

    private fun parseCloud(c: JsonObject): Cloud {
        // Get ID
        val id: Int = c[ID_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks if ID is positive
        checkValid(id >= 0)
        // Checks if the ID is unique
        checkValid(validateUniqueCloudIDs(id))

        // Get location
        val location: Int = c[LOCATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks if ID is positive
        checkValid(location >= 0)
        // Checks if this tile exists on the map and returns its coordinate
        val tile: Tile = simData.getTileById(location) ?: throw ValidationException()
        // Checks if the tile already has a cloud on it
        checkValid(validateLocation(tile))

        // Gets duration
        val duration: Int = c[DURATION_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for positive or -1 duration
        checkValid(duration > 0 || duration == -1)

        // Gets amount
        val amount: Int = c[AMOUNT_STRING]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for positive amount
        checkValid(amount > 0)

        val createdCloud = Cloud(id, tile.location, duration, amount)

        // Add cloud to CloudToTile Map
        cloudToTile[createdCloud] = tile
        // Add cloudID to cloudIDs list
        cloudIDs.add(id)

        return createdCloud
    }

    private fun validateLocation(t: Tile): Boolean {
        return !cloudToTile.containsValue(t)
    }

    private fun validateUniqueCloudIDs(id: Int): Boolean {
        return !cloudIDs.contains(id)
    }

    private fun validateUniqueIncidentIDs(id: Int): Boolean {
        return !incidentIDs.contains(id)
    }

    /**
     * Will cross-check sowing plans from farm parser
     */
    private fun checkSowingPlanFields(plans: List<SowingPlan>): Boolean {
        val newVillageIDs = cityExpansionTiles.map { it.id }.toSet()
        for (plan in plans) {
            val potentialTilesInPlan = plan.getSowingTiles().filter { it.id !in newVillageIDs }
            if (potentialTilesInPlan.none {
                    it.category == TileType.FIELD && it.possiblePlants?.contains(plan.getPlant()) == true
                }
            ) {
                return false
            }
        }
        return true
    }

    private fun checkValid(condition: Boolean) {
        if (!condition) throw ValidationException()
    }
}
