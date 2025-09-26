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

    override var bloomingTime: Duration? = null
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0

    override val cuttingTime = mutableListOf<CustomPair>()
    override val mowingTime = mutableListOf<CustomPair>()

    // USES YEAR-TICK
    override fun needsHarvesting(
        yearTick: Int,
        actionsNeeded: MutableList<ActionType>,
        lateActions: MutableList<ActionType>
    ) {
        if ((POTATO_HARVEST_START..POTATO_HARVEST_END).contains(yearTick)) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
        // Potato has no late harvesting period
    }

    // USES SIM-TICK
    override fun needsWeeding(simTick: Int, actionsNeeded: MutableList<ActionType>) {
        if ((simTick - sownTick) % 2 == 0 && simTick != sownTick) {
            actionsNeeded.add(ActionType.WEEDING)
        }
    }

    // SownTick: SimTick needs to be converted to yearTick here
    override fun checkLateSowing(lateActions: MutableList<ActionType>) {
        if (sownTick - POTATO_SOW_END == 1 || sownTick - POTATO_SOW_END == 2) {
            lateActions.add(ActionType.SOWING)
        }
    }

    /**
     * Penalty applied once. Can by estimate handler once when late sowing detected
     * 20% per delayed tick
     */
    override fun applyLateSowingPenalty() {
        var counter = sownTick - POTATO_SOW_END
        while (counter > 0) {
            this.harvestEstimate = (LATE_SOW_PENALTY_FIELDS * this.harvestEstimate).toInt()
            counter--
        }
    }

    override fun applyLateHarvestPenalty(yearTick: Int) {
        if (yearTick > POTATO_HARVEST_END) {
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
