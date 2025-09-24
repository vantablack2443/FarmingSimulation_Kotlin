package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType

const val MISSED_MOWING_PENALTY = 0.9

/**
 * abstract class for plantation plants
 */
abstract class PlantationPlant : Plant() {
    open val cuttingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    open val mowingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    override fun needsCutting(tick: Int) {
        val cuttingDone = cuttingTime.filter { it.second }
        if (cuttingDone.isEmpty()) {
            this.actionsNeeded.add(ActionType.CUTTING)
        }
    }
    override fun needsMowing(tick: Int) {
        for (element in mowingTime) {
            val duration = element.first
            val done = element.second
            if (duration.inRange(tick) && !done) {
                actionsNeeded.add(ActionType.MOWING)
            }
        }
    }

    override fun applyCuttingPenalty() {
        harvestEstimate /= 2
    }

    override fun applyMowingPenalty() {
        harvestEstimate *= (MISSED_MOWING_PENALTY * harvestEstimate).toInt()
    }
}
