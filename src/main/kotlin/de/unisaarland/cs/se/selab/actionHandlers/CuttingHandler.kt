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
        val operableTiles = getOperableTiles(farm)
        this.operableTiles = operableTiles

        if (operableTiles.isEmpty()) {
            return
        }
        this.tileMap = farm.tileHashMap

        // For each operable tile, try to assign a machine and perform the action
        for (tile in operableTiles) {
            if (tile.id in farm.tileHashMap) {
                continue // Skip if tile is already handled
            }
            val plantType = tile.currentCrop ?: continue // Skip if no crop
            val availableMachines = getAvailableMachines(farm, plantType)
            val machine = getNextMachine(availableMachines, farm, tile)
            if (machine != null) {
                performAction(machine, tile, yearTick)
                farm.tileHashMap.add(tile.id)
                this.tileMap.add(tile.id)
                continueAction(machine, tile, farm, operableTiles, yearTick)
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
        plant?.actionsNeeded?.remove(ActionType.CUT)
        if (plant != null) {
            for (element in plant.cuttingTime) {
                if (element.first.inRange(yearTick)) {
                    element.second = true
                }
            }
        }

        // Log the action
        logFarmAction(machine.farmID, ActionType.CUT, tile.id, machine.duration)
    }

    /**
     * Continues the CUT action by finding neighboring tiles within a radius of 2
     * that also require the CUT action and are reachable by the machine.
     * If no more tiles are available, the machine returns to its home shed.
     *
     * @param machine The machine performing the action.
     * @param tile The current tile being processed.
     */
    private fun continueAction(
        machine: Machine,
        tile: Tile,
        farm: Farm,
        operableTiles: MutableList<Tile>,
        yearTick: Int
    ) {
        if (!machine.canPerform()) {
            machine.currentTile = machine.homeShed // Return to shed if time is up
            return
        }

        // Get neighboring tiles within radius 2 that are also in operableTiles and reachable
        val tilesInRadius = this.simulationMap.getTilesByRadius(tile, 2)
        val neighborTiles = tilesInRadius
            .filter { it in operableTiles }
            .filter { simulationMap.isReachable(machine, it) }
            .sortedBy { it.id } // Sort by ID

        val nextTile = neighborTiles.firstOrNull()
        if (nextTile != null) {
            farm.tileHashMap.add(nextTile.id)
            this.tileMap.add(nextTile.id)
            performAction(machine, nextTile, yearTick)
            continueAction(machine, nextTile, farm, operableTiles, yearTick) // Recursively continue action
        } else {
            machine.currentTile = machine.homeShed
            machine.resetElapsedTime()
            logMachineFinish(machine.farmID, machine.id)
        }
    }

    /**
     * Retrieves all plantation tiles that have a plant requiring the CUT action.
     * The tiles are sorted by their ID.
     *
     * @param farm The farm containing the plantation tiles.
     * @return A mutable list of tiles that require the CUT action.
     */
    override fun getOperableTiles(farm: Farm): MutableList<Tile> {
        val tiles = farm.getPlantation()
            .filter { it.plant != null && it.plant?.actionsNeeded?.contains(ActionType.CUT) ?: false }
            .sortedBy { it.id }
            .toMutableList()
        return tiles
    }

    /**
     * Retrieves a list of available machines that can perform the CUT action
     * on the given plant type. The machines are sorted by duration and ID.
     *
     * @param farm The farm containing the machines.
     * @param plantType The type of plant to be cut.
     * @return A list of machines that can perform the CUT action.
     */
    private fun getAvailableMachines(farm: Farm, plantType: PlantType): List<Machine> {
        return farm.getMachines().filter {
            !it.isStuck && it.plants.contains(plantType) && it.actions.contains(ActionType.CUT)
        }.sortedWith(compareBy({ it.duration }, { it.id }))
    }

    /**
     * Retrieves the next available machine that can reach the destination tile.
     * The machine is marked as in use by adding its ID to the farm's machineHashMap.
     *
     * @param machines The list of machines to choose from.
     * @param farm The farm containing the machineHashMap.
     * @param destination The tile the machine needs to reach.
     * @return The next available machine, or null if none are available.
     */
    private fun getNextMachine(machines: List<Machine>, farm: Farm, destination: Tile): Machine? {
        for (machine in machines) {
            if (!farm.machineHashMap.contains(machine.id) && this.simulationMap.isReachable(machine, destination)) {
                farm.machineHashMap.add(machine.id)
                return machine
            }
        }
        return null
    }

    /**
     *THINGS BELOW SO THAT THIS SHIT BUILDS
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
