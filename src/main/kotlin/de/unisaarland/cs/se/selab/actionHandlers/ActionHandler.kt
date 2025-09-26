package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Abstract class representing a generic action handler for different phases of the simulation.
 * It provides common functionalities and enforces implementation of specific methods
 * for handling actions on tiles using machines.
 *
 * @param simulationMap The simulation map used to determine tile relationships and reachability.
 * @param plantdata The plant data used for accessing plant-related information.
 */
abstract class ActionHandler(
    val simulationMap: SimulationMap,
    val plantdata: PlantData
) {
    var operableTiles: List<Tile> = mutableListOf()

    /**
     * Starts phase with parameters farm, yearTick and simTick
     */
    abstract fun startPhase(farm: Farm, yearTick: Int, simTick: Int)

    /**
     * Starts phase with parameters farm and machine
     */
    abstract fun startPhase(farm: Farm, machine: Machine)

    /**
     * start phase, year tick used in mowing handler
     */
    abstract fun startPhase(farm: Farm, machine: Machine, yearTick: Int)

    /**
     * Performs action inside concrete action class with parameters machine and tile
     */
    abstract fun performAction(machine: Machine, tile: Tile)

    /**
     * performs action, year tick needed for cutting and mowing times
     */
    abstract fun performAction(machine: Machine, tile: Tile, yearTick: Int)

    /**
     * Gets operable tiles based on farm
     */
    abstract fun getOperableTiles(farm: Farm): List<Tile>

    /**
     * Gets operable tiles based on farm, plantType and yearTick
     */
    abstract fun getOperableTiles(farm: Farm, plant: PlantType, tick: Int): List<Tile>

    /**
     * Updates the tile hash map of the farm with the given tile
     */
    fun updateTileMap(farm: Farm, tile: Tile) {
        // TODO implementation just to build
        farm.tileHashMap.contains(tile.id)
    }

    /**
     * Updates the machine hash map of the farm with the given machine
     */
    fun updateMachineMap(farm: Farm, machine: Machine) {
        // TODO
        farm.machineHashMap.contains(machine.id)
    }

    /**
     * Returns the machine to the shed after completing its action
     */
    fun returnToShed(machine: Machine) {
        // TODO
        machine.currentHarvest ?: return
    }

    /**
     * Clears the tile hash maps
     */
    fun clearSets(farm: Farm) {
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()
    }

    /**
     * Retrieves a list of available machines that can perform the CUT action
     * on the given plant type. The machines are sorted by duration and ID.
     *
     * @param farm The farm containing the machines.
     * @param plantType The type of plant to be cut.
     * @return A list of machines that can perform the CUT action.
     */
    fun getAvailableMachines(farm: Farm, plantType: PlantType, actionType: ActionType): List<Machine> {
        return farm.getMachines().filter {
            !it.isStuck && it.plants.contains(plantType) && it.actions.contains(actionType)
        }.sortedWith(compareBy({ it.duration }, { it.id }))
    }

    /**
     * Returns the next machine that can sow the given tile from the list of machines that can sow the given plant
     * type. Returns null if no machine is available for the given tile.
     * Will check for machine availability in the farm's machineHashMap and reachability
     * Requires that the machines list is sorted by duration then id
     */
    fun getNextMachine(machines: List<Machine>, farm: Farm, destination: Tile): Machine? {
        for (machine in machines) {
            if (!farm.machineHashMap.contains(machine.id) && this.simulationMap.isReachable(machine, destination)) {
                farm.machineHashMap.add(machine.id)
                return machine
            }
        }
        return null
    }

    /**
     * Gets all tiles (plantation and field) on which the action can be performed,
     * with plantation tiles first, then field tiles, both sorted by id.
     */
    fun getOperableTiles(farm: Farm, actionType: ActionType): MutableList<Tile> {
        val plantationTiles = farm.getPlantation()
            .filter { it.id !in farm.tileHashMap }
            .filter { it.plant != null && it.actionsNeeded.contains(actionType) }
            .sortedBy { it.id }
        val fieldTiles = farm.getFields()
            .filter { it.id !in farm.tileHashMap }
            .filter { it.plant != null && it.actionsNeeded.contains(actionType) }
            .sortedBy { it.id }
        return (plantationTiles + fieldTiles).toMutableList()
    }

//    /**
//     * Performs the CUT action on a tile using the given machine.
//     * Updates the machine's state and removes the CUT action from the plant's required actions.
//     *
//     * @param machine The machine performing the action.
//     * @param tile The tile on which the action is performed.
//     */
//    fun performAction(
//        machine: Machine,
//        tile: Tile,
//        yearTick: Int
//    ) {
//        machine.currentTile = tile
//        machine.updateElapsedTime()
//
//        val plant = tile.plant
//        plant?.actionsNeeded?.remove(ActionType.CUTTING)
//        if (plant != null) {
//            for (element in plant.cuttingTime) {
//                if (element.first.inRange(yearTick)) {
//                    element.second = true
//                }
//            }
//        }
//
//        // Log the action
//        logFarmAction(machine.farmID, ActionType.CUTTING, tile.id, machine.duration)
//    }
}
