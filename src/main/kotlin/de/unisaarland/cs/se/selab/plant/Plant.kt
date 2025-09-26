package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType

/**
 * custom mutable pair fot cutting and mowing times
 */
data class CustomPair(var first: Duration = Duration(-1, -1), var second: Boolean = false)

/**
 * abstract plant class
 */
abstract class Plant {
    abstract var harvestEstimate: Int
    abstract var neededMoisture: Int
    abstract var neededSunlight: Int
    abstract var harvestingTime: Duration
    abstract var bloomingTime: Duration?
    abstract var pollination: Double
    abstract var animalAttack: Boolean
    abstract var animalAttackPenalty: Double
    abstract val cuttingTime: MutableList<CustomPair>
    abstract val mowingTime: MutableList<CustomPair>

    /**
     * Default function to be overridden when necessary by the concrete plant
     * Returns false if the plant does not have a blooming phase
     */
    open fun isBlooming(tick: Int): Boolean {
        return false
    }

    /**
     * applies animal attack penalty at harvest estimation step
     */
    open fun animalAttackPenalty() { return }

    /**
     * updates animal attack penalty
     */
    open fun doAnimalAttack() { return }

    /**
     * updates the pollination buff
     */
    open fun doBeeHappy(effect: Double) { return }

    /**
     * applies bee happy effects at harvest estimation step
     */
    open fun applyPollinationBuff() { return }

    /**
     * sets harvest estimate to the initial value
     */
    open fun resetHarvestEstimate() { return }

    /**
     * update harvest estimate based on the late harvest penalty
     */
    open fun applyLateHarvestPenalty(tick: Int) { return }

    /**
     * check if the plant needs cutting in this tick
     */
    open fun needsCutting(tick: Int) { return }

    /**
     * check if the plant needs harvesting in this tick
     */
    open fun needsHarvesting(tick: Int) { return }

    /**
     * check if the plant needs mowing in this tick
     */
    open fun needsMowing(tick: Int) { return }

    /**
     * update harvest estimate based on missed cutting penalty
     */
    open fun applyCuttingPenalty() { return }

    /**
     * update harvest estimate based on missed mowing penalty
     */
    open fun applyMowingPenalty() { return }

    /**
     * update harvest estimate based on missed weeding penalty
     */
    open fun applyMissedWeedingPenalty() { return }

    /**
     * checks if the sown tick of the plant is in late period
     */
    open fun checkLateSowing() { return }

    /**
     * checks if the plant needs weeding in this tick
     */
    open fun needsWeeding(tick: Int) { return }

    /**
     * updates harvest estimate based on late sowing penalty
     */
    open fun applyLateSowingPenalty() { return }

    /**
     * resets the mowing time after animal attack
     */
    open fun resetMowingTime(startTick: Int) { return }

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
