package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
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
    override fun startPhase(farm: Farm, machine: Machine) {
        val operableTiles = getOperableTiles(farm).toMutableList()
        if (operableTiles.isEmpty()) {
            return
        }

        // get target tile for first action
        val targetTile = findTargetTile(machine, operableTiles)
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
        machine.resetElapsedTime()

        val returnShed: Tile? = simulationMap.findTargetShed(
            machine,
            farm.getFarmstead().filter { it.shed == true },
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
        machine.currentTile = tile
        machine.updateElapsedTime()
        val currentMoisture = tile.currentMoisture ?: error("Current moisture null or invalid")
        val maxMoisture = tile.maxMoisture ?: error("Max moisture null or invalid")
        val amount = maxMoisture - currentMoisture
        tile.increaseMoistureByAmount(amount)
    }

    /**
     * Finds the target tile with the lowest ID that is reachable by the machine from the list of operable tiles.
     */
    private fun findTargetTile(m: Machine, operableTiles: List<Tile>): Tile {
        return operableTiles
            .filter { tile -> simulationMap.isReachable(m, tile) }
            .minBy { it.id }
    }

    /**
     * Returns a list of operable tiles that need irrigation and are not already handled in the current tick.
     */
    override fun getOperableTiles(farm: Farm): List<Tile> {
        val operableTiles = mutableListOf<Tile>()
        for (tile in farm.getFields()) {
            if (!tile.hasPlantGrowing()) continue
            if (tile.needsIrrigation() && tile.id !in farm.tileHashMap) {
                operableTiles.add(tile)
            }
        }
        for (tile in farm.getPlantation()) {
            if (!tile.hasPlantGrowing()) continue
            if (tile.needsIrrigation() && tile.id !in farm.tileHashMap) {
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
    override fun getOperableTiles(farm: Farm, plant: PlantType, tick: Int): List<Tile> {
        return emptyList()
    }

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        return
    }

    /** why was this deleted in actionhandler?
     * override fun getOperableTiles(farm: Farm, plant: PlantType): List<Tile> {
     *         error("performAction() is not implemented in IrrigationHandler")
     *     }
     */
}
