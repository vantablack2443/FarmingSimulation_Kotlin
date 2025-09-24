package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger.logFarmAction
import de.unisaarland.cs.se.selab.log.Logger.logMachineFinish
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Handles the weeding phase of the simulation, assigning machines to tiles
 * to perform the WEED action on plants that require it.
 *
 * @param simulationMap The simulation map used to determine tile relationships and reachability.
 * @param plantdata The plant data used for accessing plant-related information.
 */
class WeedingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    /**
     * Starts the weeding phase for a specific machine, assigning it to operable tiles.
     * Machine passed should not be in the MachineMap
     */
    override fun startPhase(
        farm: Farm,
        machine: Machine
    ) {
        val plantsThisMachineCanWorkOn: List<PlantType> = machine.plants

        val operableTiles = getOperableTiles(farm)
        if (operableTiles.isEmpty()) {
            return
        }
        for (tile in operableTiles) {
            val plantType = tile.currentCrop
            if (tile.id in farm.tileHashMap || plantType == null) {
                continue // Skip if tile is already handled
            }
            if (plantsThisMachineCanWorkOn.contains(plantType)) {
                if (simulationMap.isReachable(machine, tile)) {
                    performAction(machine, tile)
                    continueAction(machine, tile, farm, operableTiles)
                    farm.machineHashMap.add(machine.id)
                    break
                }
            }
            // Perform action with the given machine
        }
        logMachineFinish(machine.farmID, machine.id)
    }

    /**
     * Performs the WEED action on a tile using the given machine.
     * Updates the machine's state and removes the WEED action from the plant's required actions.
     *
     */
    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
        machine.currentTile = tile
        machine.updateElapsedTime()
        val plant = tile.plant
        plant?.actionsNeeded?.remove(ActionType.WEEDING)

        // Log the action
        logFarmAction(machine.farmID, ActionType.WEEDING, tile.id, machine.duration)
    }

    /**
     * Continues the WEED action by finding neighboring tiles within a radius of 2
     * that also require the WEED action and are reachable by the machine.
     * If no more tiles are available, the machine returns to its home shed.
     *
     */
    private fun continueAction(machine: Machine, tile: Tile, farm: Farm, operableTiles: MutableList<Tile>) {
        if (machine.canPerform()) {
            // Get neighboring tiles within radius 2
            val tilesInRadius = this.simulationMap.getTilesByRadius(tile, 2)
            val neighborTiles = tilesInRadius
                .filter { it in operableTiles }
                .filter { simulationMap.isReachable(machine, it) }
                .sortedBy { it.id } // Sort by ID

            val nextTile = neighborTiles.firstOrNull()
            if (nextTile != null) {
                performAction(machine, nextTile)
                continueAction(machine, nextTile, farm, operableTiles) // Recursively continue action
            } else {
                machine.currentTile = machine.homeShed
                machine.resetElapsedTime()
            }
            return
        }
        machine.currentTile = machine.homeShed // Return to shed if time is up
        machine.resetElapsedTime()
    }

    /**
     * Returns all plantation tiles that have a plant requiring the WEED action, sorted by ID.
     *
     * @param farm The farm containing the plantation tiles.
     * @return A mutable list of tiles that require the WEED action.
     */
    override fun getOperableTiles(farm: Farm): MutableList<Tile> {
        val tiles = farm.getPlantation()
            .filter { it.plant != null && it.plant?.actionsNeeded?.contains(ActionType.WEEDING) ?: false }
            .sortedBy { it.id }
            .toMutableList()
        return tiles
    }

    /**
     * ALL THINGS BELOW IS SO THAT IT BUILDS;
     *
     * Not implemented: Throws an error if called.
     */

    /**
     * Not implemented: Throws an error if called.
     */
    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        tick: Int
    ): List<Tile> {
        return emptyList()
    }

    /**
     * Not implemented: Throws an error if called.
     */
    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        return
    }

    override fun performAction(
        machine: Machine,
        tile: Tile,
        yearTick: Int
    ) {
        return
    }

    override fun startPhase(
        farm: Farm,
        machine: Machine,
        yearTick: Int
    ) {
        return
    }
}
