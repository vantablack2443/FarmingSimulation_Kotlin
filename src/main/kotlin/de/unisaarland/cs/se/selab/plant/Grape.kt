
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.GRAPE_HARVEST
import kotlin.math.floor

const val GRAPE_SUNLIGHT = 150
const val GRAPE_MOISTURE = 250
const val GRAPE_CUT_START = 14
const val GRAPE_CUT_END = 16
const val GRAPE_MOW_START_END = 7
const val GRAPE_MOW_ALTERNATE = 13
const val GRAPE_BLOOM_START = 12
const val GRAPE_BLOOM_END = 13
const val GRAPE_LATE_HARVEST_PENALTY = 0.95
const val GRAPE_ANIMAL_ATTACK_PENALTY = 0.5
const val GRAPE_HARVEST_START_END = 17

/**
 * apple class
 */
class Grape : PlantationPlant() {
    override var neededSunlight = GRAPE_SUNLIGHT
    override var neededMoisture = GRAPE_MOISTURE
    override var harvestEstimate = GRAPE_HARVEST
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0
    override val cuttingTime = mutableListOf(
        CustomPair(Duration(GRAPE_CUT_START, GRAPE_CUT_END), false),
    )
    override val mowingTime = mutableListOf(
        CustomPair(Duration(GRAPE_MOW_START_END, GRAPE_MOW_START_END), false),
        CustomPair(Duration(GRAPE_MOW_ALTERNATE, GRAPE_MOW_ALTERNATE), false)
    )
    override var harvestingTime = Duration(GRAPE_HARVEST_START_END, GRAPE_HARVEST_START_END)
    override var bloomingTime: Duration? = Duration(GRAPE_BLOOM_START, GRAPE_BLOOM_END)

    override fun animalAttackPenalty() {
        val newEstimate = this.harvestEstimate * animalAttackPenalty
        this.harvestEstimate = maxOf(floor(newEstimate).toInt(), 0)
    }

    override fun resetHarvestEstimate() {
        this.harvestEstimate = GRAPE_HARVEST
    }

    override fun doAnimalAttack() {
        this.animalAttackPenalty *= GRAPE_ANIMAL_ATTACK_PENALTY
        this.animalAttack = true
    }

    override fun needsHarvesting(
        yearTick: Int,
        actionsNeeded: MutableList<ActionType>
    ) {
        if (yearTick == GRAPE_HARVEST_START_END) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
        if (yearTick in GRAPE_HARVEST_START_END + 1..GRAPE_HARVEST_START_END + 3) {
            actionsNeeded.add(ActionType.HARVESTING)
        }
    }

    override fun applyLateHarvestPenalty(yearTick: Int) {
        if (yearTick - GRAPE_HARVEST_START_END > 3) { // more than 3 ticks late, set to 0
            this.harvestEstimate = 0
            return
        }

        if (yearTick in GRAPE_HARVEST_START_END + 1..GRAPE_HARVEST_START_END + 3) {
            // up to 3 ticks late, reduce by 5% per tick
            val newEstimate = floor(this.harvestEstimate * GRAPE_LATE_HARVEST_PENALTY)
            this.harvestEstimate = newEstimate.toInt()
        }
    }

    override fun resetMowingTime(startTick: Int) {
        return
    }
}
