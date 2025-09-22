
package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.GRAPE_HARVEST
import de.unisaarland.cs.se.selab.plantdata.GRAPE_HARVEST_START_END
import kotlin.math.floor

const val GRAPE_SUNLIGHT = 150
const val GRAPE_MOISTURE = 250
const val GRAPE_CUT_START = 15
const val GRAPE_CUT_END = 16
const val GRAPE_CUT_ALT = 14
const val GRAPE_MOW_START_END = 7
const val GRAPE_MOW_ALTERNATE = 13
const val GRAPE_BLOOM_START = 12
const val GRAPE_BLOOM_END = 13
const val GRAPE_LATE_HARVEST_PENALTY = 0.95
const val GRAPE_ANIMAL_ATTACK_PENALTY = 0.5

/**
 * apple class
 */
class Grape : PlantationPlant() {
    override var neededSunlight = GRAPE_SUNLIGHT
    override var neededMoisture = GRAPE_MOISTURE
    override var harvestEstimate = GRAPE_HARVEST
    override var actionsNeeded = mutableListOf<ActionType>()
    override var lateActions = mutableListOf<ActionType>()
    override var animalAttack = false
    override var pollination = 1.0
    override var animalAttackPenalty = 1.0
    override val cuttingTime = mutableListOf(
        Pair(Duration(GRAPE_CUT_START, GRAPE_CUT_END), false),
        Pair(Duration(GRAPE_CUT_ALT, GRAPE_CUT_ALT), false)
    )
    override val mowingTime = mutableListOf(
        Pair(Duration(GRAPE_MOW_START_END, GRAPE_MOW_START_END), false),
        Pair(Duration(GRAPE_MOW_ALTERNATE, GRAPE_MOW_ALTERNATE), false)
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
    }

    override fun needsHarvesting(tick: Int) {
        if (tick == GRAPE_HARVEST_START_END) {
            this.actionsNeeded.add(ActionType.HARVEST)
        }
        if (tick in GRAPE_HARVEST_START_END + 1..GRAPE_HARVEST_START_END + 3) {
            this.actionsNeeded.add(ActionType.HARVEST)
            this.lateActions.add(ActionType.HARVEST)
        }
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick - GRAPE_HARVEST_START_END > 3) { // more than 3 ticks late, set to 0
            this.harvestEstimate = 0
            return
        }
        var effect = GRAPE_LATE_HARVEST_PENALTY
        var counter = tick
        while (counter <= GRAPE_HARVEST_START_END + 3) {
            effect *= GRAPE_LATE_HARVEST_PENALTY
            counter++
        }
        val newEstimate = this.harvestEstimate * effect
        this.harvestEstimate = newEstimate.toInt()
    }
}
