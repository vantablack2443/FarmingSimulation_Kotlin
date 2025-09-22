
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.plantdata.ALMOND_HARVEST

const val ALMOND_SUNLIGHT = 130
const val ALMOND_MOISTURE = 400
const val ALMOND_CUT_START = 21
const val ALMOND_CUT_END = 22
const val ALMOND_CUT_START_ALT = 3
const val ALMOND_CUT_END_ALT = 4
const val ALMOND_MOW_START_END = 11
const val ALMOND_MOW_ALTERNATE = 17
const val ALMOND_HARVEST_START = 16
const val ALMOND_HARVEST_END = 18
const val ALMOND_BLOOM_START = 4
const val ALMOND_BLOOM_END = 4
const val PENALTY_POINT_NINE = 0.9

/**
 * almond class
 */
class Almond : PlantationPlant() {
    override var neededSunlight = ALMOND_SUNLIGHT
    override var neededMoisture = ALMOND_MOISTURE
    override var harvestEstimate = ALMOND_HARVEST
    override var cuttingTime = mutableListOf(
        Pair(Duration(ALMOND_CUT_START, ALMOND_CUT_END), false),
        Pair(Duration(ALMOND_CUT_START_ALT, ALMOND_CUT_END_ALT), false)
    )
    override var mowingTime = mutableListOf(
        Pair(Duration(ALMOND_MOW_START_END, ALMOND_MOW_START_END), false),
        Pair(Duration(ALMOND_MOW_ALTERNATE, ALMOND_MOW_ALTERNATE), false)
    )
    override var harvestingTime = Duration(ALMOND_HARVEST_START, ALMOND_HARVEST_END)
    override var bloomingTime: Duration? = Duration(ALMOND_BLOOM_START, ALMOND_BLOOM_END)

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

    override fun needsCutting(tick: Int) {
        // TODO
    }

    override fun needsMowing(tick: Int) {
        // TODO
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick <= ALMOND_HARVEST_END) {
            return
        } else if (tick - ALMOND_HARVEST_END > 1) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        } else { // up to 2 ticks late, reduce by 10% per tick
            this.harvestEstimate = (this.harvestEstimate * PENALTY_POINT_NINE).toInt()
        }
    }


    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = ALMOND_HARVEST
    }

}