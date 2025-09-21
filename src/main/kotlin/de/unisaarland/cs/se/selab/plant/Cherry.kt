
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.plantdata.CHERRY_HARVEST

const val CHERRY_SUNLIGHT = 120
const val CHERRY_MOISTURE = 150
const val CHERRY_CUT_START = 21
const val CHERRY_CUT_END = 22
const val CHERRY_CUT_START_ALT = 3
const val CHERRY_CUT_END_ALT = 4
const val CHERRY_MOW_START_END = 11
const val CHERRY_HARVEST_START = 13
const val CHERRY_HARVEST_END = 14
const val CHERRY_BLOOM_START = 8
const val CHERRY_BLOOM_END = 9

/**
 * apple class
 */
class Cherry : PlantationPlant() {
    override var neededSunlight = CHERRY_SUNLIGHT
    override var neededMoisture = CHERRY_MOISTURE
    override var harvestEstimate = CHERRY_HARVEST
    override var cuttingTime = mutableListOf(
        Pair(Duration(CHERRY_CUT_START, CHERRY_CUT_END), false),
        Pair(Duration(CHERRY_CUT_START_ALT, CHERRY_CUT_END_ALT), false)
    )
    override var mowingTime = mutableListOf(
        Pair(Duration(CHERRY_MOW_START_END, CHERRY_MOW_START_END), false)
    )
    override var harvestingTime = Duration(CHERRY_HARVEST_START, CHERRY_HARVEST_END)
    override var bloomingTime: Duration? = Duration(CHERRY_BLOOM_START, CHERRY_BLOOM_END)

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
        this.harvestEstimate = CHERRY_HARVEST
    }
}