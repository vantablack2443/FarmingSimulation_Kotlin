package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.OAT_HARVEST

const val OAT_SUNLIGHT = 90
const val OAT_MOISTURE = 300
const val OAT_SOW_START = 6
const val OAT_SOW_END = 6
const val OAT_HARVEST_START = 13
const val OAT_HARVEST_END = 16
const val OAT_WEED_START_OFFSET = 1
const val OAT_WEED_END_OFFSET = 3

/**
 * Oat class
 */
class Oat : FieldPlant() {
    override var neededSunlight = OAT_SUNLIGHT
    override var neededMoisture = OAT_MOISTURE
    override var harvestEstimate = OAT_HARVEST
    override var sowingTime: Duration = Duration(OAT_SOW_START, OAT_SOW_END)
    override var harvestingTime = Duration(OAT_HARVEST_START, OAT_HARVEST_END)
    override val actionsNeeded = mutableListOf<ActionType>()
    override val lateActions = mutableListOf<ActionType>()
    override var bloomingTime: Duration? = null
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0

    override val cuttingTime = mutableListOf<CustomPair>()
    override val mowingTime = mutableListOf<CustomPair>()

    override fun needsHarvesting(tick: Int) {
        if ((OAT_HARVEST_START..OAT_HARVEST_END).contains(tick)) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
    }

    // USES SIM-TICK
    override fun needsWeeding(tick: Int) {
        // In the first 3 ticks after sowing
        if (((sownTick + OAT_WEED_START_OFFSET)..(sownTick + OAT_WEED_END_OFFSET)).contains(tick)) {
            actionsNeeded.add(ActionType.WEEDING)
        } else if (tick <= OAT_HARVEST_END + 2) {
            lateActions.add(ActionType.HARVESTING)
        }
        // Oat can be harvested up to two ticks after the harvesting period ends
    }

    // OAT does not require pollination. Does not bloom

    override fun checkLateSowing() {
        if (sownTick - OAT_SOW_END == 1 || sownTick - OAT_SOW_END == 2) {
            lateActions.add(ActionType.SOWING)
        }
    }

    /**
     * Penalty applied once. Can by estimate handler once when late sowing detected
     * 20% per delayed tick
     */
    override fun applyLateSowingPenalty() {
        var counter = sownTick - OAT_SOW_END
        while (counter > 0) {
            this.harvestEstimate = (LATE_SOW_PENALTY_FIELDS * this.harvestEstimate).toInt()
            counter--
        }
    }

    /**
     * Penalty applied per late tick. Can be called each tick by estimator.
     * Takes year tick
     */
    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick <= OAT_HARVEST_END) { // not late
            return
        } else if (tick - OAT_HARVEST_END > 2) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        } else { // %20 reduction per tick
            this.harvestEstimate = (LATE_HARVEST_PENALTY_FIELDS * this.harvestEstimate).toInt()
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = OAT_HARVEST
    }
}
