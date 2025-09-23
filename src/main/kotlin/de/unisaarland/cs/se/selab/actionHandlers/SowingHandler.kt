package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
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

        // Loop through all sowing plans and try to execute each
        val plansExecuted: MutableList<SowingPlan> = mutableListOf()
        for (plan in sowingPlans) {
            val planSuccess: Boolean = tryPlan(farm, plan, simTick)
            // Add plan to executed list if successful
            if (planSuccess) {
                plansExecuted.add(plan)
            }
        }
        // Remove all successfully executed plans from the farm's sowing plans
        farm.removeSowingPlans(plansExecuted)

        this.clearSets(farm)
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        tick: Int
    ): List<Tile> {
        val operableTiles = mutableListOf<Tile>()
        for (tile in farm.getFields()) {
            // ???? WHY DOES IsSowable REQUIRE THE PLANT PARAMETER
            if (tile.currentCrop == plant && tile.isSowable(plant, tick)) {
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

    private fun tryPlan(farm: Farm, plan: SowingPlan, simTick: Int): Boolean {
        // Get all tiles belonging to the farm that can be sown for the given plan
        val tilesToSow = getTilesToSow(farm, plan, simTick)

        // Return if there are no tiles to sow
        if (tilesToSow.isEmpty()) {
            return false
        }

        // Select machine, first by duration then id
        val machines = getAvailableMachines(farm, plan.getPlant())

        // Return if there are no available machines
        if (machines.isEmpty()) {
            return false
        }

        // hasSown flag to check if at least one tile has been sown
        var hasSown = false

        // Try to sow with each machine until the plan is completed or no machines are left
        // Tile comes first then machine
        var nextTile: Tile? = getNextTile(tilesToSow, farm)
        while (nextTile != null) {
            val nextMachine = getNextMachine(machines, farm, nextTile)
            // Sow the next tile with the next available machine, if no machine is available, get next tile
            // Continue action if possible, else return to shed and get next tile
            if (nextMachine == null) {
                nextTile = getNextTile(tilesToSow, farm)
                continue
                // There could be the case that none of the machines can reach the tile, so we get the next tile
                // In case none of the tiles can be reached, the plan has failed so the flag will remain false
            }

            // If this part of the code is reached a machine and tile are available for sowing
            //
            hasSown = true

            // Sow the tile with the machine
            // This tile has already been added to the hashmap in getNextTile

            // Update machine's location
            nextMachine.currentTile = nextTile

            // Creates plant, sets tile's plant to the created plant, sets tile's current crop to the plant type
            val plant = Plant.createPlant(plan.getPlant())
            nextTile.plant = plant
            nextTile.currentCrop = plan.getPlant()

            // Continue action with machine while adding tiles sowed to the hashmap
            var continueTile: Tile? = this.simulationMap.tileForContinueAction(
                nextMachine,
                tilesToSow
            )
            while (nextMachine.canPerform() && continueTile != null) {
                // Update machine's elapsed time
                nextMachine.updateElapsedTime()

                if (tilesToSow.any { it.id == continueTile.id }) {
                    // Updates machine's location
                    nextMachine.currentTile = continueTile

                    // Creates plant, sets tile's plant to the created plant, sets tile's current crop to the plant type
                    val plantContinue = Plant.createPlant(plan.getPlant())
                    continueTile.plant = plantContinue
                    continueTile.currentCrop = plan.getPlant()

                    // Add tile to the hashmap to avoid re-sowing
                    farm.tileHashMap.add(continueTile.id)
                }

                // Get the next tile for continue action
                continueTile = this.simulationMap.tileForContinueAction(
                    nextMachine,
                    tilesToSow
                )
            }

            // Try to return machine to shed
            // Reset elapsed  time on machine
            nextMachine.resetElapsedTime()

            val returnShed: Tile? = this.simulationMap.findTargetShed(
                nextMachine,
                farm.getFarmstead().filter { it.shed == true },
                nextMachine.currentHarvest != null
            )

            // If no shed is reachable, the machine is stuck
            if (returnShed == null) {
                nextMachine.isStuck = true
            } else {
                nextMachine.currentTile = returnShed
                nextMachine.homeShed = returnShed
            }

            nextTile = getNextTile(tilesToSow, farm)
        }

        return hasSown
    }

    /**
     * Will return the next tile that can be sown from the list of tiles to sow
     */
    private fun getNextTile(tilesToSow: List<Tile>, farm: Farm): Tile? {
        for (tile in tilesToSow) {
            if (!farm.tileHashMap.contains(tile.id)) {
                farm.tileHashMap.add(tile.id)
                return tile
            }
        }
        return null
    }

    /**
     * Returns the next machine that can sow the given tile from the list of machines that can sow the given plant
     * type. Returns null if no machine is available for the given tile.
     * Will check for machine availability in the farm's machineHashMap and reachability
     * Requires that the machines list is sorted by duration then id
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
     * Gets all available machines that can sow for the given plant type and returns them sorted by duration then id
     * Returns the machines that are not stuck(in shed), can plant the given plant type and have the sow action
     */
    private fun getAvailableMachines(farm: Farm, plantType: PlantType): List<Machine> {
        val machines = mutableListOf<Machine>()
        for (machine in farm.getMachines()) {
            if (!machine.isStuck && machine.plants.contains(plantType) && machine.actions.contains(ActionType.SOW)) {
                machines.add(machine)
            }
        }

        // REMOVE THE SORTING PART IF THE MACHINE LIST IS MAINTAINED IN ORDER
        return machines.sortedWith(compareBy({ it.duration }, { it.id }))
    }

    override fun startPhase(farm: Farm, machine: Machine) {
        // TODO
        return
    }

    override fun startPhase(
        farm: Farm,
        machine: Machine,
        yearTick: Int
    ) {
        return
    }

    override fun performAction(machine: Machine, tile: Tile) {
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

    override fun getOperableTiles(farm: Farm): List<Tile> {
        // TODO
        return emptyList()
    }
}
