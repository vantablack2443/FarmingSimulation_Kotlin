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
    val operableTiles: List<Tile> = mutableListOf()
    val tileMap: HashSet<Int> = hashSetOf()
    abstract fun startPhase(farm: Farm, yearTick: Int, simTick: Int): Unit
    abstract fun startPhase(farm: Farm, machine: Machine): Unit
    abstract fun performAction(machine: Machine, tile: Tile): Unit
    abstract fun getOperableTiles(farm: Farm): List<Tile>
    abstract fun getOperableTiles(farm: Farm, plant: PlantType, yearTick: Int): List<Tile>


    fun updateTileMap(farm: Farm, tile: Tile) {
        TODO()
    }
    fun updateMachineMap(farm: Farm, machine: Machine) {
        TODO()
    }
    fun returnToShed(machine: Machine) {
        TODO()
    }

    fun clearSets(farm: Farm) {
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()
    }
}
