package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.OAT_HARVEST
import kotlin.math.floor

const val OAT_SUNLIGHT = 90
const val OAT_MOISTURE = 300
const val OAT_SOW_START = 6
const val OAT_SOW_END = 6
const val OAT_HARVEST_START = 13
const val OAT_HARVEST_END = 16
const val OAT_WEED_START_OFFSET = 1
const val OAT_WEED_END_OFFSET = 3
const val OAT_LATE_HARVEST_PENALTY = 0.8

/**
 * Oat class
 */
class Oat : FieldPlant() {
    override var neededSunlight = OAT_SUNLIGHT
    override var neededMoisture = OAT_MOISTURE
    override var harvestEstimate = OAT_HARVEST
    override var sowingTime: Duration = Duration(OAT_SOW_START, OAT_SOW_END)
    override var harvestingTime = Duration(OAT_HARVEST_START, OAT_HARVEST_END)

    override var bloomingTime: Duration? = null
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
        if ((OAT_HARVEST_START..OAT_HARVEST_END).contains(yearTick)) {
            actionsNeeded.add(ActionType.HARVESTING)
        } else if (yearTick in OAT_HARVEST_END..OAT_HARVEST_END + 2) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
        // Oat can be harvested up to two ticks after the harvesting period ends
    }

    // USES SIM-TICK
    override fun needsWeeding(simTick: Int, actionsNeeded: MutableList<ActionType>) {
        // In the first 3 ticks after sowing
        if (((sownSimTick + OAT_WEED_START_OFFSET)..(sownSimTick + OAT_WEED_END_OFFSET)).contains(simTick)) {
            actionsNeeded.add(ActionType.WEEDING)
        }
    }

    // OAT does not require pollination. Does not bloom

    override fun checkLateSowing(lateActions: MutableList<ActionType>, yearTickSown: Int) {
        if (yearTickSown - OAT_SOW_END == 1 || yearTickSown - OAT_SOW_END == 2) {
            lateActions.add(ActionType.SOWING)
        }
    }

    /**
     * Penalty applied once. Can by estimate handler once when late sowing detected
     * 20% per delayed tick
     */
    override fun applyLateSowingPenalty() {
        var counter = sownYearTick - OAT_SOW_END
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
        if (yearTick <= OAT_HARVEST_END) { // not late
            return
        } else if (yearTick - OAT_HARVEST_END > 2) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        } else { // %20 reduction per tick
            this.harvestEstimate = floor(OAT_LATE_HARVEST_PENALTY * this.harvestEstimate).toInt()
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = OAT_HARVEST
    }
}
