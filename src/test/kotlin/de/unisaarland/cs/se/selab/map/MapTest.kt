package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MapTest {
    private lateinit var map: SimulationMap
    private lateinit var fieldTile: Tile
    private lateinit var plantationTile: Tile
    private lateinit var shedTile: Tile

    @BeforeEach
    fun setUp() {
        fieldTile = Tile(
            1, Coordinate(0, 0), TileType.FIELD, TileShape.SQUARE, airflow = false,
            direction = null, shed = null, possiblePlants = null, maxMoisture = null
        )
        plantationTile = Tile(
            2, Coordinate(1, 0), TileType.PLANTATION, TileShape.SQUARE,
            airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
        )
        shedTile = Tile(
            3, Coordinate(2, 0), TileType.VILLAGE, TileShape.SQUARE,
            airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
        )

        val tiles = mutableMapOf(
            fieldTile.location to fieldTile,
            plantationTile.location to plantationTile,
            shedTile.location to shedTile,
        )
        map = SimulationMap(tiles)
    }

    @Test
    fun testGetTileByCoordinate() {
        assertEquals(fieldTile, map.getTileByCoordinate(Coordinate(0, 0)))
        assertEquals(plantationTile, map.getTileByCoordinate(Coordinate(1, 0)))
        assertEquals(shedTile, map.getTileByCoordinate(Coordinate(2, 0)))
        assertNull(map.getTileByCoordinate(Coordinate(99, 99)))
    }

}
