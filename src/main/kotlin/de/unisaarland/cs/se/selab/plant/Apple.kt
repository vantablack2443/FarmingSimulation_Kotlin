
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.APPLE_HARVEST
import kotlin.math.floor

const val APPLE_SUNLIGHT = 50
const val APPLE_MOISTURE = 100
const val APPLE_CUT_START = 21
const val APPLE_CUT_END = 22
const val APPLE_CUT_START_ALT = 3
const val APPLE_CUT_END_ALT = 4
const val APPLE_MOW_START_END = 11
const val APPLE_MOW_ALTERNATE = 17
const val APPLE_BLOOM_START = 8
const val APPLE_BLOOM_END = 9
const val ANIMAL_ATTACK_PENALTY = 0.9
const val APPLE_HARVEST_START = 17
const val APPLE_HARVEST_END = 19
const val APPLE_LATE_HARVEST_PENALTY = 0.5

/**
 * apple class
 */
class Apple : PlantationPlant() {
    override var neededSunlight = APPLE_SUNLIGHT
    override var neededMoisture = APPLE_MOISTURE
    override var harvestEstimate = APPLE_HARVEST
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0
    override val cuttingTime = mutableListOf(
        CustomPair(Duration(APPLE_CUT_START, APPLE_CUT_END), false),
        CustomPair(Duration(APPLE_CUT_START_ALT, APPLE_CUT_END_ALT), false)
    )
    override val mowingTime = mutableListOf(
        CustomPair(Duration(APPLE_MOW_START_END, APPLE_MOW_START_END), false),
        CustomPair(Duration(APPLE_MOW_ALTERNATE, APPLE_MOW_ALTERNATE), false)
    )
    override var harvestingTime = Duration(APPLE_HARVEST_START, APPLE_HARVEST_END)
    override var bloomingTime: Duration? = Duration(APPLE_BLOOM_START, APPLE_BLOOM_END)

    /**
     * updates harvest estimate based on the animal attack
     */
    override fun animalAttackPenalty() {
        val newEstimate = this.harvestEstimate * animalAttackPenalty
        this.harvestEstimate = maxOf(floor(newEstimate).toInt(), 0)
    }

    /**
     * updates the harvest estimate based on the bee happy effect
     */
    override fun applyPollinationBuff() {
        val newEstimate = this.harvestEstimate * pollination
        this.harvestEstimate = floor(newEstimate).toInt()
    }

    /**
     * updates the animal attack penalty
     */
    override fun doAnimalAttack() {
        // this.animalAttack = true
        this.animalAttackPenalty *= ANIMAL_ATTACK_PENALTY
    }

    /**
     * updates the pollination effect
     */
    override fun doBeeHappy(effect: Double) {
        this.pollination *= effect
    }

    /**
     * checks if the plant is blooming
     */
    override fun isBlooming(tick: Int): Boolean {
        return tick in APPLE_BLOOM_START..APPLE_BLOOM_END
    }

    /**
     * checks if the plant needs harvesting in this tick
     */
    override fun needsHarvesting(
        yearTick: Int,
        actionsNeeded: MutableList<ActionType>,
        lateActions: MutableList<ActionType>
    ) {
        if (yearTick in APPLE_HARVEST_START..APPLE_HARVEST_END) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
        if (yearTick == APPLE_HARVEST_END + 1) {
            actionsNeeded.add(ActionType.HARVESTING)
            lateActions.add(ActionType.HARVESTING)
        }
    }

    /**
     * updates harvest estimate based on the late penalty ; tick is year tick
     */
    override fun applyLateHarvestPenalty(yearTick: Int) {
        if (yearTick - APPLE_HARVEST_END > 1) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        }
        if (yearTick - APPLE_HARVEST_END == 1) {
            // up to 2 ticks late, reduce by 10% per tick
            val newEstimate = floor(this.harvestEstimate * APPLE_LATE_HARVEST_PENALTY)
            this.harvestEstimate = newEstimate.toInt()
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = APPLE_HARVEST
    }
}
