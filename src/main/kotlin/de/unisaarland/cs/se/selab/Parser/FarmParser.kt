package de.unisaarland.cs.se.selab.Parser

import org.json.JSONObject
import org.json.JSONArray

import de.unisaarland.cs.se.selab.SimulationData

class FarmParser(private val simulationData: SimulationData) {
    private var simulationData: SimulationData
    private var farmIDs: List<Int>
    private var farmNames: List<String>
    private var machineIDs: List<Int>
    private var machineNames: List<String>
    private var sowingPlanIDs: List<Int>
    private var sowingPlanTicks: MutableMap<Int, List<SowingPlan>>


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