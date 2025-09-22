package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.plantdata.PlantData

/**
 * Handles the action phase of the simulation
 */
class ActionPhaseHandler(farms: List<Farm>) {
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
    fun farmPhase(currentyearTick: Int, simTick: Int) {
        TODO()
    }
}
