package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.machine.PlantAndHarvest
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

// const val FALLOW_DURATION = 4

/**
 * handler for performing HARVESTING
 */
class HarvestingHandler(
    simulationMap: SimulationMap,
    plantdata: PlantData
) : ActionHandler(simulationMap, plantdata) {

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        val harvestablePlantTypes: List<PlantType> = this.plantdata.getHarvestablePlantTypes(yearTick)
        if (harvestablePlantTypes.isEmpty()) {
            return
        }
        // using the operableTiles from the ActionHandler
        val operableTiles = getOperableTiles(farm, ActionType.HARVESTING)
        // get OperableTiles takes care of the prioritization of the tiles-- changed the signature in the diagram
        // val operableTiles = getOperableTiles(farm, harvestablePlantTypes)
        for (tile in operableTiles.toList()) {
            val currentCrop = tile.currentCrop ?: continue

            val availableMachines = getAvailableMachines(farm, currentCrop, ActionType.HARVESTING)
            if (availableMachines.isEmpty()) {
                continue
            }

            val machine = getNextMachine(availableMachines, farm, tile)

            if (machine != null) {
                doHarvest(farm, machine, tile)
                operableTiles.remove(tile)

                continueAction(farm, machine, simTick, tile, operableTiles)
                /* missing machine return */
                /* farm.machineHashMap.add(machine.id) not needed since getnextmachine alr adds it */
                machine.resetElapsedTime()

                val returnShed: Tile? = this.simulationMap.findTargetShed(
                    machine,
                    farm.getFarmstead().filter { it.shed == true }.sortedBy { it.id },
                    machine.currentHarvest != null
                )

                // If no shed is reachable, the machine is stuck
                if (returnShed == null) {
                    machine.isStuck = true
                    Logger.logMachineReturnFail(machine.id)
                } else {
                    machine.currentTile = returnShed
                    machine.homeShed = returnShed
                    val amount = machine.currentHarvest?.harvestAmount ?: 0
                    val plantType = machine.currentHarvest?.plant ?: throw IllegalArgumentException("Plant is null")
                    Logger.logMachineFinish(machine.id, returnShed.id)
                    Logger.logUnload(machine.id, amount, plantType)
                }

                if (!machine.isStuck) {
                    farm.addHarvestPerPlant(
                        machine.currentHarvest?.plant ?: error("Plant invalid"),
                        machine.currentHarvest?.harvestAmount ?: 0
                    )
                    machine.currentHarvest = null
                }
            }
        }
    }

    private fun continueAction(
        farm: Farm,
        machine: Machine,
        simTick: Int,
        tileToBaseOn: Tile,
        operableTiles: MutableList<Tile>
    ) {
        if (!machine.canPerform()) {
            return
        }
        // we pass a filtered list of operable tiles only containing the tiles of the current crop
        val continueTile = this.simulationMap.tileForContinueAction(
            machine,
            operableTiles.filter { it.currentCrop == tileToBaseOn.currentCrop },
            farm
        )

        /*val toContinueOn = this.simulationMap.getReachableTiles(
            machine,
            2,
            true
        ).filter {
            it.currentCrop == tileToBaseOn.currentCrop && !farm.tileHashMap.contains(it.id)
        }*/

        if (continueTile == null) {
            return
        }

        doHarvest(farm, machine, continueTile)
        operableTiles.remove(continueTile)

        continueAction(farm, machine, simTick, continueTile, operableTiles)

        /*for (tile in toContinueOn) {
            if (this.simulationMap.isReachable(machine, tile)) {
                doHarvest(farm, machine, tile, simTick)
                operableTiles.remove(tile)

                continueAction(farm, machine, simTick, tile, operableTiles)
            }
        }*/
    }

    private fun doHarvest(farm: Farm, machine: Machine, tile: Tile) {
        // log farm action
        Logger.logFarmAction(machine.id, ActionType.HARVESTING, tile.id, machine.duration)

        machine.currentTile = tile
        machine.updateElapsedTime()

        val currentCrop = tile.currentCrop
        val plant = tile.plant
        if (currentCrop != null && plant != null) {
            if (machine.currentHarvest != null) {
                if (machine.currentHarvest?.plant == currentCrop) {
                    machine.currentHarvest?.harvestAmount += plant.harvestEstimate
                } else {
                    error("Machine is already carrying a different plant")
                }
            } else {
                machine.currentHarvest = PlantAndHarvest(currentCrop, plant.harvestEstimate)
            }
            Logger.logHarvest(machine.id, plant.harvestEstimate, currentCrop)

            plant.harvestEstimate = 0
            // Estimator handles killing the plants if fields and fallowing using this bool
            tile.harvestedThisTick = true
        }
        /*if (tile.category == TileType.FIELD) {
            // Handled by estimator - // Estimator handles killing the plants if fields and fallowing
            // tile.plant = null
            // tile.fallowDuration = Duration(simTick + 1, simTick + FALLOW_DURATION)
        }*/

        tile.actionsNeeded.remove(ActionType.HARVESTING)
        // Clears actionsNeedded and lateActions lists. Missed actions shouldn't be logged after harvesting
        tile.actionsNeeded.clear()
        tile.lateActions.clear()
        farm.tileHashMap.add(tile.id)
        // not a normal setter because it takes the yearTick and base off the duration on that
    }

//    private fun getAvailableMachine(farm: Farm, tile: Tile): Machine? {
//        val machines = mutableListOf<Machine>()
//        for (machine in farm.getMachines()) {
//            if (!machine.isStuck &&
//                machine.plants.contains(tile.currentCrop) && machine.actions.contains(ActionType.HARVESTING)
//            ) {
//                if (this.simulationMap.isReachable(machine, tile)) {
//                    machines.add(machine)
//                }
//            }
//        }
//
//        val sortedMachines = machines.sortedWith(compareBy({ it.duration }, { it.id }))
//        return sortedMachines.firstOrNull()
//    }
//
//    private fun getOperableTiles(farm: Farm, harvestablePlantTiles: List<PlantType>): MutableList<Tile> {
//        val plantationTiles = farm.getPlantation().filter { tile -> harvestablePlantTiles.contains(tile.currentCrop) }
//        val fieldTiles = farm.getFields().filter { tile -> harvestablePlantTiles.contains(tile.currentCrop) }
//        return (plantationTiles.sortedBy { it.id } + fieldTiles.sortedBy { it.id }).toMutableList()
//    }

    override fun startPhase(
        farm: Farm,
        machine: Machine
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

    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
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
        return emptyList()
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        tick: Int
    ): List<Tile> {
        return emptyList()
    }
}
