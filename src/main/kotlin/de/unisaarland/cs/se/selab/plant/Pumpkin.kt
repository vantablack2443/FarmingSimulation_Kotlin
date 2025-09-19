package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration

const val PUMPKIN_SUNLIGHT = 120
const val PUMPKIN_MOISTURE = 600
const val PUMPKIN_HARVEST = 500000
const val PUMPKIN_SOW_START = 10
const val PUMPKIN_SOW_END = 12
const val PUMPKIN_HARVEST_START = 17
const val PUMPKIN_HARVEST_END = 20

/**
 * Pumpkin class
 */
class Pumpkin: FieldPlant() {
    override var neededSunlight = PUMPKIN_SUNLIGHT
    override var neededMoisture = PUMPKIN_MOISTURE
    override var harvestEstimate = PUMPKIN_HARVEST
    override var sowingTime: Duration = Duration(PUMPKIN_SOW_START, PUMPKIN_SOW_END)
    override var harvestingTime = Duration(PUMPKIN_HARVEST_START, PUMPKIN_HARVEST_END)

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
}