package de.unisaarland.cs.se.selab.harvestestimatehandler

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger.logHarvestEstimate
import de.unisaarland.cs.se.selab.log.Logger.logMissedActions
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Oat
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plant.Potato
import de.unisaarland.cs.se.selab.plant.Pumpkin
import de.unisaarland.cs.se.selab.plant.Wheat
import de.unisaarland.cs.se.selab.tile.Tile

class HarvestEstimateHandler(val simulationMap: SimulationMap) {

    /**
     * This function is called from the Simulation class. It iterates over all plantable tiles and
     * checks for each tile if there is a plant growing on it. Then it calls the respective harvest
     * estimate function based on the tile type (field or plantation).
     */
    fun estimateHarvest() {
        val plantableTiles = simulationMap.getPlantableTiles()

        for (tile in plantableTiles) {
            if (!tile.hasPlantGrowing()) {
                continue
            }
            when (tile.category) {
                TileType.FIELD -> fieldHarvestEstimate(tile)
                TileType.PLANTATION -> plantationHarvestEstimate(tile)
                else -> throw IllegalStateException("Tile is not plantable")
            }
        }
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a field tile.
     */
    fun fieldHarvestEstimate(t: Tile) {
        val plantOfTile = getPlantFromType(t.currentCrop)
        logMissedActions(t.id, plantOfTile.lateActions)

        applyLateSowing(t)
        applySunlight(t)
        applyMoisture(t)
        applyMissedWeeding(t)
        applyLateHarvest(t)

        logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * Applies all relevant penalties to the harvest estimate of a plantation tile.
     */
    fun plantationHarvestEstimate(t: Tile) {
        logMissedActions(t.id, t.plant?.lateActions ?: emptyList())

        applySunlight(t)
        applyMoisture(t)
        applyMissedCutting(t)
        applyMissedMowing(t)
        applyLateHarvest(t)

        logHarvestEstimate(t.id, t.plant?.harvestEstimate ?: 0, t.currentCrop!!)
    }

    /**
     * TODO
     */
    fun applyLateSowing(t: Tile) {
        TODO()
    }

    /**
     * Applies the sunlight penalty on the tile plant's harvest estimate if the current sunlight
     * is more than the needed sunlight.
     */
    fun applySunlight(t: Tile) {
        val neededSunlight = t.plant?.neededSunlight ?: throw IllegalStateException("Tile has no plant")

        while (t.currentSunlight - neededSunlight >= 25) {
            t.plant!!.harvestEstimate = kotlin.math.floor(0.9 * t.plant!!.harvestEstimate).toInt()
            t.currentSunlight -= 25
        }
    }

    /**
     * Applies the moisture penalty on the tile plant's harvest estimate if the current moisture
     * is less than the needed moisture.
     */
    fun applyMoisture(t: Tile) {
        val neededMoisture = t.plant?.neededMoisture ?: throw IllegalStateException("Tile has no plant")

        if (t.currentMoisture == 0) {
            t.plant!!.harvestEstimate = 0
            return
        }

        t.plant!!.harvestEstimate -= 50 * ((neededMoisture - t.currentMoisture?.toInt()) / 100)
        }


    /**
     * TODO
     */
    fun applyMissedWeeding(t: Tile) {
        TODO()
    }

    /**
     * TODO
     */
    fun applyLateHarvest(t: Tile) {
        TODO()
    }

    /**
     * TODO
     */
    fun applyMissedCutting(t: Tile) {
        TODO()
    }

    /**
     * TODO
     */
    fun applyMissedMowing(t: Tile) {
        TODO()
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
