package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData

class CuttingHandler(simulationMap: SimulationMap, plantdata: PlantData, operableTiles: List<Tile>,
                     tileMap: HashSet<Int>
) : ActionHandler(simulationMap, plantdata,
    operableTiles, tileMap
) {

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        TODO("Not yet implemented")
    }

    override fun startPhase(
        farm: Farm,
        machine: Machine
    ) {
        TODO("Not yet implemented")
    }

    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
        TODO("Not yet implemented")
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType
    ): List<Tile> {
        TODO("Not yet implemented")
    }

    override fun getOperableTiles(farm: Farm): List<Tile> {
        TODO("Not yet implemented")
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        yearTick: Int
    ): List<Tile> {
        TODO("Not yet implemented")
    }
}
