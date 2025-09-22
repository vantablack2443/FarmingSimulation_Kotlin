package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType

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

    /**
     * Default function to be overridden when necessary by the concrete plant
     * Returns false if the plant does not have a blooming phase
     */
    open fun isBlooming(tick: Int): Boolean {
        return false
    }

    abstract fun animalAttackPenalty(): Unit
    abstract fun doAnimalAttack(): Unit
    abstract fun doBeeHappy(effect: Int): Unit
    abstract fun applyPollinationBuff(): Unit
    abstract fun resetHarvestEstimate(): Unit
    abstract fun applyLateHarvestPenalty(tick: Int): Unit

    /**
     * Factory method to create plants based on PlantType
     * Used by sowing handler to create a plant based on plantType
     */
    companion object {
        /**
         * Creates a Plant instance based on the provided PlantType.
         */
        fun createPlant(plantType: PlantType): Plant {
            return when (plantType) {
                PlantType.POTATO -> Potato()
                PlantType.WHEAT -> Wheat()
                PlantType.OAT -> Oat()
                PlantType.PUMPKIN -> Pumpkin()
                PlantType.APPLE -> Apple()
                PlantType.ALMOND -> Almond()
                PlantType.CHERRY -> Cherry()
                PlantType.GRAPE -> Grape()
            }
        }
    }
}
