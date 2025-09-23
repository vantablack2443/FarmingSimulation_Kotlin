package de.unisaarland.cs.se.selab.actionHandlers

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
    var tileMap: HashSet<Int> = hashSetOf()

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
}
