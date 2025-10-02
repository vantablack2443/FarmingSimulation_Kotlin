package de.unisaarland.cs.se.selab.tile

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.plant.Plant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * tests basic functions in the tile class
 */
class TileTest {
    val tiles = mutableListOf<Tile>()

    @BeforeEach
    fun setUp() {
        tiles.addAll(
            mutableListOf(
                Tile(1, Coordinate(5, 1), TileType.FARMSTEAD, TileShape.SQUARE),
                Tile(2, Coordinate(6, 2), TileType.PLANTATION, TileShape.OCTAGONAL),
                Tile(3, Coordinate(6, 4), TileType.FIELD, TileShape.OCTAGONAL),
                Tile(4, Coordinate(8, 2), TileType.FIELD, TileShape.OCTAGONAL)
            )
        )
        tiles.forEach { it.currentMoisture = 100 }
        tiles.forEach { it.maxMoisture = 250 }
        tiles[1].currentCrop = PlantType.CHERRY
        tiles[1].plant = Plant.createPlant(PlantType.CHERRY)
        tiles[2].currentCrop = PlantType.WHEAT
        tiles[2].plant = Plant.createPlant(PlantType.WHEAT)
    }

    @Test
    fun testTileFunctions() {
        tiles.forEach { it.decreaseMoistureByAmount(1000) }
        tiles.forEach { assertEquals(0, it.currentMoisture) }
        tiles.forEach { it.needsIrrigation() }
        assert(tiles[0].actionsNeeded.isEmpty())
        assert(tiles[1].actionsNeeded.isNotEmpty())
        assert(tiles[2].actionsNeeded.isNotEmpty())
        assert(tiles[3].actionsNeeded.isEmpty())
        tiles.forEach { it.increaseMoistureByAmount(3000) }
        tiles.forEach { assertEquals(250, it.currentMoisture) }
        assertTrue { tiles[1].hasPlantGrowing() }
        assertTrue { tiles[2].hasPlantGrowing() }
    }
}
