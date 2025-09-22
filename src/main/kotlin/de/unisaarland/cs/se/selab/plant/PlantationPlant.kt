package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration

/**
 * abstract class for plantation plants
 */
abstract class PlantationPlant : Plant() {
    open var cuttingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    open var mowingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    abstract fun needsCutting(tick: Int)
    abstract fun needsMowing(tick: Int)

    open fun applyCuttingPenalty() {
        harvestEstimate /= 2
    }

    open fun applyMowingPenalty() {
        harvestEstimate *= (PENALTY_POINT_NINE * harvestEstimate).toInt()
    }



}