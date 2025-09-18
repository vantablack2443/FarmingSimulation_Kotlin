package de.unisaarland.cs.se.selab.parser

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
/**
 * Throws exception in case validation fails
 * */
class ValidationException : Exception("Validation Exception, missing or invalid fields")

/**
 * Handles Parsing the Scenario File
 * */
class ScenarioParser(private val simData: SimulationData) {
    private var incidentIDs: MutableList<Int> = mutableListOf()
    private var cloudIDs: MutableList<Int> = mutableListOf()
    private var cloudToTile: MutableMap<Cloud, Tile> = mutableMapOf()

    // map from tick to cloud creation incidents
    private var cloudCreationIncidents: MutableMap<Int, MutableList<Incident>> = mutableMapOf()

    // for double-checking against sowing plan tiles
    private var cityExpansionTiles: MutableList<Tile> = mutableListOf()

    fun parse(jsonPath: String) {
        // Read file
        val jsonFile = File(jsonPath).readText()
        // Parse file
        val jsonData = Json.parseToJsonElement(jsonFile).jsonObject
        // Get incidents array
        val incidents = jsonData["incidents"]?.jsonArray ?: throw ValidationException()
        // Get clouds array
        val clouds = jsonData["clouds"]?.jsonArray ?: throw ValidationException()

        // Parse incidents
        parseIncidents(incidents)
        // Parse clouds
        parseClouds(clouds)

        // Validate City Expansion Incidents
        if (!validateCityExpansion(simData.incidents.values.toList())) throw ValidationException()

        // Validate Cloud Creation Incidents for overlapping areas
        if (!validateCloudCreation(simData.incidents.values.toList())) throw ValidationException()
    }

    private fun parseIncidents(incidents: JsonArray) {
        for (incident in incidents) {
            val i = parseIncident(incident.jsonObject)
            simData.incidents[i.id] = i
        }
    }

    private fun parseIncident(obj: JsonObject): Incident {
        // Get ID
        val id: Int = obj["id"]?.jsonPrimitive?.int ?: throw ValidationException()
        // Check if ID is not negative
        if (id < 0) throw ValidationException()
        // Checks if ID is unique
        if (!validateUniqueIncidentIDs(id)) throw ValidationException()

        // Get tick
        val tick: Int = obj["tick"]?.jsonPrimitive?.int ?: throw ValidationException()
        // Check if tick is not negative
        if (tick < 0) throw ValidationException()

        // Get Incident Type
        val type: String = obj["type"]?.jsonPrimitive?.content ?: throw ValidationException()
        // Convert to enum - Throw error if no matching enum value
        val incident_type: IncidentType = try {
            IncidentType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            throw ValidationException()
        }

        // Will be set depending on IncidentType
        lateinit var incident: Incident

        when (incident_type) {
            IncidentType.ANIMAL_ATTACK -> {
                // Get location
                val tileID: Int = obj["location"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for non-negative id
                if (tileID < 0) throw ValidationException()
                // Check if tile exists and get tile
                val tile: Tile = simData.tiles[tileID] ?: throw ValidationException()

                // Get Radius
                val radius: Int = obj["radius"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for non-negative radius
                if (radius < 0) throw ValidationException()

                // Validate affected tiles - CHECK FOR FOREST
                if (!validateAnimalAttack(tile, radius)) throw ValidationException()

                incident = AnimalAttack(id, tick, incident_type, tile, radius)
            }

            IncidentType.BEE_HAPPY -> {
                // Get location
                val tileID: Int = obj["location"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for if non negative
                if (tileID < 0) throw ValidationException()
                // Check if tile exists and get tile
                val tile: Tile = simData.tiles[tileID] ?: throw ValidationException()

                // Get Radius
                val radius: Int = obj["radius"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for non-negative radius
                if (radius < 0) throw ValidationException()

                // Validate affected tiles - CHECK FOR MEADOW
                if (!validateBeeHappy(tile, radius)) throw ValidationException()

                // Get pollination effect
                val effect: Int = obj["effect"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Check if positive
                if (effect <= 0) throw ValidationException()

                incident = BeeHappy(id, tick, incident_type, effect, tile, radius)
            }

            IncidentType.BROKEN_MACHINE -> {
                // Gets duration
                val duration: Int = obj["duration"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for positive or -1 duration
                if (duration <= 0 && duration != -1) throw ValidationException()

                // Get machine ID
                val machineid: Int = obj["machineId"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks if not negative
                if (machineid < 0) throw ValidationException()
                // Check if machine exists and get machine
                val machine = simData.machines[machineid] ?: throw ValidationException()

                lateinit var durationObject: Duration
                if (duration == -1) {
                    durationObject = Duration(-1, -1)
                } else {
                    durationObject = Duration(tick + 1, tick + duration)
                }

                incident = BrokenMachine(id, tick, incident_type, machine, durationObject)
            }

            IncidentType.CITY_EXPANSION -> {
                // Get location
                val tileID: Int = obj["location"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for if non-negative
                if (tileID < 0) throw ValidationException()
                // Check if tile exists and get tile
                val tile: Tile = simData.tiles[tileID] ?: throw ValidationException()
                // Add to cityExpansionTiles to validate sowing Plans
                cityExpansionTiles.add(tile)

                // VALIDATE EXISTENCE OF ADJOINING VILLAGE AT POINT OF INCIDENCE AFTER COLLECTING ALL INCIDENTS

                // !! Check toList function
                incident = CityExpansion(id, tick, incident_type, tile, simData.farms.values.toList())
            }

            IncidentType.CLOUD_CREATION -> {
                // Gets duration
                val duration: Int = obj["duration"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for positive or -1 duration
                if (duration <= 0 && duration != -1) throw ValidationException()

                // Get location
                val tileID: Int = obj["location"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for if non negative
                if (tileID < 0) throw ValidationException()
                // Check if tile exists and get tile
                val tile: Tile = simData.tiles[tileID] ?: throw ValidationException()

                // Get Radius
                val radius: Int = obj["radius"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for non-negative radius
                if (radius < 0) throw ValidationException()

                // Get amount of water on cloud
                val amount: Int = obj["amount"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks if positive
                if (amount <= 0) throw ValidationException()

                // VALIDATE WITH CITY EXPANSION TILES AND SOWING PLAN TILES AFTER COLLECTING ALL INCIDENTS

                incident = CloudCreation(id, tick, incident_type, tile, radius, duration, amount)

                // Add to cloudCreationIncidents
                cloudCreationIncidents.getOrPut(tick) { mutableListOf() }.add(incident)
            }

            IncidentType.DROUGHT -> {
                // Get location
                val tileID: Int = obj["location"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for if non negative
                if (tileID < 0) throw ValidationException()
                // Check if tile exists and get tile
                val tile: Tile = simData.tiles[tileID] ?: throw ValidationException()

                // Get Radius
                val radius: Int = obj["radius"]?.jsonPrimitive?.int ?: throw ValidationException()
                // Checks for non-negative radius
                if (radius < 0) throw ValidationException()

                // Validate affected tiles - CHECK FOR FIELD OR PLANTATION
                if (!validateDrought(tile, radius)) throw ValidationException()

                // Initialize CloudHandler at a later point when simData is fully initialized !!!!
                incident = Drought(id, tick, incident_type, tile, radius)
            }
        }

        // Add incidentID to incidentIDs list
        incidentIDs.add(id)

        return incident
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

    /*private fun validateAffectedTiles(tile: Tile, radius: Int, type: IncidentType): Boolean {
        return true
    }*/

    private fun validateCityExpansion(incidents: List<Incident>): Boolean {
        val cityExpansionIncidents: List<CityExpansion> = incidents.filterIsInstance<CityExpansion>()
            .sortedBy { it.tick }

        val newVillages: MutableList<Tile> = mutableListOf()

        for (cityExpansionIncident in cityExpansionIncidents) {
            val tiles = simData.map.getTilesByRadius(cityExpansionIncident.tile, 1)
            if (!tiles.any { it.category == TileType.VILLAGE } &&
                !newVillages.any { it.category == TileType.VILLAGE }
            ) {
                return false
            }
            if (cityExpansionIncident.tile.category != TileType.FIELD &&
                cityExpansionIncident.tile.category != TileType.ROAD
            ) {
                return false
            }
            newVillages.add(cityExpansionIncident.tile)
        }
        return true
    }

    private fun validateCloudCreation(incidents: List<Incident>): Boolean {
        val cloudCreationIncidents: List<CloudCreation> = incidents.filterIsInstance<CloudCreation>()
            .sortedBy { it.tick }
        // TODO
        return true
    }

    private fun parseClouds(clouds: JsonArray) {
        for (cloud in clouds) {
            val c = parseCloud(cloud.jsonObject)
            simData.clouds[c.id] = c
        }
    }

    private fun parseCloud(c: JsonObject): Cloud {
        // Get ID
        val id: Int = c["id"]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks if ID is positive
        if (id < 0) throw ValidationException()
        // Checks if the ID is unique
        if (!validateUniqueCloudIDs(id)) throw ValidationException()

        // Get location
        val location: Int = c["location"]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks if ID is positive
        if (location < 0) throw ValidationException()
        // Checks if this tile exists on the map and returns its coordinate
        val tile: Tile = simData.tiles[id] ?: throw ValidationException()
        // Checks if the tile already has a cloud on it
        if (!validateLocation(tile)) throw ValidationException()

        // Gets duration
        val duration: Int = c["duration"]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for positive or -1 duration
        if (duration <= 0 && duration != -1) throw ValidationException()

        // Gets amount
        val amount: Int = c["amount"]?.jsonPrimitive?.int ?: throw ValidationException()
        // Checks for positive amount
        if (amount < 0) throw ValidationException()

        val c = Cloud(id, tile.location, duration, amount)

        // Add cloud to CloudToTile Map
        cloudToTile[c] = tile

        // Add cloudID to cloudIDs list
        cloudIDs.add(id)

        return c
    }

    private fun validateLocation(t: Tile): Boolean {
        if (cloudToTile.containsValue(t)) {
            return false
        } else {
            return true
        }
    }

    private fun validateUniqueCloudIDs(id: Int): Boolean {
        if (cloudIDs.contains(id)) {
            return false
        } else {
            return true
        }
    }

    private fun validateUniqueIncidentIDs(id: Int): Boolean {
        if (incidentIDs.contains(id)) {
            return false
        } else {
            return true
        }
    }

    /*private fun checkOverlappingCloudCreation(): Boolean {
        // TODO
        return true
    }*/

    /**
     * TODO
     */
    private fun checkSowingPlanFields(plans: List<SowingPlan>): Boolean {
        // TODO
        return true
    }
}
