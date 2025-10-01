
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.ALMOND_HARVEST
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
const val ALMOND_BLOOM_END = 5
const val ALMOND_LATE_HARVEST_PENALTY = 0.9
const val ALMOND_HARVEST_START = 16
const val ALMOND_HARVEST_END = 19

/**
 * almond class
 */
class Almond : PlantationPlant() {
    override var neededSunlight = ALMOND_SUNLIGHT
    override var neededMoisture = ALMOND_MOISTURE
    override var harvestEstimate = ALMOND_HARVEST
    override var animalAttack = false
    override val pollination = mutableListOf<Double>()

    override val animalAttackPenalty = mutableListOf<Double>()

//    override var animalAttackPenalty = 1.0
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
        for (debuff in animalAttackPenalty) {
            val newEstimate = this.harvestEstimate * debuff
            this.harvestEstimate = maxOf(floor(newEstimate).toInt(), 0)
        }
//        val newEstimate = this.harvestEstimate * animalAttackPenalty
//        this.harvestEstimate = maxOf(floor(newEstimate).toInt(), 0)
    }

    override fun applyPollinationBuff() {
        for (buff in pollination) {
            val newEstimate = this.harvestEstimate * buff
            this.harvestEstimate = floor(newEstimate).toInt()
        }
    }

    override fun doAnimalAttack() {
        this.animalAttackPenalty.add(ANIMAL_ATTACK_PENALTY)
//        this.animalAttackPenalty *= ANIMAL_ATTACK_PENALTY
        this.animalAttack = true
    }

    override fun doBeeHappy(effect: Double) {
        this.pollination.add(effect)
    }

    override fun isBlooming(tick: Int): Boolean {
        return tick in ALMOND_BLOOM_START..ALMOND_BLOOM_END
    }

    override fun needsHarvesting(
        yearTick: Int,
        actionsNeeded: MutableList<ActionType>
    ) {
        if (this.harvestEstimate == 0) { return }
        if (yearTick in ALMOND_HARVEST_START..ALMOND_HARVEST_END) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
        if (yearTick == ALMOND_HARVEST_END + 1) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
    }

    override fun applyLateHarvestPenalty(yearTick: Int): Boolean {
        var acted = false
        if (yearTick == ALMOND_HARVEST_END + 1) { // at the end of 1st late tick, apply penalty for 2nd late
            this.harvestEstimate = 0
            acted = true
        }
        if (yearTick == ALMOND_HARVEST_END) {
            // at the end of last harvest tick, apply penalty for 1st late
            val newEstimate = floor(this.harvestEstimate * ALMOND_LATE_HARVEST_PENALTY)
            this.harvestEstimate = newEstimate.toInt()
            acted = true
        }
        return acted
    }

    override fun filterHarvestingIfNotMissed(yearTick: Int, actionsNeeded: MutableList<ActionType>) {
        if (actionsNeeded.contains(ActionType.HARVESTING)) {
            if (yearTick in ALMOND_HARVEST_START..<ALMOND_HARVEST_END) {
                actionsNeeded.remove(ActionType.HARVESTING)
            }
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = ALMOND_HARVEST
    }
}
