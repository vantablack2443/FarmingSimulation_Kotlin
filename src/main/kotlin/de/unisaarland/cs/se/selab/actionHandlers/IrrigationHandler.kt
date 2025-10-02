package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * This class handles the irrigation action of the farming phase.
 */
class IrrigationHandler(
    simulationMap: SimulationMap,
    plantdata: PlantData
) : ActionHandler(
    simulationMap,
    plantdata
) {
    /*
    CHECK IF YOU CAN IRRIGATE PLANTATIONS RIGHT AFTER FIRST FIELD IRRIGATION
     */

    /**\
     * Handles the main logic of the irrigation phase, starting by getting operable tiles and then
     * checks for the target tile to perform actions and also for action continuation
     */
    private fun performAction(farm: Farm, machine: Machine, tile: Tile) {
        Logger.logFarmAction(machine.id, ActionType.IRRIGATING, tile.id, machine.duration)

        machine.currentTile = tile
        machine.updateElapsedTime()

        val currentMoisture = tile.currentMoisture ?: error("Current moisture null or invalid")
        val maxMoisture = tile.maxMoisture ?: error("Max moisture null or invalid")
        val amount = maxMoisture - currentMoisture
        tile.increaseMoistureByAmount(amount)

        tile.actionsNeeded.remove(ActionType.IRRIGATING)
        farm.tileHashMap.add(tile.id)
    }

    /**
     * Returns a list of operable tiles that need irrigation and are not already handled in the current tick.
     */
    private fun getOperableTiles(farm: Farm, tileType: TileType): List<Tile> {
        var tiles = emptyList<Tile>()

        if (tileType == TileType.FIELD) {
            tiles = farm.getFields()
        }
        if (tileType == TileType.PLANTATION) {
            tiles = farm.getPlantation()
        }

        val operableTiles = tiles
            .filter { it.id !in farm.tileHashMap }
            .filter { it.plant != null && it.actionsNeeded.contains(ActionType.IRRIGATING) }
            .sortedBy { it.id }
        return operableTiles
    }

    /**\
     * Handles the main logic of the irrigation phase, starting by getting operable tiles and then
     * checks for the target tile to perform actions and also for action continuation
     */
    fun startPhase(farm: Farm, machine: Machine, tileType: TileType) {
        val operableTiles = getOperableTiles(farm, tileType)
        if (operableTiles.isEmpty()) {
            return
        }
        for (tile in operableTiles) {
            if (tile.id in farm.tileHashMap || tile.currentCrop == null) {
                continue // Skip if tile is already handled
            }
            val plantType = tile.currentCrop
            if (machine.plants.contains(plantType) && simulationMap.isReachable(machine, tile)) {
                // perform action on target tile
                performAction(farm, machine, tile)

                // continue action
                val irrigatableFields = getOperableTiles(farm, TileType.FIELD)
                val irrigatablePlantations = getOperableTiles(farm, TileType.PLANTATION)
                if (tileType == TileType.FIELD) {
                    continueOperationField(
                        machine,
                        (irrigatableFields + irrigatablePlantations).sortedBy { it.id },
                        farm
                    )
                } else {
                    continueOperationPlantation(
                        machine,
                        irrigatablePlantations,
                        farm
                    )
                }

                farm.machineHashMap.add(machine.id)
                machine.currentTile = machine.homeShed
                machine.resetElapsedTime()
                Logger.logMachineFinish(machine.id, machine.homeShed.id)
                break
            }
        }
    }

    private fun continueOperationField(machine: Machine, operableTiles: List<Tile>, farm: Farm) {
        if (!machine.canPerform()) {
            return
        }
        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
            .filter { it.id !in farm.tileHashMap }
            .filter { it.currentCrop in machine.plants }
        // This set will contain all tiles and plantation tiles ordered by id
        val irrigatableTiles = operableTiles.intersect(reach.toSet()).sortedBy { it.id }

        val nextFieldTile = irrigatableTiles.firstOrNull { it.category == TileType.FIELD }
        if (nextFieldTile != null) {
            performAction(farm, machine, nextFieldTile)
            continueOperationField(machine, operableTiles, farm)
        } else {
            val nextPlantationTile = irrigatableTiles.firstOrNull { it.category == TileType.PLANTATION }
            if (nextPlantationTile != null) {
                performAction(farm, machine, nextPlantationTile)
                continueOperationField(machine, operableTiles, farm)
            }
        }
    }

    private fun continueOperationPlantation(machine: Machine, operableTiles: List<Tile>, farm: Farm) {
        if (!machine.canPerform()) {
            return
        }
        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
            .filter { it.id !in farm.tileHashMap }
            .filter { it.currentCrop in machine.plants }

        val irrigatablePlantations = operableTiles.intersect(reach.toSet()).sortedBy { it.id }

        if (irrigatablePlantations.isNotEmpty()) {
            performAction(farm, machine, irrigatablePlantations.first())
            continueOperationPlantation(machine, operableTiles, farm)
        }
    }

    /**
     * Finds the next tile to continue the irrigation action within a radius of 2 from the machine's current tile.
     */

    /**
     * These functions aren't implemented inside IrrigationHandler, probably declare them as open in
     * ActionHandler and then override them in the classes that need them.
     */

    override fun performAction(
        machine: Machine,
        tile: Tile,
        yearTick: Int
    ) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine, yearTick: Int) {
        return
    }

    override fun performAction(machine: Machine, tile: Tile) {
        return
    }

    override fun getOperableTiles(farm: Farm, plant: PlantType, tick: Int): List<Tile> {
        return emptyList()
    }

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine) {
        return
    }

    override fun getOperableTiles(farm: Farm): List<Tile> {
        return emptyList()
    }
}
