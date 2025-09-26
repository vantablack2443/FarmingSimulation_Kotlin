package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.enumerations.ActionType

const val MISSED_MOWING_PENALTY = 0.9

/**
 * abstract class for plantation plants
 */
abstract class PlantationPlant : Plant() {
    override fun needsCutting(yearTick: Int, actionsNeeded: MutableList<ActionType>) {
        val cuttingDone = cuttingTime.filter { it.second }
        if (cuttingDone.isNotEmpty()) {
            return
        }

        for (cutIn in cuttingTime) {
            if (cutIn.first.inRange(yearTick)) {
                actionsNeeded.add(ActionType.CUTTING)
            }
        }
    }
    override fun needsMowing(yearTick: Int, actionsNeeded: MutableList<ActionType>) {
        for (element in mowingTime) {
            val duration = element.first
            val done = element.second
            if (duration.inRange(yearTick) && !done) {
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

    /**
     * resets mowing time after animal attack
     */
    override fun resetMowingTime(startTick: Int) {
        for (element in this.mowingTime) {
            // if mowing needed to be done
            if (element.first.inRange(startTick) && !element.second) {
                element.second = true
            }
            if (element.first.inRange(startTick + 1) && !element.second) {
                element.second = true
            }
        }
    }
}
