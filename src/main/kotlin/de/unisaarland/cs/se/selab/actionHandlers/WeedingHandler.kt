package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
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
     *
     * @param farm The farm containing the tiles and machines.
     * @param machine The machine to be used for weeding.
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
            if (tile.id in farm.tileHashMap) {
                continue // Skip if tile is already handled
            }

            val plantType = tile.currentCrop ?: continue // Don't really need to do this if operable tiles checks
            // Skip if no crop
            if (plantsThisMachineCanWorkOn.contains(plantType)) {
                if (simulationMap.isReachable(machine, tile)) {
                    performAction(machine, tile)
                    updateMachineMap(farm, machine)
                    break
                }
            }
            // Perform action with the given machine
        }
    }

    /**
     * Performs the WEED action on a tile using the given machine.
     * Updates the machine's state and removes the WEED action from the plant's required actions.
     *
     * @param machine The machine performing the action.
     * @param tile The tile on which the action is performed.
     */
    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
        machine.currentTile = tile
        machine.updateElapsedTime()
        val plant = tile.plant
        plant?.actionsNeeded?.remove(ActionType.WEED)
        continueAction(machine, tile)
    }

    /**
     * Continues the WEED action by finding neighboring tiles within a radius of 2
     * that also require the WEED action and are reachable by the machine.
     * If no more tiles are available, the machine returns to its home shed.
     *
     * @param machine The machine performing the action.
     * @param tile The current tile being processed.
     */
    private fun continueAction(machine: Machine, tile: Tile) {
        if (!machine.canPerform()) {
            machine.currentTile = machine.homeShed // Return to shed if time is up
            return
        }

        // Get neighboring tiles within radius 2
        val neighborTiles = this.simulationMap.getTilesByRadius(tile, 2)
            .filter { it.plant != null && it.plant!!.actionsNeeded.contains(ActionType.WEED) }
            .filter { simulationMap.isReachable(machine, it) }
            .sortedBy { it.id } // Sort by ID

        val nextTile = neighborTiles.firstOrNull()
        if (nextTile != null) {
            machine.currentTile = nextTile
            machine.updateElapsedTime()
            nextTile.plant?.actionsNeeded?.remove(ActionType.WEED)
            continueAction(machine, nextTile) // Recursively continue action
        } else {
            machine.currentTile = machine.homeShed
        }
    }

    /**
     * Returns all plantation tiles that have a plant requiring the WEED action, sorted by ID.
     *
     * @param farm The farm containing the plantation tiles.
     * @return A mutable list of tiles that require the WEED action.
     */
    override fun getOperableTiles(farm: Farm): MutableList<Tile> {
        val tiles = farm.getPlantation()
            .filter { it.plant != null && it.plant!!.actionsNeeded.contains(ActionType.WEED) }
            .sortedBy { it.id }
            .toMutableList()
        return tiles
    }

    /**
     * ALL THIS SHIT BELOW IS SO THAT IT BUILDS; WE NEED TO FIX STUFF
     *
     * Not implemented: Throws an error if called.
     */
    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType
    ): List<Tile> {
        error("getOperableTiles(farm, plant) is not implemented in WeedingHandler")
    }

    /**
     * Not implemented: Throws an error if called.
     */
    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        yearTick: Int
    ): List<Tile> {
        error("getOperableTiles(farm, plant, yearTick) is not implemented in WeedingHandler")
    }

    /**
     * Not implemented: Throws an error if called.
     */
    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        error("startPhase(farm, yearTick, simTick) is not implemented in WeedingHandler")
    }
}
