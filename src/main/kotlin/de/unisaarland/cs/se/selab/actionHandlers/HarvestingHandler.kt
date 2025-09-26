package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.machine.PlantAndHarvest
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

const val FALLOW_DURATION = 4

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
        for (tile in operableTiles) {
            val availableMachines = getAvailableMachines(farm, tile.currentCrop!!, ActionType.HARVESTING)
            val machine = getNextMachine(availableMachines, farm, tile)
            if (machine != null) {
                doHarvest(farm, machine, tile, yearTick)
                continueAction(farm, machine, yearTick, tile, operableTiles)
                /* missing machine return */
                farm.machineHashMap.add(machine.id)
                machine.resetElapsedTime()
            }
//            this.simulationMap.findTargetShed(availableMachine, farm.getShedTiles(), true)
//            farm.machineHashMap.remove(availableMachine.id)
        }
    }

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

    private fun continueAction(
        farm: Farm,
        machine: Machine,
        yearTick: Int,
        tileToBaseOn: Tile,
        operableTiles: MutableList<Tile>
    ) {
        if (!machine.canPerform()) {
            return
        }
        val toContinueOn = this.simulationMap.getReachableTiles(
            machine,
            2,
            true
        ).filter { it.currentCrop == tileToBaseOn.currentCrop }
        if (toContinueOn.isEmpty()) {
            return
        }
        for (tile in toContinueOn) {
            if (this.simulationMap.isReachable(machine, tile)) {
                doHarvest(farm, machine, tile, yearTick)
                operableTiles.remove(tile)
                continueAction(farm, machine, yearTick, tile, operableTiles)
            }
        }
    }

    private fun doHarvest(farm: Farm, machine: Machine, tile: Tile, yearTick: Int) {
        // logger.logFarmAction(machine.id, ActionType.HARVESTING, tile.id)
        machine.currentTile = tile
        machine.updateElapsedTime()
        val currentCrop = tile.currentCrop
        val plant = tile.plant
        if (currentCrop != null && plant != null) {
            /* do Harvest needs to be fixed since it overwrites the old harves in the machine */
            machine.currentHarvest = PlantAndHarvest(currentCrop, plant.harvestEstimate)
            plant.harvestEstimate = 0
        }
        if (tile.category == TileType.FIELD) {
            tile.plant = null
        }
        farm.tileHashMap.add(tile.id)
        // not a normal setter because it takes the yearTick and base off the duration on that
        tile.fallowDuration = Duration(yearTick, yearTick + FALLOW_DURATION)
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
}
