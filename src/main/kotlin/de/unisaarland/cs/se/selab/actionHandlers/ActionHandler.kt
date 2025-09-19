package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

abstract class ActionHandler (
    val simulationMap: SimulationMap,
    val plantdata: PlantData,
    val operableTiles: List<Tile>,
    val tileMap: HashSet<tileID>

){
    abstract fun startPhase(farm: Farm, yearTick: Int, simTick: Int): Unit
    abstract fun startPhase(farm: Farm, machine: Machine): Unit
    abstract fun performAction(machine: Machine, tile: Tile): Unit
    abstract fun getOperableTiles(farm: Farm, plant: PlantType): List<Tile>
    abstract fun getOperableTiles(farm: Farm): List<Tile>
    abstract fun getOperableTiles(farm: Farm, plant: PlantType, yearTick: Int): List<Tile>
    fun updateTileMap(farm: Farm, tile: Tile): Unit {
        TODO()
    }
    fun updateMachineMap(farm: Farm, machine: Machine): Unit {
        TODO()
    }
    fun returnToShed(machine: Machine): Unit {
        TODO()
    }
}