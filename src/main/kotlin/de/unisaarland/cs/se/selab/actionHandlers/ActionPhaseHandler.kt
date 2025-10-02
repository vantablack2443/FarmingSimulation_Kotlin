package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.plantdata.PlantData

/**
 * Handles the action phase of the simulation
 */
class ActionPhaseHandler(private val farms: List<Farm>) {
    private lateinit var sowingHandler: SowingHandler
    private lateinit var harvestingHandler: HarvestingHandler
    private lateinit var weedingHandler: WeedingHandler
    private lateinit var cuttingHandler: CuttingHandler
    private lateinit var mowingHandler: MowingHandler
    private lateinit var irrigationHandler: IrrigationHandler
    private val plantData = PlantData()

    /**
     * Gets the plant data
     */
    fun getPlantData(): PlantData {
        return this.plantData
    }

    /**
     * Sets the sowing handler
     */
    fun setSowingHandler(sowingHandler: SowingHandler) {
        this.sowingHandler = sowingHandler
    }

    /**
     * Sets the harvesting handler
     */
    fun setHarvestingHandler(harvestingHandler: HarvestingHandler) {
        this.harvestingHandler = harvestingHandler
    }

    /**
     * Sets the weeding handler
     */
    fun setWeedingHandler(weedingHandler: WeedingHandler) {
        this.weedingHandler = weedingHandler
    }

    /**
     * Sets the cutting handler
     */
    fun setCuttingHandler(cuttingHandler: CuttingHandler) {
        this.cuttingHandler = cuttingHandler
    }

    /**
     * Sets the mowing handler
     */
    fun setMowingHandler(mowingHandler: MowingHandler) {
        this.mowingHandler = mowingHandler
    }

    /**
     * Sets the irrigation handler
     */
    fun setIrrigationHandler(irrigationHandler: IrrigationHandler) {
        this.irrigationHandler = irrigationHandler
    }

    /**
     * Starts the farm phase
     */
    fun farmPhase(yearTick: Int, simTick: Int) {
        for (farm in farms) {
            farm.updateNeededActions(yearTick, simTick)
        }
        for (farm in farms) {
            Logger.logFarmStart(farm.getId())
            sowingHandler.startPhase(farm, yearTick, simTick)
            harvestingHandler.startPhase(farm, yearTick, simTick)
            cuttingHandler.startPhase(farm, yearTick, simTick)
            for (machine in farm.getMachines().sortedWith(compareBy({ it.duration }, { it.id }))) {
                // hashMap checking still needed inside handlers
                if (machine.brokenFor?.inRange(simTick) == true) {
                    continue
                }
                if (machine.isStuck || machine.id in farm.machineHashMap) {
                    continue
                }

                assignMachine(machine, farm, yearTick)
            }
            // Clear both hashmaps at the end of the phase for the farm
            farm.machineHashMap.clear()
            farm.tileHashMap.clear()
            // Log end farm actions
            Logger.logFarmEnd(farm.getId())
        }
    }

    private fun assignMachine(machine: Machine, farm: Farm, yearTick: Int) {
        // IRRIGATE FIELDS
        if (machine.actions.contains(ActionType.IRRIGATING)) {
            irrigationHandler.startPhase(farm, machine, TileType.FIELD)
        }

        // WEED FIELDS
        if (machine.actions.contains(ActionType.WEEDING) && machine.id !in farm.machineHashMap) {
            weedingHandler.startPhase(farm, machine)
        }

        // IRRIGATE PLANTATIONS
        if (machine.actions.contains(ActionType.IRRIGATING) && machine.id !in farm.machineHashMap) {
            irrigationHandler.startPhase(farm, machine, TileType.PLANTATION)
        }

        // MOW PLANTATIONS
        if (machine.actions.contains(ActionType.MOWING) && machine.id !in farm.machineHashMap) {
            mowingHandler.startPhase(farm, machine, yearTick)
        }
    }
}
