package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType

const val MISSED_MOWING_PENALTY = 0.9

/**
 * abstract class for plantation plants
 */
abstract class PlantationPlant : Plant() {
    open var cuttingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    open var mowingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    open fun needsCutting(tick: Int) {
        val cuttingDone = cuttingTime.filter { it.second }
        if (cuttingDone.isEmpty()) {
            this.actionsNeeded.add(ActionType.CUT)
        }
    }
    open fun needsMowing(tick: Int) {
        for (element in mowingTime) {
            val duration = element.first
            val done = element.second
            if (duration.inRange(tick) && !done) {
                actionsNeeded.add(ActionType.MOW)
            }
        }
    }

    open fun applyCuttingPenalty() {
        harvestEstimate /= 2
    }

    open fun applyMowingPenalty() {
        harvestEstimate *= (MISSED_MOWING_PENALTY * harvestEstimate).toInt()
    }



}