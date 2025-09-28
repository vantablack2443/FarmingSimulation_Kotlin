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

    /**\
     * Handles the main logic of the irrigation phase, starting by getting operable tiles and then
     * checks for the target tile to perform actions and also for action continuation
     */
    fun startPhase(farm: Farm, machine: Machine, tileType: TileType) {
        val operableTiles = getOperableTiles(farm, tileType).toMutableList()
        if (operableTiles.isEmpty()) {
            return
        }

        // get target tile for first action
        val targetTile = findTargetTile(machine, operableTiles) ?: return

        // perform action on target tile
        performAction(machine, targetTile)
        // add tile to farm's tileHashMap so that it won't
        // be performed on again in this tick
        farm.tileHashMap.add(targetTile.id)
        // remove tile from operableTiles
        operableTiles.remove(targetTile)

        // try to continue action
        var continueTile: Tile? = continueAction(machine, operableTiles)

        while (continueTile != null && machine.canPerform()) {
            performAction(machine, continueTile)
            farm.tileHashMap.add(continueTile.id)
            operableTiles.remove(continueTile)
            continueTile = continueAction(machine, operableTiles)
        }

        // machine cannot perform anymore
        farm.machineHashMap.add(machine.id)
        machine.resetElapsedTime()

        val returnShed: Tile? = simulationMap.findTargetShed(
            machine,
            farm.getFarmstead().filter { it.shed == true }.sortedBy { it.id },
            machine.currentHarvest != null
        )

        if (returnShed == null) {
            machine.isStuck = true
        } else {
            machine.currentTile = returnShed
            machine.homeShed = returnShed
        }
    }

    /**
     * Performs the irrigation action on the specified tile using the given machine.
     */
    override fun performAction(machine: Machine, tile: Tile) {
        // call log farming action here
        Logger.logFarmAction(machine.id, ActionType.IRRIGATING, tile.id, machine.duration)
        machine.currentTile = tile
        machine.updateElapsedTime()
        val currentMoisture = tile.currentMoisture ?: error("Current moisture null or invalid")
        val maxMoisture = tile.maxMoisture ?: error("Max moisture null or invalid")
        val amount = maxMoisture - currentMoisture
        tile.increaseMoistureByAmount(amount)
        tile.actionsNeeded.remove(ActionType.IRRIGATING)
    }

    /**
     * Finds the target tile with the lowest ID that is reachable by the machine from the list of operable tiles.
     */
    private fun findTargetTile(m: Machine, operableTiles: List<Tile>): Tile? {
        val reachableTiles = operableTiles.filter { tile -> simulationMap.isReachable(m, tile) }
        if (reachableTiles.isEmpty()) return null
        return reachableTiles.minBy { it.id }
    }

    /**
     * Returns a list of operable tiles that need irrigation and are not already handled in the current tick.
     */
    fun getOperableTiles(farm: Farm, tileType: TileType): List<Tile> {
        val operableTiles = mutableListOf<Tile>()
        var tiles = emptyList<Tile>()
        if (tileType == TileType.FIELD) {
            tiles = farm.getFields()
        }
        if (tileType == TileType.PLANTATION) {
            tiles = farm.getPlantation()
        }
        for (tile in tiles) {
            if (tile.actionsNeeded.contains(ActionType.IRRIGATING) && tile.id !in farm.tileHashMap) {
                operableTiles.add(tile)
            }
        }
        return operableTiles.sortedBy { it.id }
    }

    /**
     * Finds the next tile to continue the irrigation action within a radius of 2 from the machine's current tile.
     */
    private fun continueAction(m: Machine, operableTiles: List<Tile>): Tile? {
        val remainingTiles = simulationMap.getTilesByRadius(m.currentTile, 2)
            .filter { tile -> operableTiles.contains(tile) }
            .filter { tile -> simulationMap.isReachable(m, tile) }
            .sortedBy { it.id }

        return if (remainingTiles.isEmpty()) {
            null
        } else {
            remainingTiles.first()
        }
    }

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
