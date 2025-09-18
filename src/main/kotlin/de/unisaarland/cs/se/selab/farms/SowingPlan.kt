package de.unisaarland.cs.se.selab.farms

class SowingPlan (
    private val id : Int,
    private val plant : PlantType,
    private val tick : Int,
    private val affectedTiles : List<Tile>
) {

    fun getSowingTiles() : List<Tile> = affectedTiles

    fun getPlant() : PlantType = plant

    fun getTick() : Int = tick

}