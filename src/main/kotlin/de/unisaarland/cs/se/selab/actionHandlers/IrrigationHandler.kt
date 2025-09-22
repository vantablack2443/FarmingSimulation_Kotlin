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
    override fun startPhase(farm: Farm, m: Machine) {
        val operableTiles = getOperableTiles(farm) as MutableList<Tile>
        if (operableTiles.isEmpty()) {
            return
        }

        // get target tile for first action
        val targetTile = findTargetTile(m, operableTiles)
        // perform action on target tile
        performAction(m, targetTile)
        // add tile to farm's tileHashMap so that it won't
        // be performed on again in this tick
        farm.tileHashMap.add(targetTile.id)
        // remove tile from operableTiles
        operableTiles.remove(targetTile)

        // try to continue action
        var continueTile: Tile? = continueAction(m, operableTiles)

        while (continueTile != null && m.canPerform()) {
            performAction(m, continueTile)
            farm.tileHashMap.add(continueTile.id)
            operableTiles.remove(continueTile)
            continueTile = continueAction(m, operableTiles)
        }

        // machine cannot perform anymore
        m.resetElapsedTime()

        val returnShed: Tile? = simulationMap.findTargetShed(
            m,
            farm.getFarmstead().filter { it.shed == true },
            m.currentHarvest != null
        )

        if (returnShed == null) {
            m.isStuck = true
        } else {
            m.currentTile = returnShed
            m.homeShed = returnShed
        }
    }

    /**
     * Performs the irrigation action on the specified tile using the given machine.
     */
    override fun performAction(m: Machine, tile: Tile) {
        // call log farming action here
        m.currentTile = tile
        m.updateElapsedTime()
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
    override fun getOperableTiles(f: Farm): List<Tile> {
        val operableTiles = mutableListOf<Tile>()
        for (tile in f.getFields()) {
            if (!tile.hasPlantGrowing()) continue
            if (tile.needsIrrigation() && tile.id !in f.tileHashMap) {
                operableTiles.add(tile)
            }
        }
        for (tile in f.getPlantation()) {
            if (!tile.hasPlantGrowing()) continue
            if (tile.needsIrrigation() && tile.id !in f.tileHashMap) {
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
    override fun getOperableTiles(farm: Farm, plant: PlantType, yearTick: Int): List<Tile> {
        error("getOperableTiles(farm, plant, yearTick) is not implemented in IrrigationHandler")
    }

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        error("startPhase(farm, yearTick, simTick) is not implemented in IrrigationHandler")
    }

    /** why was this deleted in actionhandler?
     * override fun getOperableTiles(farm: Farm, plant: PlantType): List<Tile> {
     *         error("performAction() is not implemented in IrrigationHandler")
     *     }
     */
}
