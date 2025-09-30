package de.unisaarland.cs.se.selab.harvestestimatehandler

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.incidents.FALLOW_DURATION
import de.unisaarland.cs.se.selab.log.Logger.logHarvestEstimate
import de.unisaarland.cs.se.selab.log.Logger.logMissedActions
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile
const val TWENTY_FIVE = 25
const val HUNDRED = 100
const val FIFTY = 50
const val PENALTY_POINT_NINE = 0.9
const val SIXTEEN = 16
const val FOUR = 4

/**
 * This class is responsible for estimating the harvest of all plantable tiles on the map.
 * It applies various penalties based on the conditions of the tiles and the actions taken (or not taken)
 * during the simulation.
 *
 */
class HarvestEstimateHandler(val simulationMap: SimulationMap) {

    /**
     * This function is called from the Simulation class. It iterates over all plantable tiles and
     * checks for each tile if there is a plant growing on it. Then it calls the respective harvest
     * estimate function based on the tile type (field or plantation).
     */
    fun estimateHarvest(yearTick: Int, simTick: Int) {
        val plantableTiles = simulationMap.getPlantableTiles()

        for (tile in plantableTiles) {
            if (!tile.hasPlantGrowing()) {
                continue
            }
            when (tile.category) {
                TileType.FIELD -> fieldHarvestEstimate(tile, simTick, yearTick)
                TileType.PLANTATION -> plantationHarvestEstimate(tile, yearTick)
                else -> error("Tile is not plantable")
            }
            // 1. Clear both actionList and ?lateList
            tile.actionsNeeded.clear()
            // Will retain HARVESTING if it exists to apply late harvesting at the start of the next tick
            // New change -> HARVESTING is never added to list, simulation calls it at the start of each tick
            tile.lateActions.clear()
        }
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a field tile.
     */
    fun fieldHarvestEstimate(t: Tile, simTick: Int, yearTick: Int) {
        val plantOfTile = t.plant ?: return

        plantOfTile.filterHarvestingIfNotMissed(yearTick, t.actionsNeeded)

        // Log missed actions if there are any -- need to verify this
        if (t.actionsNeeded.isNotEmpty()) {
            logMissedActions(t.id, t.actionsNeeded)
        }

        val anyActionApplied: Boolean = listOf(
            applyLateSowing(t),
            applySunlight(t),
            applyMoisture(t),
            applyMissedWeeding(t),
            applyLateHarvest(t, yearTick),

            // 2. Apply incidents then reset pollination and animal attack counters
            // Incidents go here
            applyAnimalAttack(t),
            applyBeeHappy(t),
            applyDrought(t)
        ).any { it }

        // For fields
        // If harvested, kill the plants so that the other components get what they expect
        // If drought, kill the plants .
        // If estimate 0 for any other reason, kill the plants .
        // If plants killed ,set fallow

        plantOfTile.pollination = 1.0
        plantOfTile.animalAttackPenalty = 1.0
        plantOfTile.animalAttack = false

        // Does not log change if harvested
        if (t.harvestedThisTick) {
            // Kill plants and set fallow
            t.plant = null
            t.currentCrop = null
            t.fallowDuration = Duration(simTick + 1, simTick + FALLOW_DURATION)
            // Set fallow

            // Set these to zero
            t.harvestedThisTick = false
            // IMPORTANT or else will always set estimate to 0
            t.droughtHit = false
            return
        }

        // If any change happened including drought, then log harvest including 0
        // Kill plants if estimate is 0 and set fallow
        if (anyActionApplied) {
            val crop = t.currentCrop ?: return
            logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, crop)

            // Set these to zero
            t.harvestedThisTick = false
            // IMPORTANT or else will always set estimate to 0
            t.droughtHit = false

            // Drought would also set this to 0, applyDrought() and harvesting
            if (plantOfTile.harvestEstimate == 0) {
                // Kill plants and set fallow
                t.plant = null
                t.currentCrop = null
                t.fallowDuration = Duration(simTick + 1, simTick + FALLOW_DURATION)
                // Set fallow
            }
        }
        // logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a plantation tile.
     */
    fun plantationHarvestEstimate(t: Tile, yearTick: Int) {
        val plantOfTile = t.plant ?: return

        plantOfTile.filterHarvestingIfNotMissed(yearTick, t.actionsNeeded)

        // Log missed actions if there are any -- need to verify this
        if (t.actionsNeeded.isNotEmpty()) {
            logMissedActions(t.id, t.actionsNeeded)
        }

        val anyActionApplied: Boolean = listOf(
            applySunlight(t),
            applyMoisture(t),
            // 3. Handle missed cutting period
            applyMissedCutting(t, yearTick),
            applyMissedMowing(t),
            applyLateHarvest(t, yearTick),

            // 2. Apply incidents then reset pollination and animal attack counters
            // Incidents go here
            applyAnimalAttack(t),
            applyBeeHappy(t),
            applyDrought(t)
        ).any { it }

        // For plantations
        // If harvested, don't kill the plants
        // If drought, kill the plants .
        // If estimate 0 for any other reason, don't kill plants .
        // No fallowing for plantations

        plantOfTile.pollination = 1.0
        plantOfTile.animalAttackPenalty = 1.0
        plantOfTile.animalAttack = false

        if (t.harvestedThisTick) {
            t.harvestedThisTick = false
            t.droughtHit = false
            return
        }

        // If any change occurred including drought, log and kill plant if drought
        if (anyActionApplied) {
            val crop = t.currentCrop ?: return
            logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, crop)

            t.harvestedThisTick = false

            // With drought the plants are killed
            if (t.droughtHit) {
                t.plant = null
                t.currentCrop = null
                // IMPORTANT or else will always set estimate to 0
                t.droughtHit = false
            }
        }

        // logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies the late sowing penalty on the tile plant's harvest estimate if there was a late sowing action.
     * Should only be applied per once per harvest cycle on the tick of sowing, if the plant was sown late
     */
    fun applyLateSowing(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: return false // null check first

        /*
        SOWING will only be added to late actions on the tick of sowing,
        after the sowingHandler calls checkLateSowing
         */
        if (ActionType.SOWING in t.lateActions) {
            acted = true
            t.lateActions.remove(ActionType.SOWING)
            plant.applyLateSowingPenalty()
        }

        /*val lateActions = t.plant?.lateActions ?: return

        if (lateActions.contains(ActionType.SOWING)) {
            lateActions.remove(ActionType.SOWING)
            (t.plant as FieldPlant).applyLateSowingPenalty()
        }*/
        return acted
    }

    /**
     * Applies the sunlight penalty on the tile plant's harvest estimate if the current sunlight
     * is more than the needed sunlight.
     */
    fun applySunlight(t: Tile): Boolean {
        var acted = false
        val neededSunlight = t.plant?.neededSunlight ?: error("Need sunlight needed")

        t.plant?.let { plant ->
            if (t.currentSunlight - neededSunlight >= TWENTY_FIVE) {
                acted = true
            }
            var currentSunlight = t.currentSunlight
            while (currentSunlight - neededSunlight >= TWENTY_FIVE) {
                plant.harvestEstimate = kotlin.math.floor(PENALTY_POINT_NINE * plant.harvestEstimate).toInt()
                currentSunlight -= TWENTY_FIVE
            }
        }

        /*while (t.currentSunlight - neededSunlight >= TWENTY_FIVE) {
            t.plant!!.harvestEstimate = kotlin.math.floor(PENALTY_POINT_NINE * t.plant!!.harvestEstimate).toInt()
            t.currentSunlight -= TWENTY_FIVE
        }*/
        return acted
    }

    /**
     * Applies the moisture penalty on the tile plant's harvest estimate if the current moisture
     * is less than the needed moisture.
     */
    fun applyMoisture(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: error("Need moisture needed")
        var currentMoisture = t.currentMoisture ?: error("Need moisture needed")

        if (currentMoisture == 0 && plant.harvestEstimate > 0) {
            plant.harvestEstimate = 0
            return true
        }

        if (plant.neededMoisture - currentMoisture >= HUNDRED) {
            acted = true
        }
        while (plant.neededMoisture - currentMoisture >= HUNDRED) {
            plant.harvestEstimate = maxOf(plant.harvestEstimate - FIFTY, 0)
            currentMoisture += HUNDRED
        }
        // Previous version
        /*val penaltyCounter = (plant.neededMoisture - currentMoisture) / HUNDRED
        plant.harvestEstimate -= FIFTY * penaltyCounter*/

        /*val neededMoisture = t.plant?.neededMoisture ?: error("Need moisture needed")

        if (t.currentMoisture == 0) {
            t.plant!!.harvestEstimate = 0
            return
        }

        val currentMoisture = t.currentMoisture ?: error("Need moisture needed")
        val penaltyCounter = (neededMoisture - currentMoisture) / HUNDRED
        t.plant!!.harvestEstimate -= FIFTY * penaltyCounter*/
        return acted
    }

    /**
     * Applies the weeding penalty for each missed weeding action on the field tile plant's harvest estimate.
     * Checks if actionsNeeded list contains WEEDING if so calls applyMissedWeedingPenalty
     */
    fun applyMissedWeeding(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: return false

        // Checks if WEEDING is in actionsNeeded to apply penalty
        while (ActionType.WEEDING in t.actionsNeeded) {
            acted = true
            t.actionsNeeded.remove(ActionType.WEEDING)
            plant.applyMissedWeedingPenalty()
        }

        // when we sow, we set the plant attribute on the tile right?
        /*val lateActions = t.plant?.lateActions ?: return

        while (lateActions.contains(ActionType.WEEDING)) {
            lateActions.remove(ActionType.WEEDING)
            (t.plant as FieldPlant).applyMissedWeedingPenalty()
        }*/
        return acted
    }

    /**
     * Applies the late harvest penalty for the late harvest action on the tile plant's harvest estimate.
     */
    fun applyLateHarvest(t: Tile, yearTick: Int): Boolean {
        /**
         *  estimateHarvest() would most likely need to include the yearTick parameter as well since
         *  applyLateHarvestPenalty needs it to determine how many times the penalty needs to be applied
         */
        val plant = t.plant ?: return false
        return plant.applyLateHarvestPenalty(yearTick)
    }

    /**
     * Applies the cutting penalty for a missed cutting action on the plantation tile plant's harvest estimate.
     */
    fun applyMissedCutting(t: Tile, yearTick: Int): Boolean {
        val plant = t.plant ?: return false
        val actionsNeeded = t.actionsNeeded

        if (ActionType.CUTTING !in actionsNeeded) {
            return false
        }

        actionsNeeded.remove(ActionType.CUTTING)

        val crop = t.currentCrop ?: return false
        val shouldApplyPenalty = when (crop) {
            PlantType.GRAPE -> yearTick == SIXTEEN
            PlantType.APPLE, PlantType.ALMOND, PlantType.CHERRY -> yearTick == FOUR
            else -> false
        }

        if (!shouldApplyPenalty) {
            return false
        }

        plant.applyCuttingPenalty()
        return true

        /*var acted = false
        val plant = t.plant ?: return false
        val actionsNeeded = t.actionsNeeded

        // Remove from list if its in there
        if (ActionType.CUTTING in actionsNeeded) {
            actionsNeeded.remove(ActionType.CUTTING)

            // Only apply cutting penalty if yearTick is the last cutting tick
            if (t.currentCrop == PlantType.GRAPE && yearTick != SIXTEEN) { return }
            if (t.currentCrop == PlantType.APPLE && yearTick != FOUR) { return }
            if (t.currentCrop == PlantType.ALMOND && yearTick != FOUR) { return }
            if (t.currentCrop == PlantType.CHERRY && yearTick != FOUR) { return }

            plant.applyCuttingPenalty()
        }

        *//*val lateActions = t.plant?.lateActions ?: return

        // no while loop since cutting can be done only once per harvest cycle
        if (lateActions.contains(ActionType.CUTTING)) {
            lateActions.remove(ActionType.CUTTING)
            (t.plant as PlantationPlant).applyCuttingPenalty()
        }*//*
        return acted*/
    }

    /**
     * Applies the mowing penalty for each missed mowing action on the plantation tile plant's harvest estimate.
     */
    fun applyMissedMowing(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: return false

        while (ActionType.MOWING in t.actionsNeeded) {
            acted = true
            t.actionsNeeded.remove(ActionType.MOWING)
            plant.applyMowingPenalty()
        }
        return acted
    }

    /**
     * Applies animal attack if animal attack counter > 1.0
     */
    fun applyAnimalAttack(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: return false
        if (plant.animalAttack) {
            acted = true
            plant.animalAttackPenalty()
        }
        return acted
    }

    /**
     * Applies bee happy pollination buff if pollination counter > 1.0
     */
    fun applyBeeHappy(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: return false
        if (plant.pollination > 1.0) {
            acted = true
            plant.applyPollinationBuff()
        }
        return acted
    }

    /**
     * Sets harvest estimate to 0 after drought
     */
    fun applyDrought(t: Tile): Boolean {
        var acted = false
        val plant = t.plant ?: return false
        if (t.droughtHit) {
            plant.harvestEstimate = 0
            acted = true
        }
        return acted
    }

    /**
     * Helper function for fieldHarvestEstimate returning the corresponding plant accordingly to given plantType,
     * since we cannot directly get the plant from the tile in the case where it is a field.
     * WHAT IS THE POINT OF THIS FUNCTION
     */
    /*private fun getPlantFromType(plantType: PlantType?): Plant {
        return when (plantType) {
            PlantType.POTATO -> Potato()
            PlantType.WHEAT -> Wheat()
            PlantType.OAT -> Oat()
            PlantType.PUMPKIN -> Pumpkin()
            null -> throw IllegalArgumentException("Plant type is null")
            else -> throw IllegalArgumentException("Unknown plant type: $plantType")
        }
    }*/
}
