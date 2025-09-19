package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.farm.Farm

class ActionPhaseHandler(farms: List<Farm>) {
    private lateinit var sowingHandler: SowingHandler
    private lateinit var harvestinHandler: HarvestingHandler
    private lateinit var weedingHandler: WeedingHandler
    private lateinit var cuttingHandler: CuttingHandler
    private lateinit var mowingHandler: MowingHandler
    private lateinit var weedingHandler: WeedingHandler
}