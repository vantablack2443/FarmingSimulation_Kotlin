
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration

const val APPLE_SUNLIGHT = 50
const val APPLE_MOISTURE = 100
const val APPLE_HARVEST = 1700000
const val APPLE_CUT_START = 21
const val APPLE_CUT_END = 22
const val APPLE_CUT_START_ALT = 3
const val APPLE_CUT_END_ALT = 4
const val APPLE_MOW_START_END = 11
const val APPLE_MOW_ALTERNATE = 17
const val APPLE_HARVEST_START = 17
const val APPLE_HARVEST_END = 19
const val APPLE_BLOOM_START = 8
const val APPLE_BLOOM_END = 9

/**
 * apple class
 */
class Apple : PlantationPlant() {
    override var neededSunlight = APPLE_SUNLIGHT
    override var neededMoisture = APPLE_MOISTURE
    override var harvestEstimate = APPLE_HARVEST
    override var cuttingTime = mutableListOf(
        Pair(Duration(APPLE_CUT_START, APPLE_CUT_END), false),
        Pair(Duration(APPLE_CUT_START_ALT, APPLE_CUT_END_ALT), false)
    )
    override var mowingTime = mutableListOf(
        Pair(Duration(APPLE_MOW_START_END, APPLE_MOW_START_END), false),
        Pair(Duration(APPLE_MOW_ALTERNATE, APPLE_MOW_ALTERNATE), false)
    )
    override var harvestingTime = Duration(APPLE_HARVEST_START, APPLE_HARVEST_END)
    override var bloomingTime: Duration? = Duration(APPLE_BLOOM_START, APPLE_BLOOM_END)

    override fun animalAttackPenalty() {
        TODO("Not yet implemented")
    }

    override fun applyPollinationBuff() {
        TODO("Not yet implemented")
    }

    override fun doAnimalAttack() {
        TODO("Not yet implemented")
    }

    override fun doBeeHappy() {
        TODO("Not yet implemented")
    }

    override fun isBlooming(tick: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun needsHarvesting(tick: Int) {
        TODO("Not yet implemented")
    }
}