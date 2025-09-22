
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
const val APPLE_HARVEST_START = 17
const val APPLE_HARVEST_END = 19
const val APPLE_BLOOM_START = 8
const val APPLE_BLOOM_END = 9
const val ANIMAL_ATTACK_PENALTY = 0.9

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
    override fun doBeeHappy(effect: Int) {
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
    override fun needsHarvesting(tick: Int) {
        if (tick in APPLE_HARVEST_START..APPLE_HARVEST_END) {
            this.actionsNeeded.add(ActionType.HARVEST)
        }
        if (tick == APPLE_HARVEST_END + 1 || tick == APPLE_HARVEST_END + 2) {
            this.actionsNeeded.add(ActionType.HARVEST)
            this.lateActions.add(ActionType.HARVEST)
        }
    }

    /**
     * updates harvest estimate based on the late penalty ; tick is year tick
     */
    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick - APPLE_HARVEST_END > 1) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        }
        if (tick - APPLE_HARVEST_END == 1) { // up to 1 tick late, reduce by half
            this.harvestEstimate /= 2
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = APPLE_HARVEST
    }
}
