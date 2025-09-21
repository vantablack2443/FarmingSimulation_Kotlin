package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.plantdata.OAT_HARVEST

const val OAT_SUNLIGHT = 90
const val OAT_MOISTURE = 300
const val OAT_SOW_START = 6
const val OAT_SOW_END = 6
const val OAT_HARVEST_START = 13
const val OAT_HARVEST_END = 16

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
}