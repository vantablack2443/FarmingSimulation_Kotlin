package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.POTATO_HARVEST

const val POTATO_SUNLIGHT = 130
const val POTATO_MOISTURE = 500
const val POTATO_SOW_START = 7
const val POTATO_SOW_END = 10
const val POTATO_HARVEST_START = 17
const val POTATO_HARVEST_END = 20

/**
 * Potato class
 */
class Potato : FieldPlant() {
    override var neededSunlight = POTATO_SUNLIGHT
    override var neededMoisture = POTATO_MOISTURE
    override var harvestEstimate = POTATO_HARVEST
    override var sowingTime: Duration = Duration(POTATO_SOW_START, POTATO_SOW_END)
    override var harvestingTime: Duration = Duration(POTATO_HARVEST_START, POTATO_HARVEST_END)

    override fun needsHarvesting(yearTick: Int) {
        if (POTATO_HARVEST_START <= yearTick && yearTick <= POTATO_HARVEST_END) {
            actionsNeeded.add(ActionType.HARVEST)
        }
    }

    override fun needsWeeding(simTick: Int) {
        if ((simTick - sownTick) % 2 == 0 && simTick != sownTick) {
            actionsNeeded.add(ActionType.WEED)
        }
    }

    // SownTick: SimTick needs to be converted to yearTick here
    override fun checkLateSowing() {
        if (sownTick - POTATO_SOW_END <= 2) {
            lateActions.add(ActionType.SOW)
        }
    }

    override fun applyLateSowingPenalty() {
        var counter = sownTick - POTATO_SOW_END
        while (counter > 0) {
            this.harvestEstimate = (LATE_SOW_PENALTY_FIELDS * this.harvestEstimate).toInt()
            counter--
        }
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick > POTATO_HARVEST_END) {
            this.harvestEstimate = 0
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = POTATO_HARVEST
    }
}
