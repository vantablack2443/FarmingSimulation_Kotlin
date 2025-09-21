
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.plantdata.GRAPE_HARVEST

const val GRAPE_SUNLIGHT = 150
const val GRAPE_MOISTURE = 250
const val GRAPE_CUT_START = 15
const val GRAPE_CUT_END = 16
const val GRAPE_CUT_ALT = 14
const val GRAPE_MOW_START_END = 7
const val GRAPE_MOW_ALTERNATE = 13
const val GRAPE_HARVEST_TIME = 17
const val GRAPE_BLOOM_START = 12
const val GRAPE_BLOOM_END = 13

/**
 * apple class
 */
class Grape : PlantationPlant() {
    override var neededSunlight = GRAPE_SUNLIGHT
    override var neededMoisture = GRAPE_MOISTURE
    override var harvestEstimate = GRAPE_HARVEST
    override var cuttingTime = mutableListOf(
        Pair(Duration(GRAPE_CUT_START, GRAPE_CUT_END), false),
        Pair(Duration(GRAPE_CUT_ALT, GRAPE_CUT_ALT), false)
    )
    override var mowingTime = mutableListOf(
        Pair(Duration(GRAPE_MOW_START_END, GRAPE_MOW_START_END), false),
        Pair(Duration(GRAPE_MOW_ALTERNATE, GRAPE_MOW_ALTERNATE), false)
    )
    override var harvestingTime = Duration(GRAPE_HARVEST_TIME, GRAPE_HARVEST_TIME)
    override var bloomingTime: Duration? = Duration(GRAPE_BLOOM_START, GRAPE_BLOOM_END)

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

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = GRAPE_HARVEST
    }
}
