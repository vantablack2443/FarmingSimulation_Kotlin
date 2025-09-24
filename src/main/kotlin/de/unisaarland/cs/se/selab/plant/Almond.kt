
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.ALMOND_HARVEST
import de.unisaarland.cs.se.selab.plantdata.ALMOND_HARVEST_END
import de.unisaarland.cs.se.selab.plantdata.ALMOND_HARVEST_START
import kotlin.math.floor

const val ALMOND_SUNLIGHT = 130
const val ALMOND_MOISTURE = 400
const val ALMOND_CUT_START = 21
const val ALMOND_CUT_END = 22
const val ALMOND_CUT_START_ALT = 3
const val ALMOND_CUT_END_ALT = 4
const val ALMOND_MOW_START_END = 11
const val ALMOND_MOW_ALTERNATE = 17
const val ALMOND_BLOOM_START = 4
const val ALMOND_BLOOM_END = 4
const val ALMOND_LATE_HARVEST_PENALTY = 0.9

/**
 * almond class
 */
class Almond : PlantationPlant() {
    override var neededSunlight = ALMOND_SUNLIGHT
    override var neededMoisture = ALMOND_MOISTURE
    override var harvestEstimate = ALMOND_HARVEST
    override val actionsNeeded = mutableListOf<ActionType>()
    override val lateActions = mutableListOf<ActionType>()
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0
    override val cuttingTime = mutableListOf(
        CustomPair(Duration(ALMOND_CUT_START, ALMOND_CUT_END), false),
        CustomPair(Duration(ALMOND_CUT_START_ALT, ALMOND_CUT_END_ALT), false)
    )
    override val mowingTime = mutableListOf(
        CustomPair(Duration(ALMOND_MOW_START_END, ALMOND_MOW_START_END), false),
        CustomPair(Duration(ALMOND_MOW_ALTERNATE, ALMOND_MOW_ALTERNATE), false)
    )
    override var harvestingTime = Duration(ALMOND_HARVEST_START, ALMOND_HARVEST_END)
    override var bloomingTime: Duration? = Duration(ALMOND_BLOOM_START, ALMOND_BLOOM_END)

    override fun animalAttackPenalty() {
        val newEstimate = this.harvestEstimate * animalAttackPenalty
        this.harvestEstimate = maxOf(floor(newEstimate).toInt(), 0)
    }

    override fun applyPollinationBuff() {
        val newEstimate = this.harvestEstimate * pollination
        this.harvestEstimate = floor(newEstimate).toInt()
    }

    override fun doAnimalAttack() {
        this.animalAttackPenalty *= ANIMAL_ATTACK_PENALTY
    }

    override fun doBeeHappy(effect: Double) {
        this.pollination *= effect
    }

    override fun isBlooming(tick: Int): Boolean {
        return tick in ALMOND_BLOOM_START..ALMOND_BLOOM_END
    }

    override fun needsHarvesting(tick: Int) {
        if (tick in ALMOND_HARVEST_START..ALMOND_HARVEST_END) {
            this.actionsNeeded.add(ActionType.HARVESTING)
        }
        if (tick == ALMOND_HARVEST_END + 1) {
            this.actionsNeeded.add(ActionType.HARVESTING)
            this.lateActions.add(ActionType.HARVESTING)
        }
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick - ALMOND_HARVEST_END > 1) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        }
        if (tick - ALMOND_HARVEST_END == 1) {
            // up to 2 ticks late, reduce by 10% per tick
            val newEstimate = floor(this.harvestEstimate * ALMOND_LATE_HARVEST_PENALTY)
            this.harvestEstimate = newEstimate.toInt()
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = ALMOND_HARVEST
    }
}
