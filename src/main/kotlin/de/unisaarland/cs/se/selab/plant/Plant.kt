package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType

abstract class Plant(
    var harvestEstimate : Int,
    var neededMoisture : Int,
    var harvestingTime : Duration,
    var bloomingTime : Duration?,
    var pollination : Int,
    var animalAttack : Boolean,
    var actionsNeeded : MutableList<ActionType>,
    var lateActions : MutableList<ActionType>
    ) {
    abstract fun needsHarvesting(tick : Int) : Unit
    abstract fun isBlooming(tick : Int) : Boolean
    abstract fun animalAttackPenalty() : Unit
    abstract fun doAnimalAttack() : Unit
    abstract fun doBeeHappy() : Unit
    abstract fun applyPollinationBuff() : Unit
}