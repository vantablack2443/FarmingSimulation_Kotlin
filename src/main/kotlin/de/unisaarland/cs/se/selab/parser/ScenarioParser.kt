package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.tile.Tile
import org.json.JSONArray
import org.json.JSONObject

class ScenarioParser (private val simData:SimulationData) {
    private var incidentID: List<Int> = mutableListOf()
    private var cloudIDs: List<Int> = mutableListOf()
    private var cloudToTile: MutableMap<Cloud, Tile> = mutableMapOf()
    private var cloudCreationIncidents: MutableMap<Int, List<Incident>> = mutableMapOf() // map from tick to cloud creation incidents
    private var cityExpansionTiles: List<Tile> = mutableListOf() // for double-checking against sowing plan tiles

    fun parse(json: String) {
        TODO()
    }

    private fun parseIncidents(incidents: JSONArray) {
        TODO()
    }

    private fun parseIncident(obj: JSONObject): Incident {
        TODO()
    }

    private fun validateIncident(type: IncidentType): Boolean {
        TODO()
    }

    private fun validateAffectedTiles(tile: Tile, radius: Int, type: IncidentType): Boolean {
        TODO()
    }

    private fun validateCloudCreation(incidents: List<Incident>): Boolean {
        TODO()
    }

    private fun parseClouds(clouds: JSONArray) {
        TODO()
    }

    private fun parseCloud(c: JSONObject): Cloud {
        TODO()
    }

    private fun validateLocation(t: Tile): Boolean {
        TODO()
    }

    private fun validateUniqueIDs(): Boolean {
        TODO()
    }

    private fun checkOverlappingCloudCreation(): Boolean {
        TODO()
    }

    private fun checkSowingPlanFields(plan: SowingPlan): Boolean {
        TODO()
    }
}