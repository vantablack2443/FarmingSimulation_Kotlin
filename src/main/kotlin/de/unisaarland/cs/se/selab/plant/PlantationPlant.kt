package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.enumerations.ActionType
import kotlin.math.floor

const val MISSED_MOWING_PENALTY = 0.9
const val MISSED_CUTTING_PENALTY = 0.5

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

    // MUST ONLY BE CALLED ONCE ALL CUTTING PERIODS ARE MISSED
    // This logic must be handled by the harvest estimator
    override fun applyCuttingPenalty() {
        val cuttingDone = cuttingTime.filter { it.second }
        if (cuttingDone.isNotEmpty()) {
            return
        }
        val newEstimate = floor(this.harvestEstimate * MISSED_CUTTING_PENALTY)
        this.harvestEstimate = newEstimate.toInt()
    }

    override fun applyMowingPenalty() {
        val newEstimate = floor(this.harvestEstimate * MISSED_MOWING_PENALTY)
        this.harvestEstimate = newEstimate.toInt()
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
