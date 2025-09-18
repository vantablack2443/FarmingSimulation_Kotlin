package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonPrimitive

import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import org.jetbrains.kotlin.gradle.utils.`is`
import kotlin.text.get


class FarmParser(private val simulationData: SimulationData) {
    private lateinit var farmIDs: List<Int>
    private lateinit var farmNames: List<String>
    private lateinit var machineIDs: List<Int>
    private lateinit var machineNames: List<String>
    private lateinit var sowingPlanIDs: List<Int>
    private lateinit var sowingPlanTicks: MutableMap<Int, List<SowingPlan>>


    fun parse(json: String): Unit {
        val jsonObject = JsonObject(json) // todo

        // Parse main components
        val farms = jsonObject["farms"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Farms array is missing or not an array.")
        val machines = jsonObject["machines"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Machines array is missing or not an array.")
        val sowingPlans = jsonObject["sowingPlans"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Sowing plans is missing or not an array.")

        parseFarms(farms)
        parseMachines(machines)
        parseSowingPlans(sowingPlans)

        simulationData.farms().forEach { farm ->
            if (!validateFarmTiles(farm)) {
                throw IllegalArgumentException("Farm ${farm.id} has invalid tiles.")
            }
        }

        val isValid = validateUniqueAttributes() && crossValidateFarmMachine()
        if (!isValid) {
            throw IllegalArgumentException("Validation failed for farms, machines, or sowing plans.")
            // logging should be done here as file invalid
        }
        // logging should be done here as file valid
    }

    private fun parseFarms(farms: JsonArray) {
        var mapOfFarms = mutableMapOf<Int, Farm>()
        for (farm in farms) {
            val farm = parseFarm(farm as JsonObject)
            // gets ID and then sets one by one to mapofFarms
            mapOfFarms[farm.id] = farm
        }
        simulationData.setFarms(mapOfFarms)
    }

    private fun parseFarm(farm: JsonObject): Farm {
        val id = farm["id"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Farm id is missing or not an integer.")
        if (id < 0) throw IllegalArgumentException("Farm id is invalid.")

        val name = farm["name"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Farm name is missing or not a string.")
        if (name.isEmpty()) throw IllegalArgumentException("Farm name is invalid.")

        val farmsteads = farm["farmsteads"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Farmsteads array is missing or not an array.")
        val farmsteadTiles = farmsteads.map { farmstead ->
            val tileID = farmstead.jsonPrimitive.int
            simulationData.tiles[tileID] ?: throw IllegalArgumentException("Tile $tileID not found.")
        }

        val fields = farm["fields"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Fields array is missing or not an array.")
        val fieldTiles = fields.map { field ->
            val tileID = field.jsonPrimitive.int
            simulationData.tiles[tileID] ?: throw IllegalArgumentException("Tile $tileID not found.")
        }

        val plantations = farm["plantations"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Plantations array is missing or not an array.")
        val plantationTiles = plantations.map { plantation ->
            val tileID = plantation.jsonPrimitive.int
            simulationData.tiles[tileID] ?: throw IllegalArgumentException("Tile $tileID not found.")
        }

        val machineParsing = parseMachines(farm["machines"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Machines array is missing or not an array."))
        val machines = farm["machines"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Machines array is missing or not an array.")
        val allMachines = machines.map { machine ->
            val machineID = machine.jsonPrimitive.int
            simulationData.machines[machineID] ?: throw IllegalArgumentException("Machine $machineID not found.")
        }

        val sowingPlansParsing = parseSowingPlans(farm["sowingPlans"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Sowing plans array is missing or not an array."))
        val sowingPlans = farm["sowingPlans"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Sowing plans array is missing or not an array.")
        val allSowingPlans = sowingPlans.map { sowingPlan ->
            val sowingPlanID = sowingPlan.jsonPrimitive.int
            simulationData.sowingPlans[sowingPlanID] ?: throw IllegalArgumentException("Sowing plan $sowingPlanID not found.")
        }
        val sowingPlanMap: MutableMap<Int, List<SowingPlan>> = mutableMapOf()
        allSowingPlans.forEachIndexed { index, sowingPlanList ->
            sowingPlanMap[index] = sowingPlanList
        }

        return Farm(id, name, farmsteadTiles, fieldTiles, plantationTiles, allMachines, sowingPlanMap, mutableMapOf()) // harvestPerPlant is done at the end
    }

    private fun parseMachines(machines: JsonArray) {
        var mapOfMachines = mutableMapOf<Int, Machine>()
        for (machine in machines) {
            val machine = parseMachine(machine as JsonObject)
            mapOfMachines[machine.id] = machine
        }
        simulationData.setMachines(mapOfMachines)
    }

    private fun parseMachine(m: JsonObject): Machine {
        val id = m["id"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Machine id is missing or not an integer.")
        if (id < 0) throw IllegalArgumentException("Machine id is invalid.")

        val name = m["name"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Machine name is missing or not a string.")
        if (name.isEmpty()) throw IllegalArgumentException("Machine name is invalid.")

        val actions = m["actions"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Actions array is missing or not an array.")
        if (actions.isEmpty()) throw IllegalArgumentException("Actions array is empty.")
        val listOfActions : List<ActionType> = actions.map { action ->
            ActionType.valueOf(action.jsonPrimitive.content.uppercase())
        }

        val plants = m["plants"]?.jsonArray?.jsonArray ?: throw IllegalArgumentException("Plants array is missing or not an array.")
        if (plants.isEmpty()) throw IllegalArgumentException("Plants array is empty.")
        val listOfPlants : List<PlantType> = plants.map { plant ->
            PlantType.valueOf(plant.jsonPrimitive.content.uppercase())
        }

        val duration = m["duration"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Duration is missing or not an integer.")
        if (duration !in 1..14) throw IllegalArgumentException("Duration is invalid.")

        val location = m["location"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Location is missing or not an integer.")
        if (location < 0) throw IllegalArgumentException("Location is invalid.")
        val tile : Tile = simulationData.tiles[location] ?: throw IllegalArgumentException("Tile $location not found.")

        val isValid = validateMachineLocation(location) && validateActionsAndPlants(actions, plants)
        if (!isValid) {
            throw IllegalArgumentException("Location $location not found.")
        }

        return Machine(id, name, duration, tile, listOfActions, listOfPlants, homeShed = tile)
    }

    private fun parseSowingPlans(plans: JsonArray): Unit {
        var mapOfSowingPlans: MutableMap<Int, List<SowingPlan>> = mutableMapOf()
        for (plan in plans) {
            val p = parseSowingPlan(plan as JsonObject)
            mapOfSowingPlans[plan.id] = plan
        }
        simulationData.setSowingPlans(mapOfSowingPlans)
    }

    private fun parseSowingPlan(plan: JsonObject): SowingPlan {
       val id = plan["id"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Sowing plan id is missing or not an integer.")
       if (id < 0) throw IllegalArgumentException("Sowing plan id is invalid.")

        val tick = plan["tick"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Sowing plan tick is missing or not an integer.")
        if (tick < 0) throw IllegalArgumentException("Sowing plan tick is invalid.")

        val plant = plan["plant"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Sowing plan plant is missing or not a string.")
        val plantType = PlantType.valueOf(plant.toUpperCase())

        val tiles : List<Tile> = if (plan["location"] != null && plan["radius"] != null) {
            val location = plan["location"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Location missing or not an integer")
            val radius = plan["radius"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Radius missing or not an integer")

            if (location < 0 || radius < 0) throw IllegalArgumentException("Location is invalid.")

            val centerTile = simulationData.tiles[location] ?: throw IllegalArgumentException("Tile $location not found.")
            val radiusTiles = map.getTilesByRadius(centerTile, radius) // TODO double check

            if (!validateSowingPlanLocation(radiusTiles, true)) {
                throw IllegalArgumentException("Sowing plan location is invalid.")
            }

        } else if (plan["tiles"] != null) {
            val tileArray = plan["tiles"]?.jsonPrimitive?.jsonArray ?: throw IllegalArgumentException("Tiles array missing")
            val arrayofTiles = tileArray.map { tileJson ->
                val tileID = tileJson.jsonPrimitive.int
                simulationData.tiles[tileID] ?: throw IllegalArgumentException("Tile $tileID not found.")
            }
            if (!validateSowingPlanLocation(arrayofTiles, false)) {
                throw IllegalArgumentException("Sowing plan location is invalid.")
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
                    if (!plantList.contains(PlantType.WHEAT, PlantType.OAT, PlantType.PUMPKIN, PlantType.POTATO))
                        return false
                }
                ActionType.CUT, ActionType.MOW -> {
                    if (!plantList.contains(PlantType.APPLE, PlantType.GRAPE, PlantType.CHERRY, PlantType.ALMOND))
                        return false
                }
                else -> { return true }
            }
        }
        return true
    }

    private fun validateMachineLocation(tileID: Int): Boolean {
        val tile = simulationData.tiles[tileID] ?: return false
        return tile.category == TileType.FARMSTEAD && tile.shed == true
    }

    private fun validateUniqueAttributes(): Boolean {
        // TODO
    }

    private fun crossValidateFarmMachine(): Boolean {
        // TODO
    }

    private fun validateSowingPlanLocation(tiles: List<Tile>, fields: Boolean): Boolean {
     // TODO
    }

    private fun validateFarmTiles(f: Farm): Boolean {
        // TODO
    }

}