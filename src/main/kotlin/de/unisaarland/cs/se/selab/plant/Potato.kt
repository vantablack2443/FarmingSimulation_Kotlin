package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType

class Potato(harvestEstimate: Int, neededMoisture: Int, harvestingTime: Duration, bloomingTime: Duration?,
             pollination: Int, animalAttack: Boolean, actionsNeeded: MutableList<ActionType>,
             lateActions: MutableList<ActionType>
) : FieldPlant(harvestEstimate, neededMoisture,
    harvestingTime,
    bloomingTime, pollination, animalAttack, actionsNeeded, lateActions
) {
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