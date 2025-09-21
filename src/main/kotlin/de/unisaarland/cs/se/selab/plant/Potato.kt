package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.plantdata.POTATO_HARVEST

const val POTATO_SUNLIGHT = 130
const val POTATO_MOISTURE = 500
const val POTATO_SOW_START = 7
const val POTATO_SOW_END = 10
const val POTATO_HARVEST_START = 17
const val POTATO_HARVEST_END = 20

/**
 * Potato class
 */
class Potato : FieldPlant() {
    override var neededSunlight = POTATO_SUNLIGHT
    override var neededMoisture = POTATO_MOISTURE
    override var harvestEstimate = POTATO_HARVEST
    override var sowingTime: Duration = Duration(POTATO_SOW_START, POTATO_SOW_END)
    override var harvestingTime: Duration = Duration(POTATO_HARVEST_START, POTATO_HARVEST_END)

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

    /**
     * reset harvest estimate
     */
    override fun resetHarvestEstimate() {
        this.harvestEstimate = POTATO_HARVEST
    }
}
