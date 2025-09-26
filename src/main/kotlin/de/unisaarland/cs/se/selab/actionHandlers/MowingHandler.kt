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
 */
class MowingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    /**
     * start the mowing handler
     * @param farm the farm mowing would be performed on
     * @param machine the machine that would perform mowing
     */
    override fun startPhase(
        farm: Farm,
        machine: Machine,
        yearTick: Int
    ) {
        if (machine.id in farm.machineHashMap) {
            return
        }
        val operableTiles = getOperableTiles(farm, actionType = ActionType.MOWING)
        this.operableTiles = operableTiles
        for (tile in operableTiles) {
            if (tile.currentCrop in machine.plants && simulationMap.isReachable(machine, tile)) {
                performAction(machine, tile)
                farm.tileHashMap.add(tile.id)
                continueAction(machine, farm, operableTiles, yearTick)
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
        tile: Tile,
        yearTick: Int
    ) {
        Logger.logFarmAction(machine.id, ActionType.MOWING, tile.id, machine.duration)
        machine.currentTile = tile
        machine.updateElapsedTime()
        val plant = tile.plant
        if (plant != null) {
            plant.actionsNeeded.remove(ActionType.MOWING)
            for (element in plant.mowingTime) {
                if (element.first.inRange(yearTick)) {
                    element.second = true
                }
            }
        }
    }

    /**
     * helper function for startPhase
     * @param machine the machine that will do the actions
     * @param farm the farm that owns the teils and machine
     * @param operableTiles the tiles that should be performed on
     */
    private fun continueAction(machine: Machine, farm: Farm, operableTiles: List<Tile>, yearTick: Int) {
        while (machine.canPerform()) {
            val accessibleTiles = simulationMap.getReachableTiles(machine, 2, false)
            for (tile in operableTiles) {
                val opTile = tile.id in farm.tileHashMap
                if (tile in accessibleTiles && !opTile) {
                    performAction(machine, tile, yearTick)
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
        tick: Int
    ): List<Tile> {
        return emptyList()
    }

    /**
     * not used for mowing handler
     */
    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine) {
        return
    }

    override fun performAction(machine: Machine, tile: Tile) {
        return
    }
}
