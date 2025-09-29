package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.WHEAT_HARVEST
import kotlin.math.floor

const val WHEAT_SUNLIGHT = 90
const val WHEAT_MOISTURE = 450
const val WHEAT_SOW_START = 19
const val WHEAT_SOW_END = 20
const val WHEAT_BLOOM_START = 9
const val WHEAT_BLOOM_END = 9
const val WHEAT_HARVEST_START = 11
const val WHEAT_HARVEST_END = 13
const val WHEAT_WEED_START_OFFSET = 4
const val WHEAT_WEED_END_OFFSET = 10
const val WHEAT_LATE_HARVEST_PENALTY = 0.8

/**
 * Wheat class
 */
class Wheat : FieldPlant() {
    override var neededSunlight = WHEAT_SUNLIGHT
    override var neededMoisture = WHEAT_MOISTURE
    override var harvestEstimate = WHEAT_HARVEST
    override var sowingTime: Duration = Duration(WHEAT_SOW_START, WHEAT_SOW_END)
    override var harvestingTime = Duration(WHEAT_HARVEST_START, WHEAT_HARVEST_END)
    override var bloomingTime: Duration? = Duration(WHEAT_BLOOM_START, WHEAT_BLOOM_END)
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0

    override val cuttingTime = mutableListOf<CustomPair>()
    override val mowingTime = mutableListOf<CustomPair>()

    override fun needsHarvesting(
        yearTick: Int,
        actionsNeeded: MutableList<ActionType>
    ) {
        if (harvestEstimate == 0) return
        if ((WHEAT_HARVEST_START..WHEAT_HARVEST_END).contains(yearTick)) {
            actionsNeeded.add(ActionType.HARVESTING)
        } else if (yearTick in WHEAT_HARVEST_END..WHEAT_HARVEST_END + 2) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
        // Wheat can be harvested up to two ticks after the harvesting period ends
    }

    // USES SIM-TICK
    override fun needsWeeding(simTick: Int, actionsNeeded: MutableList<ActionType>) {
        // Weed 3 ticks and 10 ticks after sowing
        // Refer to specification for more details -> 3 ticks after tick x means on the x + 4th tick
        if (simTick == sownTick + WHEAT_WEED_START_OFFSET || simTick == sownTick + WHEAT_WEED_END_OFFSET) {
            actionsNeeded.add(ActionType.WEEDING)
        }
    }

    // WHEAT does not require pollination. Does not bloom

    // Needs to convert sownTick: simTick to yearTick here!!!!!
    override fun checkLateSowing(lateActions: MutableList<ActionType>, yearTickSown: Int) {
        if (yearTickSown - WHEAT_SOW_END == 1 || yearTickSown - WHEAT_SOW_END == 2) {
            lateActions.add(ActionType.SOWING)
        }
    }

    /**
     * Penalty applied once. Can by estimate handler once when late sowing detected
     * 20% per delayed tick
     */
    override fun applyLateSowingPenalty() {
        var counter = sownTick - WHEAT_SOW_END
        while (counter > 0) {
            this.harvestEstimate = floor(LATE_SOW_PENALTY_FIELDS * this.harvestEstimate).toInt()
            counter--
        }
    }

    /**
     * Penalty applied per late tick. Can be called each tick by estimator.
     * Takes year tick
     */
    override fun applyLateHarvestPenalty(yearTick: Int) {
        if (yearTick <= WHEAT_HARVEST_END) { // not late
            return
        } else if (yearTick - WHEAT_HARVEST_END > 2) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        } else { // %20 reduction per tick
            this.harvestEstimate = floor(WHEAT_LATE_HARVEST_PENALTY * this.harvestEstimate).toInt()
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = WHEAT_HARVEST
    }
}
