package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * This class handles the irrigation action of the farming phase.
 */
class IrrigationHandler(
    simulationMap: SimulationMap,
    plantdata: PlantData
) : ActionHandler(
    simulationMap,
    plantdata
) {
    /*
    CHECK IF YOU CAN IRRIGATE PLANTATIONS RIGHT AFTER FIRST FIELD IRRIGATION
     */

    /**\
     * Handles the main logic of the irrigation phase, starting by getting operable tiles and then
     * checks for the target tile to perform actions and also for action continuation
     */
//    fun startPhase(farm: Farm, machine: Machine, tileType: TileType) {
//        if (!machine.actions.contains(ActionType.IRRIGATING)) {
//            return
//        }
//
//        val operableFieldTiles = getOperableTiles(farm, TileType.FIELD).toMutableList()
//        val operablePlantationTiles = getOperableTiles(farm, TileType.PLANTATION).toMutableList()
//
//        // here, operableTile will point to the same reference as operableFieldTiles or operablePlantationTiles
//        val operableTiles = if (tileType == TileType.FIELD) {
//            operableFieldTiles
//        } else {
//            operablePlantationTiles
//        }
//
//        if (operableTiles.isEmpty()) {
//            return
//        }
//
//        for (tile in operableTiles) {
//            val plantType = tile.currentCrop ?: continue
//            if (machine.plants.contains(plantType)) {
//                if (simulationMap.isReachable(machine, tile)) {
//                    // perform action on target tile
//                    performAction(farm, machine, tile)
//
//                    // remove tile from operableTiles
//                    operableTiles.remove(tile)
//
//                    // continue action
//                    helperFunction(tileType, machine, operableFieldTiles, operablePlantationTiles, farm)
//
//                    farm.machineHashMap.add(machine.id)
//                    machine.currentTile = machine.homeShed
//                    machine.resetElapsedTime()
//                    Logger.logMachineFinish(machine.id, machine.homeShed.id)
//                    break
//                }
//            }
//        }
//
//        // machine cannot perform anymor/        if (machine.currentTile != machine.homeShed) {
// //
// //        }
// // //        val returnShed: Tile? = simulationMap.findTargetShed(
// // //            machine,
// // //            farm.getFarmstead().filter { it.shed == true }.sortedBy { it.id },
// // //            false
// // //        )
// //
// //        if (returnShed == null) {
// //            machine.isStuck = true
// //        } else {
// //            machine.currentTile = returnShed
// //            machine.homeShed = returnShed
// //        }
//    }
//
//    private fun helperFunction(
//        tileType: TileType,
//        machine: Machine,
//        operableFieldTiles: MutableList<Tile>,
//        operablePlantationTiles: MutableList<Tile>,
//        farm: Farm
//    ) {
//        if (tileType == TileType.FIELD) {
//            continueActionField(machine, operableFieldTiles, operablePlantationTiles, farm)
//        } else {
//            continueActionPlantation(machine, operablePlantationTiles, farm)
//        }
//    }
//
//    /**
//     * Performs the irrigation action on the specified tile using the given machine.
//     */
    private fun performAction(farm: Farm, machine: Machine, tile: Tile) {
        Logger.logFarmAction(machine.id, ActionType.IRRIGATING, tile.id, machine.duration)

        machine.currentTile = tile
        machine.updateElapsedTime()

        val currentMoisture = tile.currentMoisture ?: error("Current moisture null or invalid")
        val maxMoisture = tile.maxMoisture ?: error("Max moisture null or invalid")
        val amount = maxMoisture - currentMoisture
        tile.increaseMoistureByAmount(amount)

        tile.actionsNeeded.remove(ActionType.IRRIGATING)
        farm.tileHashMap.add(tile.id)
    }
//
//    /*
//    /**
//     * Finds the target tile with the lowest ID that is reachable by the machine from the list of operable tiles.
//     */
//    private fun findTargetTile(m: Machine, operableTiles: List<Tile>): Tile? {
//        val reachableTiles = operableTiles.filter { tile -> simulationMap.isReachable(m, tile) }
//        if (reachableTiles.isEmpty()) return null
//        return reachableTiles.minBy { it.id }
//    }
//     */
//
    /**
     * Returns a list of operable tiles that need irrigation and are not already handled in the current tick.
     */
    private fun getOperableTiles(farm: Farm, tileType: TileType): List<Tile> {
        var tiles = emptyList<Tile>()

        if (tileType == TileType.FIELD) {
            tiles = farm.getFields()
        }
        if (tileType == TileType.PLANTATION) {
            tiles = farm.getPlantation()
        }

        val operableTiles = tiles
            .filter { it.id !in farm.tileHashMap }
            .filter { it.plant != null && it.actionsNeeded.contains(ActionType.IRRIGATING) }
            .sortedBy { it.id }
        return operableTiles
    }
//
// //    /**
// //     * Finds the next tile to continue the irrigation action within a radius of 2 from the machine's current tile.
// //     */
// //    private fun continueAction(
// //        machine: Machine,
// //        operableFieldTiles: MutableList<Tile>,
// //        operablePlantationTiles: MutableList<Tile>,
// //        farm: Farm
// //    ) {
// //        if (!machine.canPerform()) {
// //            return
// //        }
// //
// //        // carrying harvest will always be false as same machine cant harvest and irrigate in same tick
// //        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
// // //        val merged = operableFieldTiles.sortedBy { it.id } + operablePlantationTiles.sortedBy { it.id }
// // //        val operable = merged.filter { it in reach }.toMutableList()
// // //        if (operable.isEmpty()) return
// //        val fieldTiles = operableFieldTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
// //        if (fieldTiles.isNotEmpty()) {
// //            performAction(farm, machine, fieldTiles.first())
// //            operableFieldTiles.remove(fieldTiles.first())
// //            continueAction(machine, operableFieldTiles, operablePlantationTiles, farm)
// //        }
// //
// //        if (!machine.canPerform()) {
// //            return
// //        }
// //
// //        val plantationTiles = operablePlantationTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
// //        if (plantationTiles.isNotEmpty()) {
// //            performAction(farm, machine, plantationTiles.first())
// //            operablePlantationTiles.remove(plantationTiles.first())
// //            continueAction(machine, operableFieldTiles, operablePlantationTiles, farm)
// //        }
// //    }

    /**\
     * Handles the main logic of the irrigation phase, starting by getting operable tiles and then
     * checks for the target tile to perform actions and also for action continuation
     */
    fun startPhase(farm: Farm, machine: Machine, tileType: TileType) {
        val operableTiles = getOperableTiles(farm, tileType)
        if (operableTiles.isEmpty()) {
            return
        }
        for (tile in operableTiles) {
            val plantType = tile.currentCrop ?: continue
            if (machine.plants.contains(plantType) && simulationMap.isReachable(machine, tile)) {
                // perform action on target tile
                performAction(farm, machine, tile)

                // continue action
                if (tileType == TileType.FIELD) {
                    continueOperationField(
                        machine,
                        (operableTiles.toMutableList() - tile).toMutableList(),
                        farm
                    )
                } else {
                    continueOperationPlantation(
                        machine,
                        (operableTiles.toMutableList() - tile).toMutableList(),
                        farm
                    )
                }

                farm.machineHashMap.add(machine.id)
                machine.currentTile = machine.homeShed
                machine.resetElapsedTime()
                Logger.logMachineFinish(machine.id, machine.homeShed.id)
                break
            }
        }
    }

    private fun continueOperationField(machine: Machine, operableTiles: MutableList<Tile>, farm: Farm) {
        if (!machine.canPerform()) {
            return
        }
        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
            .filter { it.id !in farm.tileHashMap }
        val fieldTiles = operableTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
        if (fieldTiles.isNotEmpty()) {
            performAction(farm, machine, fieldTiles.first())
            continueOperationField(machine, (fieldTiles - fieldTiles.first()).toMutableList(), farm)
        }

        if (!machine.canPerform()) {
            return
        }
        val newreach = this.simulationMap.getReachableTiles(machine, 2, false)
            .filter { it.id !in farm.tileHashMap }
        val plantationTiles = getOperableTiles(farm, TileType.PLANTATION).intersect(newreach.toSet())
            .sortedBy { it.id }.toMutableList()
        if (plantationTiles.isNotEmpty()) {
            performAction(farm, machine, plantationTiles.first())
            continueOperationField(machine, operableTiles, farm)
        }
    }

    private fun continueOperationPlantation(machine: Machine, operableTiles: MutableList<Tile>, farm: Farm) {
        if (!machine.canPerform()) {
            return
        }
        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
            .filter { it.id !in farm.tileHashMap }
        val plantationTiles = operableTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
        if (plantationTiles.isNotEmpty()) {
            performAction(farm, machine, plantationTiles.first())
            continueOperationPlantation(machine, (plantationTiles - plantationTiles.first()).toMutableList(), farm)
        }
    }

    /**
     * Finds the next tile to continue the irrigation action within a radius of 2 from the machine's current tile.
     */
//    private fun continueActionField(
//        machine: Machine,
//        operableFieldTiles: MutableList<Tile>,
//        operablePlantationTiles: MutableList<Tile>,
//        farm: Farm
//    ) {
//        if (!machine.canPerform()) {
//            return
//        }
//
//        // carrying harvest will always be false as same machine cant harvest and irrigate in same tick
//        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
//        val fieldTiles = operableFieldTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
//        if (fieldTiles.isNotEmpty()) {
//            performAction(farm, machine, fieldTiles.first())
//            operableFieldTiles.remove(fieldTiles.first())
//            continueActionField(machine, operableFieldTiles, operablePlantationTiles, farm)
//        }
//        val plantationTiles = operablePlantationTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
//        if (plantationTiles.isNotEmpty()) {
//            performAction(farm, machine, plantationTiles.first())
//            operablePlantationTiles.remove(plantationTiles.first())
//            continueActionPlantation(machine, operablePlantationTiles, farm)
//        }
//    }

//    /**
//     * Finds the next tile to continue the irrigation action within a radius of 2 from the machine's current tile.
//     */
//    private fun continueActionPlantation(
//        machine: Machine,
//        operablePlantationTiles: MutableList<Tile>,
//        farm: Farm
//    ) {
//        if (!machine.canPerform()) {
//            return
//        }
//
//        // carrying harvest will always be false as same machine cant harvest and irrigate in same tick
//        val reach = this.simulationMap.getReachableTiles(machine, 2, false)
//
//        if (!machine.canPerform()) {
//            return
//        }
//
//        val plantationTiles = operablePlantationTiles.intersect(reach.toSet()).sortedBy { it.id }.toMutableList()
//        if (plantationTiles.isNotEmpty()) {
//            performAction(farm, machine, plantationTiles.first())
//            operablePlantationTiles.remove(plantationTiles.first())
//            continueActionPlantation(machine, operablePlantationTiles, farm)
//        }
//    }

    /**
     * These functions aren't implemented inside IrrigationHandler, probably declare them as open in
     * ActionHandler and then override them in the classes that need them.
     */

    override fun performAction(
        machine: Machine,
        tile: Tile,
        yearTick: Int
    ) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine, yearTick: Int) {
        return
    }

    override fun performAction(machine: Machine, tile: Tile) {
        return
    }

    override fun getOperableTiles(farm: Farm, plant: PlantType, tick: Int): List<Tile> {
        return emptyList()
    }

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        return
    }

    override fun startPhase(farm: Farm, machine: Machine) {
        return
    }

    override fun getOperableTiles(farm: Farm): List<Tile> {
        return emptyList()
    }
}
