package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration

/**
 * abstract class for field plants
 */
abstract class FieldPlant : Plant() {
    open var sowingTime: Duration = Duration(-1, -1)
    open var sownTick: Int = -1

    /**
     * I removed the yearTick parameter from checkLateSowing and
     *  applyLateSowingPenalty since sownTick initialized in sowingHandler
     *  and can be accessed directly from within the concrete plant
     */
    open fun applyMissedWeedingPenalty() {
        this.harvestEstimate = (0.9 * this.harvestEstimate).toInt()
    }

    abstract fun checkLateSowing()
    abstract fun needsWeeding(tick: Int)
    abstract fun applyLateSowingPenalty()
}
