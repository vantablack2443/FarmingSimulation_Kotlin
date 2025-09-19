package de.unisaarland.cs.se.selab.sowingplan

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.tile.Tile

class SowingPlan(
    private val id: Int,
    private val plant: PlantType,
    private val tick: Int,
    private val affectedTiles: List<Tile>
) {

    fun getSowingTiles(): List<Tile> = affectedTiles

    fun getPlant(): PlantType = plant

    fun getTick(): Int = tick
}
