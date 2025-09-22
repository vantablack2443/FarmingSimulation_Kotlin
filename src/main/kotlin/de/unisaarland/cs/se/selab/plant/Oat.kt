package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.plantdata.OAT_HARVEST

const val OAT_SUNLIGHT = 90
const val OAT_MOISTURE = 300
const val OAT_SOW_START = 6
const val OAT_SOW_END = 6
const val OAT_HARVEST_START = 13
const val OAT_HARVEST_END = 16
const val PENALTY_POINT_EIGHT = 0.8

/**
 * Oat class
 */
class Oat : FieldPlant() {
    override var neededSunlight = OAT_SUNLIGHT
    override var neededMoisture = OAT_MOISTURE
    override var harvestEstimate = OAT_HARVEST
    override var sowingTime: Duration = Duration(OAT_SOW_START, OAT_SOW_END)
    override var harvestingTime = Duration(OAT_HARVEST_START, OAT_HARVEST_END)

    override fun needsHarvesting(tick: Int) {
        TODO("Not yet implemented")
    }

    override fun isBlooming(tick: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun animalAttackPenalty() {
        TODO("Not yet implemented")
    }

    override fun doAnimalAttack() {
        TODO("Not yet implemented")
    }

    override fun doBeeHappy() {
        TODO("Not yet implemented")
    }

    override fun applyPollinationBuff() {
        TODO("Not yet implemented")
    }

    override fun checkLateSowing() {
        if (sownTick - OAT_SOW_END <= 2) {
            lateActions.add(ActionType.SOW)
        }
    }

    override fun needsWeeding(tick: Int) {
        // TODO
    }

    override fun applyLateHarvestPenalty(tick: Int) {
        if (tick <= OAT_HARVEST_END) { // not late
            return
        } else if (tick - OAT_HARVEST_END > 2) { // more than 2 ticks late, set to 0
            this.harvestEstimate = 0
        } else { // %20 reduction per tick
            this.harvestEstimate = (PENALTY_POINT_EIGHT * this.harvestEstimate).toInt()
            }
        }
    }

    override fun applyLateSowingPenalty() {
        var counter = sownTick - OAT_SOW_END
        while (counter > 0) {
            this.harvestEstimate = (PENALTY_POINT_EIGHT * this.harvestEstimate).toInt()
            counter--
        }
    }

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = OAT_HARVEST
    }
}