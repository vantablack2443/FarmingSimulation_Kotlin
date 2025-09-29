package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import kotlin.math.floor

const val ANIMAL_ATTACK_PENALTY_FIELDS = 0.5
const val MISSED_WEEDING_PENALTY = 0.9
const val LATE_SOW_PENALTY_FIELDS = 0.8

/**
 * abstract class for field plants
 */
abstract class FieldPlant : Plant() {
    open var sowingTime: Duration = Duration(-1, -1)
    open var sownYearTick: Int = -1
    open var sownSimTick: Int = -1

    /**
     * I removed the yearTick parameter from checkLateSowing and
     *  applyLateSowingPenalty since sownTick initialized in sowingHandler
     *  and can be accessed directly from within the concrete plant
     */
    override fun applyMissedWeedingPenalty() {
        val newEstimate = floor(this.harvestEstimate * MISSED_WEEDING_PENALTY)
        this.harvestEstimate = newEstimate.toInt()
    }

    /**
     * Does Animal Attack on the plant
     * Sets the animalAttack boolean to true
     */
    override fun doAnimalAttack() {
        this.animalAttack = true
        this.animalAttackPenalty *= ANIMAL_ATTACK_PENALTY_FIELDS
    }

    /**
     * Applies the penalty for an animal attack on field plants
     * Reduces the harvest estimate by 50%
     */
    override fun animalAttackPenalty() {
        val newEstimate = this.harvestEstimate * animalAttackPenalty
        this.harvestEstimate = maxOf(floor(newEstimate).toInt(), 0)
    }

    /**
     * Sets sowing sim tick and year tick in field plants
     */
    override fun setSowingTime(sownSimTick: Int, sownYearTick: Int) {
        this.sownSimTick = sownSimTick
        this.sownYearTick = sownYearTick
    }
}
