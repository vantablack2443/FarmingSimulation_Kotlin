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

// ISSUES WITH ADDING INTO HASHMAP; FARM NEEDS TO BE PASSED IN CONTINUE action
/**
 * Handles the cutting phase of the simulation, where machines are assigned to tiles
 * to perform the CUT action on plants that require it.
 *
 * @param simulationMap The simulation map used to determine tile relationships and reachability.
 * @param plantdata The plant data used for accessing plant-related information.
 */
class CuttingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    /**
     * Starts the cutting phase by identifying operable tiles and assigning machines
     * to perform the CUT action on plants.
     *
     * @param farm The farm containing the tiles and machines.
     * @param yearTick The current year tick of the simulation.
     * @param simTick The current simulation tick.
     */
    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        // Get all plantation tiles that have a plant needing CUTTING
        val operableTiles = getOperableTiles(farm, ActionType.CUTTING)
        this.operableTiles = operableTiles

        if (operableTiles.isEmpty()) {
            return
        }

        // For each operable tile, try to assign a machine and perform the action
        for (tile in operableTiles) {
            if (tile.id in farm.tileHashMap) {
                continue // Skip if tile is already handled
            }
            val plantType = tile.currentCrop ?: continue // Skip if no crop
            val availableMachines = getAvailableMachines(farm, plantType, ActionType.CUTTING, simTick)
            val machine = getNextMachine(availableMachines, farm, tile)
            if (machine != null) {
                performAction(machine, tile, yearTick)
                farm.tileHashMap.add(tile.id)
                continueAction(machine, farm, operableTiles, yearTick)
                farm.machineHashMap.add(machine.id)
            }
        }
    }

    /**
     * Performs the CUT action on a tile using the given machine.
     * Updates the machine's state and removes the CUT action from the plant's required actions.
     *
     * @param machine The machine performing the action.
     * @param tile The tile on which the action is performed.
     */
    override fun performAction(
        machine: Machine,
        tile: Tile,
        yearTick: Int
    ) {
        machine.currentTile = tile
        machine.updateElapsedTime()

        val plant = tile.plant
        tile.actionsNeeded.remove(ActionType.CUTTING)
        if (plant != null) {
            for (element in plant.cuttingTime) {
                if (element.first.inRange(yearTick)) {
                    element.second = true
                }
            }
        }

        // Log the action
        logFarmAction(machine.id, ActionType.CUTTING, tile.id, machine.duration)
    }

    override fun getOperableTiles(farm: Farm): List<Tile> {
        return emptyList()
    }

    /**
     * Continues the CUT action by finding neighboring tiles within a radius of 2
     * that also require the CUT action and are reachable by the machine.
     * If no more tiles are available, the machine returns to its home shed.
     *
     * @param machine The machine performing the action.
     */
    private fun continueAction(
        machine: Machine,
        farm: Farm,
        operableTiles: MutableList<Tile>,
        yearTick: Int
    ) {
        if (!machine.canPerform()) {
            machine.currentTile = machine.homeShed // Return to shed if time is up
            logMachineFinish(machine.id, machine.homeShed.id)
            machine.resetElapsedTime()
            return
        }

        val nextTile = this.simulationMap.tileForContinueAction(machine, operableTiles, farm)

        if (nextTile != null) {
            farm.tileHashMap.add(nextTile.id)
            performAction(machine, nextTile, yearTick)
            continueAction(machine, farm, operableTiles, yearTick) // Recursively continue action
        } else {
            machine.currentTile = machine.homeShed
            machine.resetElapsedTime()
            logMachineFinish(machine.id, machine.homeShed.id)
        }
    }

    /**
     *THINGS BELOW SO THAT THIS BUILDS
     */

    // Implement missing abstract methods from ActionHandler
    override fun startPhase(farm: Farm, machine: Machine) {
        // Provide a minimal implementation or throw if not used
        return
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        tick: Int
    ): List<Tile> {
        return emptyList()
    }

    override fun performAction(machine: Machine, tile: Tile) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine, yearTick: Int) {
        return
    }
}
