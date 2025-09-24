
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.CHERRY_HARVEST
import de.unisaarland.cs.se.selab.plantdata.CHERRY_HARVEST_END
import de.unisaarland.cs.se.selab.plantdata.CHERRY_HARVEST_START
import kotlin.math.floor

const val CHERRY_SUNLIGHT = 120
const val CHERRY_MOISTURE = 150
const val CHERRY_CUT_START = 21
const val CHERRY_CUT_END = 22
const val CHERRY_CUT_START_ALT = 3
const val CHERRY_CUT_END_ALT = 4
const val CHERRY_MOW_START_END = 11
const val CHERRY_BLOOM_START = 8
const val CHERRY_BLOOM_END = 9
const val CHERRY_LATE_HARVEST_PENALTY = 0.7

/**
 * apple class
 */
class Cherry : PlantationPlant() {
    override var neededSunlight = CHERRY_SUNLIGHT
    override var neededMoisture = CHERRY_MOISTURE
    override var harvestEstimate = CHERRY_HARVEST
    override val actionsNeeded = mutableListOf<ActionType>()
    override val lateActions = mutableListOf<ActionType>()
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0
    override val cuttingTime = mutableListOf(
        Pair(Duration(CHERRY_CUT_START, CHERRY_CUT_END), false),
        Pair(Duration(CHERRY_CUT_START_ALT, CHERRY_CUT_END_ALT), false)
    )
    override val mowingTime = mutableListOf(
        Pair(Duration(CHERRY_MOW_START_END, CHERRY_MOW_START_END), false)
    )
    override var harvestingTime = Duration(CHERRY_HARVEST_START, CHERRY_HARVEST_END)
    override var bloomingTime: Duration? = Duration(CHERRY_BLOOM_START, CHERRY_BLOOM_END)

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
        return tick in CHERRY_BLOOM_START..CHERRY_BLOOM_END
    }

    override fun needsHarvesting(tick: Int) {
        if (tick in CHERRY_HARVEST_START..CHERRY_HARVEST_END) {
            this.actionsNeeded.add(ActionType.HARVESTING)
        }
        if (tick == CHERRY_HARVEST_END + 1) {
            this.actionsNeeded.add(ActionType.HARVESTING)
            this.lateActions.add(ActionType.HARVESTING)
        }
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick - CHERRY_HARVEST_END > 1) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        }
        if (tick - CHERRY_HARVEST_END == 1) {
            // up to 2 ticks late, reduce by 10% per tick
            val newEstimate = floor(this.harvestEstimate * CHERRY_LATE_HARVEST_PENALTY)
            this.harvestEstimate = newEstimate.toInt()
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = CHERRY_HARVEST
    }
}
