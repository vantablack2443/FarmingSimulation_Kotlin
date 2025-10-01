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
    abstract val pollination: MutableList<Double>
    abstract var animalAttack: Boolean

    abstract val animalAttackPenalty: MutableList<Double>

//    abstract var animalAttackPenalty: Double
    abstract val cuttingTime: MutableList<CustomPair>
    abstract val mowingTime: MutableList<CustomPair>

    /**
     * Default function to be overridden when necessary by the concrete plant
     * Returns false if the plant does not have a blooming phase
     * Plants that bloom override this function
     * simTick for Plantations, yearTick for Fields
     */
    open fun isBlooming(tick: Int): Boolean {
        return false
    }

    /**
     * Applies animal attack penalty at harvest estimation step
     * Overridden in FieldPlants and Grapes
     */
    open fun animalAttackPenalty() { return }

    /**
     * updates animal attack penalty
     * overridden in FieldPlant and Grape
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
     * applied per tick
     */
    open fun applyLateHarvestPenalty(yearTick: Int): Boolean { return false }

    /**
     * check if the plant needs cutting in this tick
     * If CUTTING is in the actionsNeeded list, harvestEstimator will call applyCuttingPenalty()
     */
    open fun needsCutting(yearTick: Int, actionsNeeded: MutableList<ActionType>) { return }

    /**
     * check if the plant needs harvesting in this tick
     * Adds HARVESTING to the actions needed list
     * Wont add HARVESTING to lateActions list since this is applied at the start of the tick by Simulation
     */
    open fun needsHarvesting(
        yearTick: Int,
        actionsNeeded: MutableList<ActionType>
    ) { return }

    /**
     * check if the plant needs mowing in this tick
     * IF MOWING is in the actionsNeeded list HarvestEstimator will applyMowingPenalty
     */
    open fun needsMowing(yearTick: Int, actionsNeeded: MutableList<ActionType>) { return }

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
     * HarvestEstimator checks if the WEEDING exists in actionsNeeded if so calls this function
     */
    open fun applyMissedWeedingPenalty() { return }

    /**
     * checks if the sown tick of the plant is in late period
     * Called by sowingHandler at point of sowing. Will add SOWING to lateActions list
     */
    open fun checkLateSowing(lateActions: MutableList<ActionType>, yearTickSown: Int) { return }

    /**
     * checks if the plant needs weeding in this tick
     * Adds WEEDING to actionsNeeded list
     */
    open fun needsWeeding(simTick: Int, actionsNeeded: MutableList<ActionType>) { return }

    /**
     * updates harvest estimate based on late sowing penalty
     * If SOWING not in actionsNeeded but SOWING in lateActions, harvestEstimator calls this function
     */
    open fun applyLateSowingPenalty() { return }

    /**
     * resets the mowing time after animal attack
     */
    open fun resetMowingTime(startTick: Int) { return }

    /**
     * Sets sowing sim tick and year tick in field plants
     */
    open fun setSowingTime(sownSimTick: Int, sownYearTick: Int) { return }

    /**
     * Filters out HARVESTING from actions needed list if we are within all but the last tick of the
     * harvesting period
     */
    open fun filterHarvestingIfNotMissed(yearTick: Int, actionsNeeded: MutableList<ActionType>) { return }

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
