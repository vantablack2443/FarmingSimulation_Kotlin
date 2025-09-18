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
    val category: TileType,
    val shape: TileShape,
    val airflow: Boolean?,
    val direction: Direction?,
    val shed: Boolean?,
    val possiblePlants: List<PlantType>?,
    val maxMoisture: Int?
) {
    var currentMoisture: Int? = null
    var currentSunlight: Int = 0
    var plant: Plant? = null
    val farmID: Int? = null
    var plantationDamaged: Boolean? = null
    var fallowDuration: Duration? = null

    /**
     * checks if the tile is sowable with the given plant in the given year
     */
    fun isSowable(plant: PlantType, yearTick: Int): Boolean {
        TODO()
    }

    /**
     * checks if the plant can be harvested from the tile
     */
    fun isHarvestable(plant: PlantType): Boolean {
        TODO()
    }

    /**
     * increases the current moisture
     */
    fun increaseMoistureByAmount(amount: Int) {
        TODO()
    }

    /**
     * decreases the current moisture
     */
    fun decreaseMoistureByAmount(amount: Int) {
        TODO()
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
    fun getPlant(): Plant {
        TODO()
    }

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
        TODO()
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
        TODO()
    }

    /**
     * checks if cutting required
     */
    fun requiresCutting(): Boolean {
        TODO()
    }

    /**
     * checks if there is a plant on the tile
     */
    fun hasPlantGrowing(): Boolean {
        TODO()
    }

    /**
     * check if the tile belongs to the given farm
     */
    fun isOwnedBY(farmID: Int): Boolean {
        TODO()
    }
}
