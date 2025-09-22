package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

class IrrigationHandler(
    simulationMap: SimulationMap,
    plantdata: PlantData
) : ActionHandler(
    simulationMap,
    plantdata
) {

    override fun startPhase(farm: Farm, m: Machine) {
        // TODO
    }

    override fun performAction(m: Machine, tile: Tile) {
        // TODO
    }

    private fun findTargetTile(m: Machine, operableTiles: List<Tile>): Tile {
        // TODO
    }

    override fun getOperableTiles(f: Farm): List<Tile> {
        var operableTiles = mutableListOf<Tile>()
        for (tile in f.getFields()) {
            if (!tile.hasPlantGrowing()) continue
            if (tile.currentMoisture!! < tile.maxMoisture!!) {
                operableTiles.add(tile)
            }
        }
        for (tile in f.getPlantation()) {
            if (!tile.hasPlantGrowing()) continue
            if (tile.currentMoisture!! < tile.maxMoisture!!) {
                operableTiles.add(tile)
            }
        }
        return operableTiles.sortedBy { it.id }
    }

    private fun continueAction(m: Machine, operableTiles: List<Tile>): Tile {
        // TODO
    }
}
