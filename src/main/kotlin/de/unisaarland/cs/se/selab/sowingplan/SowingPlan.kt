package de.unisaarland.cs.se.selab.sowingplan

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * SowingPlan data class
 */
class SowingPlan(
    private val id: Int,
    private val plant: PlantType,
    private val tick: Int,
    private val affectedTiles: List<Tile>
) {
    /**
     * Get id
     */
    fun getId(): Int = id

    /**
     * Get affected tiles
     */
    fun getSowingTiles(): List<Tile> = affectedTiles

    /**
     * Get plant
     */
    fun getPlant(): PlantType = plant

    /**
     * Get tick
     */
    fun getTick(): Int = tick
}
