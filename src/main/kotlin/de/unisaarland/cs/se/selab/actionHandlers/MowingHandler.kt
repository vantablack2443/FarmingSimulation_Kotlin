package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * handler to perform the mowing action on the given farm and machine
 * @param simulationMap map of the simulation
 * @param plantdata timing data on the plants
 */
class MowingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    /**
     * not used for mowing handler
     */
    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        throw NotImplementedError("getOperableTiles is not implemented in MowingHandler")
    }

    /**
     * start the mowing handler
     * @param farm the farm mowing would be performed on
     * @param machine the machine that would perform mowing
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
                machine.currentTile = machine.homeShed
                machine.resetElapsedTime()
                farm.machineHashMap.add(machine.id)
                Logger.logMachineFinish(machine.id, machine.homeShed.id)
                break
            }
        }
    }

    /**
     * performs the action of mowing
     * @param machine the machine that performs the action
     * @param tile the tile that the action would be performed in
     */
    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
        Logger.logFarmAction(machine.id, ActionType.MOW, tile.id, machine.duration)
        machine.currentTile = tile
        machine.updateElapsedTime()
        tile.plant?.actionsNeeded?.remove(ActionType.MOW)
    }

    /**
     * helper function for startPhase
     * @param machine the machine that will do the actions
     * @param farm the farm that owns the teils and machine
     * @param operableTiles the tiles that should be performed on
     */
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
     * not used for mowing handler
     */
    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        yearTick: Int
    ): List<Tile> {
        throw NotImplementedError("getOperableTiles is not implemented in MowingHandler")
    }
}
