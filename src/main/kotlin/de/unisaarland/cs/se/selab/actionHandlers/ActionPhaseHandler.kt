package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.plantdata.PlantData

class ActionPhaseHandler(farms: List<Farm>) {
    private lateinit var sowingHandler: SowingHandler
    private lateinit var harvestingHandler: HarvestingHandler
    private lateinit var weedingHandler: WeedingHandler
    private lateinit var cuttingHandler: CuttingHandler
    private lateinit var mowingHandler: MowingHandler
    private val plantData = PlantData()

    fun getPlantData(): PlantData {
        return this.plantData
    }

    fun setSowingHandler(sowingHandler: SowingHandler) {
        this.sowingHandler = sowingHandler
    }

    fun setHarvestingHandler(harvestingHandler: HarvestingHandler) {
        this.harvestingHandler = harvestingHandler
    }

    fun setWeedingHandler(weedingHandler: WeedingHandler) {
        this.weedingHandler = weedingHandler
    }

    fun setCuttingHandler(cuttingHandler: CuttingHandler) {
        this.cuttingHandler = cuttingHandler
    }

    fun setMowingHandler(mowingHandler: MowingHandler) {
        this.mowingHandler = mowingHandler
    }
}
