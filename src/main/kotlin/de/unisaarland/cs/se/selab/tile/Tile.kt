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

    /**
     * checks if the tile is sowable with the given plant in the given year
     */
    fun isSowable(plant: PlantType, yearTick: Int): Boolean {
        // TODO
        return possiblePlants?.contains(plant) == true && yearTick > 0
    }

    /**
     * checks if the plant can be harvested from the tile
     */
    fun isHarvestable(plant: PlantType): Boolean {
        // TODO
        return possiblePlants?.contains(plant) == false
    }

    /**
     * increases the current moisture
     */
    fun increaseMoistureByAmount(amount: Int) {
        val newMoisture = currentMoisture?.plus(amount)
        if (newMoisture != null) {
            maxMoisture?.let {
                if (newMoisture > it) currentMoisture = maxMoisture
                return
            }
            currentMoisture = newMoisture
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
     * gets the actions needed list from the plant
     */
    fun getActions(): List<ActionType> {
        TODO()
    }

    /**
     * gets the late actions  list from the plant
     */
    fun getLateActions(): List<ActionType> {
        TODO()
    }

    /**
     * returns the plant if the tile has one
     */
//    fun getPlant(): Plant {
//        TODO()
//    }

    /**
     * perform harvest
     */
    fun harvest() {
        TODO()
    }

    /**
     * checks if the tile needs irrigation
     */
    fun needsIrrigation(): Boolean {
        return if (plant == null) {
            false
        } else {
            currentMoisture!! < plant!!.neededMoisture
        }
    }

    /**
     * checks if mowing needed
     */
    fun requiresMowing(): Boolean {
        TODO()
    }

    /**
     * checks if weeding required
     */
    fun requiresWeeding(): Boolean {
        val actionsNeeded = this.plant?.actionsNeeded
        if (actionsNeeded == null) {
            return false
        } else {
            return ActionType.WEED in actionsNeeded
        }
    }

    /**
     * checks if cutting required
     */
    fun requiresCutting(): Boolean {
        val actionsNeeded = this.plant?.actionsNeeded
        if (actionsNeeded == null) {
            return false
        } else {
            return ActionType.MOW in actionsNeeded
        }
    }

    /**
     * checks if there is a plant on the tile
     */
    fun hasPlantGrowing(): Boolean {
        if (this.category == TileType.FIELD) {
            return plant != null
        }
        if (this.category == TileType.PLANTATION) {
            return plantationDamaged == false
        }
        return false
    }

    /**
     * check if the tile belongs to the given farm
     */
    fun isOwnedBY(farmID: Int): Boolean {
        return farmID == this.farmID
    }

    /**
     * setter for the current sunlight amount
     */
    fun setSunlight(sunlight: Int) {
        this.currentSunlight = sunlight
    }
}
