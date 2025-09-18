package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.SowingPlan
import org.json.JSONObject
import org.json.JSONArray

import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.tile.Tile

class FarmParser(private val simulationData: SimulationData) {
    private lateinit var simulationData: SimulationData
    private lateinit var farmIDs: List<Int>
    private lateinit var farmNames: List<String>
    private lateinit var machineIDs: List<Int>
    private lateinit var machineNames: List<String>
    private lateinit var sowingPlanIDs: List<Int>
    private lateinit var sowingPlanTicks: MutableMap<Int, List<SowingPlan>>


    fun parse(json: String): Unit {
            // TODO
    }

    private fun parseFarms(farms: JSONArray): Unit {
        // TODO
    }

    private fun parseFarm(f: JSONObject): Farm {
        // TODO
    }

    private fun validateFarmTiles(f: Farm):  Boolean {
        // TODO
    }

    private fun parseMachines(machines: JSONArray):Unit {
        // TODO
    }

    private fun parseMachine(m: JSONObject): Machine {
        // TODO
    }

    private fun validateActionsAndPlants(actions: JSONArray, plants: JSONArray): Boolean {
        // TODO
    }

    private fun validateMachineLocation(tileID: Int): Boolean {
        // TODO
    }

    private fun validateUniqueAttributes(): Boolean {
        // TODO
    }

    private fun crossValidateFarmMachine(): Boolean {
        // TODO
    }

    private fun parseSowingPlans(plans: JSONArray): Unit {
        // TODO
    }

    private fun parseSowingPlan(plan: JSONObject): SowingPlan {
    // TODO
    }

    private fun validateSowingPlanLocation(tiles: List<Tile>, fields: Boolean): Boolean {
     // TODO
    }


}