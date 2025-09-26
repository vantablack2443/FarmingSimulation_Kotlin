package de.unisaarland.cs.se.selab.harvestestimatehandler

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
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
    fun estimateHarvest(yearTick: Int) {
        val plantableTiles = simulationMap.getPlantableTiles()

        for (tile in plantableTiles) {
            if (!tile.hasPlantGrowing()) {
                continue
            }
            when (tile.category) {
                TileType.FIELD -> fieldHarvestEstimate(tile, yearTick)
                TileType.PLANTATION -> plantationHarvestEstimate(tile, yearTick)
                else -> error("Tile is not plantable")
            }
            // 1. Clear both actionList and ?lateList
            tile.actionsNeeded.clear()
            // Will retain HARVESTING if it exists to apply late harvesting at the start of the next tick
            tile.lateActions.retainAll { it == ActionType.HARVESTING }
        }
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a field tile.
     */
    fun fieldHarvestEstimate(t: Tile, yearTick: Int) {
        val plantOfTile = t.plant
        logMissedActions(t.id, t.actionsNeeded)

        applyLateSowing(t)
        applySunlight(t)
        applyMoisture(t)
        applyMissedWeeding(t)
        applyLateHarvest(t, yearTick)

        // 2. Apply incidents then reset pollination and animal attack counters
        // Incidents go here
        applyAnimalAttack(t)
        applyBeeHappy(t)
        plantOfTile?.pollination = 1.0
        plantOfTile?.animalAttackPenalty = 1.0
        plantOfTile?.animalAttack = false

        val crop = t.currentCrop ?: return
        logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, crop)
        // logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a plantation tile.
     */
    fun plantationHarvestEstimate(t: Tile, yearTick: Int) {
        val plantOfTile = t.plant
        logMissedActions(t.id, t.actionsNeeded)

        applySunlight(t)
        applyMoisture(t)
        // 3. Handle missed cutting period
        applyMissedCutting(t, yearTick)
        applyMissedMowing(t)
        applyLateHarvest(t, yearTick)

        // 2. Apply incidents then reset pollination and animal attack counters
        // Incidents go here
        applyAnimalAttack(t)
        applyBeeHappy(t)
        plantOfTile?.pollination = 1.0
        plantOfTile?.animalAttackPenalty = 1.0
        plantOfTile?.animalAttack = false

        val crop = t.currentCrop ?: return
        logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, crop)
        // logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies the late sowing penalty on the tile plant's harvest estimate if there was a late sowing action.
     * Should only be applied per once per harvest cycle on the tick of sowing, if the plant was sown late
     */
    fun applyLateSowing(t: Tile) {
        val plant = t.plant ?: return // null check first

        /*
        SOWING will only be added to late actions on the tick of sowing,
        after the sowingHandler calls checkLateSowing
         */
        if (ActionType.SOWING in t.lateActions) {
            t.lateActions.remove(ActionType.SOWING)
            plant.applyLateSowingPenalty()
        }

        /*val lateActions = t.plant?.lateActions ?: return

        if (lateActions.contains(ActionType.SOWING)) {
            lateActions.remove(ActionType.SOWING)
            (t.plant as FieldPlant).applyLateSowingPenalty()
        }*/
    }

    /**
     * Applies the sunlight penalty on the tile plant's harvest estimate if the current sunlight
     * is more than the needed sunlight.
     */
    fun applySunlight(t: Tile) {
        val neededSunlight = t.plant?.neededSunlight ?: error("Need sunlight needed")

        t.plant?.let { plant ->
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
    }

    /**
     * Applies the moisture penalty on the tile plant's harvest estimate if the current moisture
     * is less than the needed moisture.
     */
    fun applyMoisture(t: Tile) {
        val plant = t.plant ?: error("Need moisture needed")
        var currentMoisture = t.currentMoisture ?: error("Need moisture needed")

        if (currentMoisture == 0) {
            plant.harvestEstimate = 0
            return
        }

        while (plant.neededMoisture - currentMoisture >= HUNDRED) {
            plant.harvestEstimate -= FIFTY
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
    }

    /**
     * Applies the weeding penalty for each missed weeding action on the field tile plant's harvest estimate.
     * Checks if actionsNeeded list contains WEEDING if so calls applyMissedWeedingPenalty
     */
    fun applyMissedWeeding(t: Tile) {
        val plant = t.plant ?: return

        // Checks if WEEDING is in actionsNeeded to apply penalty
        while (ActionType.WEEDING in t.actionsNeeded) {
            t.actionsNeeded.remove(ActionType.WEEDING)
            plant.applyMissedWeedingPenalty()
        }

        // when we sow, we set the plant attribute on the tile right?
        /*val lateActions = t.plant?.lateActions ?: return

        while (lateActions.contains(ActionType.WEEDING)) {
            lateActions.remove(ActionType.WEEDING)
            (t.plant as FieldPlant).applyMissedWeedingPenalty()
        }*/
    }

    /**
     * Applies the late harvest penalty for the late harvest action on the tile plant's harvest estimate.
     */
    fun applyLateHarvest(t: Tile, yearTick: Int) {
        /**
         *  estimateHarvest() would most likely need to include the yearTick parameter as well since
         *  applyLateHarvestPenalty needs it to determine how many times the penalty needs to be applied
         */
        val plant = t.plant ?: return
        val lateActions = t.lateActions

        if (ActionType.HARVESTING in lateActions) {
            lateActions.remove(ActionType.HARVESTING)
            plant.applyLateHarvestPenalty(yearTick)
        }

        /*val lateActions = t.plant?.lateActions ?: return

        if (lateActions.contains(ActionType.HARVESTING)) {
            lateActions.remove(ActionType.HARVESTING)
            t.plant?.applyLateHarvestPenalty(yearTick)
        }*/
    }

    /**
     * Applies the cutting penalty for a missed cutting action on the plantation tile plant's harvest estimate.
     */
    fun applyMissedCutting(t: Tile, yearTick: Int) {
        val plant = t.plant ?: return
        val actionsNeeded = t.actionsNeeded

        if (t.currentCrop == PlantType.GRAPE && yearTick != SIXTEEN) { return }
        if (t.currentCrop == PlantType.APPLE && yearTick != FOUR) { return }
        if (t.currentCrop == PlantType.ALMOND && yearTick != FOUR) { return }
        if (t.currentCrop == PlantType.CHERRY && yearTick != FOUR) { return }

        if (ActionType.CUTTING in actionsNeeded) {
            actionsNeeded.remove(ActionType.CUTTING)
            plant.applyCuttingPenalty()
        }

        /*val lateActions = t.plant?.lateActions ?: return

        // no while loop since cutting can be done only once per harvest cycle
        if (lateActions.contains(ActionType.CUTTING)) {
            lateActions.remove(ActionType.CUTTING)
            (t.plant as PlantationPlant).applyCuttingPenalty()
        }*/
    }

    /**
     * Applies the mowing penalty for each missed mowing action on the plantation tile plant's harvest estimate.
     */
    fun applyMissedMowing(t: Tile) {
        val plant = t.plant ?: return

        while (ActionType.MOWING in t.actionsNeeded) {
            t.actionsNeeded.remove(ActionType.MOWING)
            plant.applyMowingPenalty()
        }
    }

    /**
     * Applies animal attack if animal attack counter > 1.0
     */
    fun applyAnimalAttack(t: Tile) {
        val plant = t.plant ?: return
        if (plant.animalAttackPenalty > 1.0) {
            plant.animalAttackPenalty()
        }
    }

    /**
     * Applies bee happy pollination buff if pollination counter > 1.0
     */
    fun applyBeeHappy(t: Tile) {
        val plant = t.plant ?: return
        if (plant.pollination > 1.0) {
            plant.applyPollinationBuff()
        }
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
