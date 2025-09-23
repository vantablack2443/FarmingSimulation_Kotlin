package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.PUMPKIN_HARVEST

const val PUMPKIN_SUNLIGHT = 120
const val PUMPKIN_MOISTURE = 600
const val PUMPKIN_SOW_START = 10
const val PUMPKIN_SOW_END = 12
const val PUMPKIN_HARVEST_START = 17
const val PUMPKIN_HARVEST_END = 20
const val PUMPKIN_BLOOM_START_OFFSET = 3
const val PUMPKIN_BLOOM_END_OFFSET = 4

/**
 * Pumpkin class
 */
class Pumpkin : FieldPlant() {
    override var neededSunlight = PUMPKIN_SUNLIGHT
    override var neededMoisture = PUMPKIN_MOISTURE
    override var harvestEstimate = PUMPKIN_HARVEST
    override var sowingTime: Duration = Duration(PUMPKIN_SOW_START, PUMPKIN_SOW_END)
    override var harvestingTime = Duration(PUMPKIN_HARVEST_START, PUMPKIN_HARVEST_END)
    override val actionsNeeded = mutableListOf<ActionType>()
    override val lateActions = mutableListOf<ActionType>()
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0
    override var bloomingTime: Duration? = null

    override val cuttingTime = mutableListOf<CustomPair>()
    override val mowingTime = mutableListOf<CustomPair>()

    // USES YEAR-TICK
    override fun needsHarvesting(tick: Int) {
        if ((PUMPKIN_HARVEST_START..PUMPKIN_HARVEST_END).contains(tick)) {
            actionsNeeded.add(ActionType.HARVEST)
        }
    }

    // USES SIM-TICK
    override fun needsWeeding(tick: Int) {
        if ((tick - sownTick) % 2 == 0 && tick != sownTick) {
            actionsNeeded.add(ActionType.WEED)
        }
    }

    // USES SIM-TICK
    override fun isBlooming(tick: Int): Boolean {
        // Two ticks after sowing
        // For two ticks
        return ((sownTick + PUMPKIN_BLOOM_START_OFFSET)..(sownTick + PUMPKIN_BLOOM_END_OFFSET)).contains(tick)
    }

    override fun doBeeHappy(effect: Double) {
        this.pollination *= effect
    }

    override fun applyPollinationBuff() {
        this.harvestEstimate = (this.harvestEstimate * pollination).toInt()
    }

    override fun checkLateSowing() {
        if (sownTick - PUMPKIN_SOW_END == 1 || sownTick - PUMPKIN_SOW_END == 2) {
            lateActions.add(ActionType.SOW)
        }
    }

    override fun applyLateSowingPenalty() {
        var counter = sownTick - PUMPKIN_SOW_END
        while (counter > 0) {
            this.harvestEstimate = (LATE_SOW_PENALTY_FIELDS * this.harvestEstimate).toInt()
            counter--
        }
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick > PUMPKIN_HARVEST_END) {
            this.harvestEstimate = 0
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = PUMPKIN_HARVEST
    }
}
