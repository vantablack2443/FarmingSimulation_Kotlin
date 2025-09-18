package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType

/**
 * abstract plant class
 */
abstract class Plant {
    open var harvestEstimate: Int = -1
    open var neededMoisture: Int = -1
    open var neededSunlight: Int = -1
    open var harvestingTime: Duration = Duration(-1, -1)
    open var bloomingTime: Duration? = null
    open var pollination: Int = 0
    open var animalAttack: Boolean = false
    open var actionsNeeded: MutableList<ActionType> = mutableListOf()
    open var lateActions: MutableList<ActionType> = mutableListOf()
    abstract fun needsHarvesting(tick: Int): Unit
    abstract fun isBlooming(tick: Int): Boolean
    abstract fun animalAttackPenalty(): Unit
    abstract fun doAnimalAttack(): Unit
    abstract fun doBeeHappy(): Unit
    abstract fun applyPollinationBuff(): Unit
}