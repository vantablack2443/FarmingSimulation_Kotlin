package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration

const val WHEAT_SUNLIGHT = 90
const val WHEAT_MOISTURE = 450
const val WHEAT_HARVEST = 1500000
const val WHEAT_SOW_START = 19
const val WHEAT_SOW_END = 20
const val WHEAT_BLOOM_START = 9
const val WHEAT_BLOOM_END = 9
const val WHEAT_HARVEST_START = 11
const val WHEAT_HARVEST_END = 13

/**
 * Wheat class
 */
class Wheat : FieldPlant() {
    override var neededSunlight = WHEAT_SUNLIGHT
    override var neededMoisture = WHEAT_MOISTURE
    override var harvestEstimate = WHEAT_HARVEST
    override var sowingTime: Duration = Duration(WHEAT_SOW_START, WHEAT_SOW_END)
    override var harvestingTime = Duration(WHEAT_HARVEST_START, WHEAT_HARVEST_END)
    override var bloomingTime: Duration? = Duration(WHEAT_BLOOM_START, WHEAT_BLOOM_END)

    override fun needsHarvesting(tick: Int) {
        TODO("Not yet implemented")
    }

    override fun isBlooming(tick: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun animalAttackPenalty() {
        TODO("Not yet implemented")
    }

    override fun doAnimalAttack() {
        TODO("Not yet implemented")
    }

    override fun doBeeHappy() {
        TODO("Not yet implemented")
    }

    override fun applyPollinationBuff() {
        TODO("Not yet implemented")
    }
}