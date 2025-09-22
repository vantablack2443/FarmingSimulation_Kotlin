package de.unisaarland.cs.se.selab.harvestestimatehandler

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger.logHarvestEstimate
import de.unisaarland.cs.se.selab.log.Logger.logMissedActions
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.Oat
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.Potato
import de.unisaarland.cs.se.selab.plant.Pumpkin
import de.unisaarland.cs.se.selab.plant.Wheat
import de.unisaarland.cs.se.selab.tile.Tile
const val TWENTY_FIVE = 25
const val HUNDRED = 100
const val FIFTY = 50
const val PENALTY_POINT_NINE = 0.9

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
        }
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a field tile.
     */
    fun fieldHarvestEstimate(t: Tile, yearTick: Int) {
        val plantOfTile = getPlantFromType(t.currentCrop)
        logMissedActions(t.id, plantOfTile.lateActions)

        applyLateSowing(t)
        applySunlight(t)
        applyMoisture(t)
        applyMissedWeeding(t)
        applyLateHarvest(t, yearTick)

        logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a plantation tile.
     */
    fun plantationHarvestEstimate(t: Tile, yearTick: Int) {
        logMissedActions(t.id, t.plant?.lateActions.orEmpty())

        applySunlight(t)
        applyMoisture(t)
        applyMissedCutting(t)
        applyMissedMowing(t)
        applyLateHarvest(t, yearTick)

        logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies the late sowing penalty on the tile plant's harvest estimate if there was a late sowing action.
     */
    fun applyLateSowing(t: Tile) {
        val lateActions = t.plant?.lateActions ?: return

        if (lateActions.contains(ActionType.SOW)) {
            lateActions.remove(ActionType.SOW)
            (t.plant as FieldPlant).applyLateSowingPenalty()
        }
    }

    /**
     * Applies the sunlight penalty on the tile plant's harvest estimate if the current sunlight
     * is more than the needed sunlight.
     */
    fun applySunlight(t: Tile) {
        val neededSunlight = t.plant?.neededSunlight ?: error("Need sunlight needed")

        while (t.currentSunlight - neededSunlight >= TWENTY_FIVE) {
            t.plant!!.harvestEstimate = kotlin.math.floor(PENALTY_POINT_NINE * t.plant!!.harvestEstimate).toInt()
            t.currentSunlight -= TWENTY_FIVE
        }
    }

    /**
     * Applies the moisture penalty on the tile plant's harvest estimate if the current moisture
     * is less than the needed moisture.
     */
    fun applyMoisture(t: Tile) {
        val neededMoisture = t.plant?.neededMoisture ?: error("Need moisture needed")

        if (t.currentMoisture == 0) {
            t.plant!!.harvestEstimate = 0
            return
        }

        val currentMoisture = t.currentMoisture ?: error("Need moisture needed")
        val penaltyCounter = (neededMoisture - currentMoisture) / HUNDRED
        t.plant!!.harvestEstimate -= FIFTY * penaltyCounter
    }

    /**
     * Applies the weeding penalty for each missed weeding action on the field tile plant's harvest estimate.
     */
    fun applyMissedWeeding(t: Tile) {
        // when we sow, we set the plant attribute on the tile right?
        val lateActions = t.plant?.lateActions ?: return

        while (lateActions.contains(ActionType.WEED)) {
            lateActions.remove(ActionType.WEED)
            (t.plant as FieldPlant).applyMissedWeedingPenalty()
        }
    }

    /**
     * Applies the late harvest penalty for the late harvest action on the tile plant's harvest estimate.
     */
    fun applyLateHarvest(t: Tile, yearTick: Int) {
        /**
         *  estimateHarvest() would most likely need to include the yearTick parameter as well since
         *  applyLateHarvestPenalty needs it to determine how many times the penalty needs to be applied
         */
        val lateActions = t.plant?.lateActions ?: return

        if (lateActions.contains(ActionType.HARVEST)) {
            lateActions.remove(ActionType.HARVEST)
            t.plant?.applyLateHarvestPenalty(yearTick)
        }
    }

    /**
     * Applies the cutting penalty for a missed cutting action on the plantation tile plant's harvest estimate.
     */
    fun applyMissedCutting(t: Tile) {
        val lateActions = t.plant?.lateActions ?: return

        // no while loop since cutting can be done only once per harvest cycle
        if (lateActions.contains(ActionType.CUT)) {
            lateActions.remove(ActionType.CUT)
            (t.plant as PlantationPlant).applyCuttingPenalty()
        }
    }

    /**
     * Applies the mowing penalty for each missed mowing action on the plantation tile plant's harvest estimate.
     */
    fun applyMissedMowing(t: Tile) {
        val lateActions = t.plant?.lateActions ?: return

        while (lateActions.contains(ActionType.MOW)) {
            lateActions.remove(ActionType.MOW)
            (t.plant as PlantationPlant).applyMowingPenalty()
        }
    }

    /**
     * Helper function for fieldHarvestEstimate returning the corresponding plant accordingly to given plantType,
     * since we cannot directly get the plant from the tile in the case where it is a field.
     */
    private fun getPlantFromType(plantType: PlantType?): Plant {
        return when (plantType) {
            PlantType.POTATO -> Potato()
            PlantType.WHEAT -> Wheat()
            PlantType.OAT -> Oat()
            PlantType.PUMPKIN -> Pumpkin()
            else -> throw IllegalArgumentException("Unknown plant type: $plantType")
        }
    }
}
