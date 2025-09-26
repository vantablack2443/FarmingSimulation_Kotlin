package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration

const val ANIMAL_ATTACK_PENALTY_FIELDS = 0.5
const val MISSED_WEEDING_PENALTY = 0.9
const val LATE_SOW_PENALTY_FIELDS = 0.8

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
    override fun applyMissedWeedingPenalty() {
        this.harvestEstimate = (MISSED_WEEDING_PENALTY * this.harvestEstimate).toInt()
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
        this.harvestEstimate = (animalAttackPenalty * this.harvestEstimate).toInt()
    }

}
