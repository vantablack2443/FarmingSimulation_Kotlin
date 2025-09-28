package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * SowingHandler class, handles the sowing action phase
 */
class SowingHandler(
    simulationMap: SimulationMap,
    plantData: PlantData
) : ActionHandler(
    simulationMap,
    plantData
) {

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        // Get all sowable plant types for the current tick in year, return from function if none
        val sowablePlantTypes: List<PlantType> = this.plantdata.getSowablePlantTypes(yearTick)
        if (sowablePlantTypes.isEmpty()) {
            return
        }

        // Get all sowing plans for the current tick in a year, ordered by tick then id
        // Assumes the ordering is handled by the function
        val sowingPlans: List<SowingPlan> = getSowingPlans(farm, sowablePlantTypes, simTick)
        // Log active sowing Plans
        Logger.logFarmSowingPlan(farm.getId(), sowingPlans.map { it.getId() })

        // Loop through all sowing plans and try to execute each
        val plansExecuted: MutableList<SowingPlan> = mutableListOf()
        for (plan in sowingPlans) {
            val planSuccess: Boolean = tryPlan(farm, plan, simTick, yearTick)
            // Add plan to executed list if successful
            if (planSuccess) {
                plansExecuted.add(plan)
            }
        }
        // Remove all successfully executed plans from the farm's sowing plans
        farm.removeSowingPlans(plansExecuted, simTick)
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        tick: Int
    ): List<Tile> {
        val operableTiles = mutableListOf<Tile>()
        for (tile in farm.getFields()) {
            // ???? WHY DOES IsSowable REQUIRE THE PLANT PARAMETER
            if (tile.currentCrop == null && tile.isSowable(plant, tick)) {
                operableTiles.add(tile)
            }
        }
        return operableTiles
    }

    private fun getSowingPlans(farm: Farm, sowablePlantTypes: List<PlantType>, simTick: Int): List<SowingPlan> {
        val sowingPlans = mutableListOf<SowingPlan>()
        val sowingPlansAvailable = farm.getSowingPlansByTick(simTick)
        // Returns empty list if there are no sowing plans available for the current simTick
        if (sowingPlansAvailable.isEmpty()) {
            return sowingPlans
        }

        // Assumes that the farm maintains sowing plans in order of tick then id
        // Assumes that farm also maintains ordering when removing sowing plans from its list
        for (plan in sowingPlansAvailable) {
            // If the plant type of the sowing plan is in the list of sowable plant types, add it to the result list
            if (sowablePlantTypes.contains(plan.getPlant())) {
                sowingPlans.add(plan)
            }
        }

        // The sowing plans should be executed in order of tick then id, for the given plantTypes
        return sowingPlans
    }

    private fun getTilesToSow(farm: Farm, plan: SowingPlan, simTick: Int): List<Tile> {
        // Gets all sowable tiles for the given plant type
        val plantableTilesFarm = getOperableTiles(farm, plan.getPlant(), simTick)

        // Get the intersection of the plantable tiles from the farm and the tiles in the plan
        val planTileID = plan.getSowingTiles().map { it.id }.toSet()
        val tilesToSow = plantableTilesFarm.filter { it.id in planTileID }

        // IF THESE ARE ORDERED, REMOVE THE SORTING PART
        // Sort tiles by id to ensure consistent order of sowing
        return tilesToSow.sortedBy { it.id }
    }

    private fun tryPlan(farm: Farm, plan: SowingPlan, simTick: Int, yearTick: Int): Boolean {
        // Get all tiles belonging to the farm that can be sown for the given plan
        val tilesToSow = getTilesToSow(farm, plan, simTick)

        // Return if there are no tiles to sow
        if (tilesToSow.isEmpty()) {
            return false
        }

        // Select machine, first by duration then id
        val machines = getAvailableMachines(farm, plan.getPlant(), ActionType.SOWING)
        // Return if there are no available machines
        if (machines.isEmpty()) {
            return false
        }

        // hasSown flag to check if at least one tile has been sown
        var hasSown = false

        for (tile in tilesToSow) {
            if (tile.id in farm.tileHashMap) {
                continue // Skip if tile is already handled
            }
            val machine = getNextMachine(machines, farm, tile)
            if (machine != null) {
                hasSown = true
                performAction(machine, tile, yearTick, plan)
                farm.tileHashMap.add(tile.id)
                continueAction(machine, tilesToSow, plan, farm, yearTick)
                farm.machineHashMap.add(machine.id)
                returnToShed(machine, farm)
            }
        }

        return hasSown
    }

    /**
     * Performs the SOWING action
     */
    private fun performAction(machine: Machine, tile: Tile, yearTick: Int, plan: SowingPlan) {
        // Update machine's location
        machine.updateElapsedTime()
        machine.currentTile = tile

        // Creates plant, sets tile's plant to the created plant, sets tile's current crop to the plant type
        val plant = Plant.createPlant(plan.getPlant())
        // Adds to SOWING to lateAction list if the tick of sowing is late
        plant.checkLateSowing(tile.lateActions, yearTick)
        tile.plant = plant
        tile.currentCrop = plan.getPlant()

        logAction(machine, tile, plan)
    }

    /**
     * Continues SOWING action
     */
    private fun continueAction(machine: Machine, tilesToSow: List<Tile>, plan: SowingPlan, farm: Farm, yearTick: Int) {
        if (!machine.canPerform()) {
            // Return to shed if time is up
            return
        }

        // Get neighboring tiles within radius 2 that could be sown
        // Checks reachability and if the tile hashmap
        val nextTile = this.simulationMap.tileForContinueAction(
            machine,
            tilesToSow,
            farm
        )
        if (nextTile != null) {
            farm.tileHashMap.add(nextTile.id)
            performAction(machine, nextTile, yearTick, plan)
            continueAction(machine, tilesToSow, plan, farm, yearTick) // Recursively continue action
        }
    }

    /**
     * Returns to shed
     */
    private fun returnToShed(machine: Machine, farm: Farm) {
        // Try to return machine to shed
        // Reset elapsed  time on machine
        machine.resetElapsedTime()

        val returnShed: Tile? = this.simulationMap.findTargetShed(
            machine,
            farm.getFarmstead().filter { it.shed == true }.sortedBy { it.id },
            machine.currentHarvest != null
        )

        // If no shed is reachable, the machine is stuck
        if (returnShed == null) {
            machine.isStuck = true
        } else {
            machine.currentTile = returnShed
            machine.homeShed = returnShed
        }

        logReturn(machine, returnShed)
    }

    private fun logAction(machine: Machine, tile: Tile, plan: SowingPlan) {
        // Logs action and sowing
        Logger.logFarmAction(machine.id, ActionType.SOWING, tile.id, machine.duration)
        Logger.logSowing(machine.id, plan.getPlant(), plan.getId())
    }

    private fun logReturn(machine: Machine, tile: Tile?) {
        if (tile == null) {
            Logger.logMachineReturnFail(machine.id)
        } else {
            Logger.logMachineFinish(machine.id, tile.id)
        }
    }

    override fun startPhase(
        farm: Farm,
        machine: Machine,
        yearTick: Int
    ) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine) {
        // TODO
        return
    }

    override fun performAction(
        machine: Machine,
        tile: Tile,
        yearTick: Int
    ) {
        return
    }

    override fun performAction(machine: Machine, tile: Tile) {
        // TODO
        return
    }

    override fun getOperableTiles(farm: Farm): List<Tile> {
        // TODO
        return emptyList()
    }
}
