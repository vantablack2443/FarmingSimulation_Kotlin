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

/**
 * Pumpkin class
 */
class Pumpkin: FieldPlant() {
    override var neededSunlight = PUMPKIN_SUNLIGHT
    override var neededMoisture = PUMPKIN_MOISTURE
    override var harvestEstimate = PUMPKIN_HARVEST
    override var sowingTime: Duration = Duration(PUMPKIN_SOW_START, PUMPKIN_SOW_END)
    override var harvestingTime = Duration(PUMPKIN_HARVEST_START, PUMPKIN_HARVEST_END)

    override fun needsHarvesting(tick: Int) {
        if((PUMPKIN_HARVEST_START .. PUMPKIN_HARVEST_END).contains(tick)){
            actionsNeeded.add(ActionType.HARVEST)
        }
    }

    override fun needsWeeding(simTick: Int) {
        if ((simTick - sownTick) % 2 == 0 && simTick != sownTick) {
            actionsNeeded.add(ActionType.WEED)
        }
    }

    override fun isBlooming(simTick: Int): Boolean {
        // Two ticks after sowing
        // For two ticks
        if (((sownTick + 3)..(sownTick + 4)).contains(simTick)) {
            return true
        }
        return false
    }

    override fun doBeeHappy(effect: Double) {
        this.pollination *= effect
    }

    override fun applyPollinationBuff() {
        this.harvestEstimate = (this.harvestEstimate * pollination).toInt()
    }

    override fun checkLateSowing() {
        if (sownTick - PUMPKIN_SOW_END <= 2) {
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