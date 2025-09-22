package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 *
 */
class MowingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    /**
     * idk
     */
    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        TODO()
    }

    /**
     * im still on draft
     */
    override fun startPhase(
        farm: Farm,
        machine: Machine
    ) {
        val operableTiles = getOperableTiles(farm)
        for (tile in operableTiles) {
            if (tile.currentCrop in machine.plants && simulationMap.isReachable(machine, tile)) {
                performAction(machine, tile)
                farm.tileHashMap.add(tile.id)
                continueAction(machine, farm, operableTiles)
                farm.machineHashMap.add(machine.id)
                break
            }
        }
    }

    /**
     * idk abt this
     */
    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
        machine.currentTile = tile
        machine.updateElapsedTime()
        tile.plant?.actionsNeeded?.remove(ActionType.MOW)
    }

    private fun continueAction(machine: Machine, farm: Farm, operableTiles: List<Tile>) {
        while (machine.canPerform()) {
            val accessibleTiles = simulationMap.getReachableTiles(machine, 2, false)
            for (tile in operableTiles) {
                val opTile = tile.id in farm.tileHashMap
                if (tile in accessibleTiles && !opTile) {
                    performAction(machine, tile)
                    farm.tileHashMap.add(tile.id)
                    break
                }
            }
        }
    }

    /**
     * which to use
     */
    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType
    ): List<Tile> {
        TODO()
    }

    /**
     * gets all tiles that needs mowing
     * @param farm all mowable tiles are stored in farm
     */
    override fun getOperableTiles(farm: Farm): List<Tile> {
        val operableTiles: MutableList<Tile> = mutableListOf()
        for (tile in farm.getPlantation()) {
            if (tile.id in farm.tileHashMap) continue
            if (tile.plantationDamaged != null && tile.plantationDamaged == true) continue
            if (tile.requiresMowing()) {
                operableTiles.add(tile)
            }
        }
        return operableTiles.sortedBy { it.id }
    }

    /**
     * unused function
     */
    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        yearTick: Int
    ): List<Tile> {
        TODO()
    }
}
