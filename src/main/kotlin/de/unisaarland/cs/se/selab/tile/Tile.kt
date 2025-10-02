package de.unisaarland.cs.se.selab.tile

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.plant.Plant

/**
 * tile class
 */
class Tile(
    val id: Int,
    val location: Coordinate,
    var category: TileType,
    val shape: TileShape
) {
    var currentCrop: PlantType? = null
    var airflow: Boolean? = null
    var direction: Direction? = null
    var shed: Boolean? = null
    var possiblePlants: List<PlantType>? = mutableListOf()
    var maxMoisture: Int? = null
    var currentMoisture: Int? = null
    var currentSunlight: Int = 0
    var plant: Plant? = null
    var farmID: Int? = null
    var plantationDamaged: Boolean? = null
    var fallowDuration: Duration? = null

    val actionsNeeded: MutableList<ActionType> = mutableListOf()
    val lateActions: MutableList<ActionType> = mutableListOf()

    var droughtHit: Boolean = false
    var harvestedThisTick: Boolean = false

    /**
     * Only checks if the tile is in fallow duration and can sow the specific plant
     * to check if a plant is sowable in current tick use plantdata.getSowablePlants
     */
    fun isSowable(plant: PlantType, simTick: Int): Boolean {
        val pp = possiblePlants ?: return false
        val fd = fallowDuration
        if (pp.contains(plant)) {
            if (fd == null || !fd.inRange(simTick)) return true
        }
        return false
    }

    /**
     * checks if the plant can be harvested from the tile
     */
//    fun isHarvestable(plant: PlantType): Boolean {
//        TODO()
//    }

    /**
     * increases the current moisture
     */
    fun increaseMoistureByAmount(amount: Int) {
        val newMoisture = currentMoisture?.plus(amount)
        if (newMoisture != null && maxMoisture != null) {
            /*
            maxMoisture?.let {
                if (newMoisture > it) currentMoisture = maxMoisture
                return
            }*/
            currentMoisture = minOf(newMoisture, maxMoisture ?: 0)
        }
    }

    /**
     * decreases the current moisture by given amount
     */
    fun decreaseMoistureByAmount(amount: Int) {
        val newMoisture = currentMoisture?.minus(amount)
        if (newMoisture != null) {
            if (newMoisture < 0) {
                currentMoisture = 0
                return
            }
            currentMoisture = newMoisture
        }
    }

    /**
     * checks if the tile needs irrigation and adds action to the list if required.
     * If plantation is damaged, the plant attribute is set to null by the incident
     *
     * This function will be called by Farm for both fields and plantations.
     * It will only be called for fields if the plant exists
     * It will be called for plantations when it hasn't been hit by drought.
     */
    fun needsIrrigation() {
        val p = plant ?: return
        val cm = currentMoisture ?: return

        if (plantationDamaged == true) {
            return
        }

        // check if the estimate is greater than 0
//        if (plant?.harvestEstimate == 0) {
//            return
//        }

        // Only required if current moisture is less than needed moisture
        if (cm < p.neededMoisture) {
            actionsNeeded.add(ActionType.IRRIGATING)
        }
    }

/**
     * checks if there is a plant on the tile
     */
    fun hasPlantGrowing(): Boolean {
        if (this.category == TileType.FIELD || this.category == TileType.PLANTATION) {
            return plant != null
        }
        return false
    }

/**
     * setter for the current sunlight amount
     */
    fun setSunlight(sunlight: Int) {
        this.currentSunlight = sunlight
    }
}
