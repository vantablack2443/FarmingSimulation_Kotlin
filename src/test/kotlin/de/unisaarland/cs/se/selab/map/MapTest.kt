package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MapTest {
    private lateinit var map: SimulationMap
    private lateinit var fieldTile: Tile
    private lateinit var plantationTile: Tile
    private lateinit var shedTile: Tile
    private lateinit var edgeTile: Tile

    @BeforeEach
    fun setUp() {
        fieldTile = Tile(
            1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL
        )
        plantationTile = Tile(
            2, Coordinate(2, 0), TileType.PLANTATION, TileShape.OCTAGONAL
        )
        shedTile = Tile(
            3, Coordinate(3, 1), TileType.FARMSTEAD, TileShape.SQUARE,
        )
        edgeTile = Tile(
            4, Coordinate(-1, -1), TileType.FARMSTEAD, TileShape.SQUARE
//            airflow = true, direction = null, shed = null, possiblePlants = null, maxMoisture = null
        )

        val tiles = mutableMapOf(
            fieldTile.location to fieldTile,
            plantationTile.location to plantationTile,
            shedTile.location to shedTile,
            edgeTile.location to edgeTile
        )
        map = SimulationMap(tiles)
    }

    @Test
    fun testGetTileByCoordinate() {
        assertEquals(fieldTile, map.getTileByCoordinate(Coordinate(0, 0)))
        assertEquals(plantationTile, map.getTileByCoordinate(Coordinate(2, 0)))
        assertEquals(shedTile, map.getTileByCoordinate(Coordinate(3, 1)))
        assertNull(map.getTileByCoordinate(Coordinate(99, 99)))
    }

    @Test
    fun testGetTileByID() {
        assertEquals(fieldTile, map.getTileByID(1))
        assertEquals(plantationTile, map.getTileByID(2))
        assertEquals(shedTile, map.getTileByID(3))
        assertNull(map.getTileByID(99))
    }

    @Test
    fun testGetTilesByRadius1() {
        val tilesRadius1 = map.getTilesByRadius(fieldTile, 1)
        assertFalse(tilesRadius1.contains(fieldTile))
        assertTrue(tilesRadius1.contains(plantationTile))
        assertFalse(tilesRadius1.contains(shedTile))
        assertTrue(tilesRadius1.contains(edgeTile))
    }

    @Test
    fun testGetTilesByRadius2() {
        val tilesRadius2 = map.getTilesByRadius(edgeTile, 2)
        assertTrue(tilesRadius2.contains(fieldTile))
        assertTrue(tilesRadius2.contains(plantationTile))
        assertFalse(tilesRadius2.contains(shedTile), "Shed tile should be out of radius 2 from edge tile")
        assertFalse(tilesRadius2.contains(edgeTile), "Edge tile should not be included in its own radius")
    }

//    @Test
//    fun testIsReachable() {
//        // Create tiles
//        val tileA = Tile(
//            100,
//            Coordinate(10, 4),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileB = Tile(
//            101,
//            Coordinate(10, 2),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileC = Tile(
//            102,
//            Coordinate(10, 6),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileD = Tile(
//            103,
//            Coordinate(8, 4),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileShed = Tile(
//            104,
//            Coordinate(9, 5),
//            TileType.FARMSTEAD,
//            TileShape.SQUARE
//        )
//        val tiles = mutableMapOf(
//            tileA.location to tileA,
//            tileB.location to tileB,
//            tileC.location to tileC,
//            tileD.location to tileD,
//            tileShed.location to tileShed
//        )
//        val testMap = SimulationMap(tiles)
//
//        // Use Mockito to mock the real Machine class
//        val machine = mock<Machine> {
//            on { currentTile } doReturn tileShed
//            on { farmID } doReturn 1
//            on { currentHarvest } doReturn null
//            on { homeShed } doReturn tileShed
//        }
//
//        // Positive cases
//        assertTrue(testMap.isReachable(machine, tileA), "Tile A should be reachable from A")
//        assertTrue(testMap.isReachable(machine, tileB), "Tile B should be reachable from A")
//        assertTrue(testMap.isReachable(machine, tileC), "Tile C should be reachable from A")
//        assertTrue(testMap.isReachable(machine, tileD), "Tile D should be reachable from A")
//
//        // Negative case: unreachable tile
//        val tileFar = Tile(
//            200,
//            Coordinate(20, 20),
//            TileType.FIELD,
//            TileShape.OCTAGONAL
//        )
//        assertFalse(testMap.isReachable(machine, tileFar), "TileFar should not be reachable")
//    }
//
//    @Test
//    fun testGetReachableTiles() {
//        // Create tiles
//        val tileA = Tile(
//            100,
//            Coordinate(10, 4),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileB = Tile(
//            101,
//            Coordinate(10, 2),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileC = Tile(
//            102,
//            Coordinate(10, 6),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileD = Tile(
//            103,
//            Coordinate(8, 4),
//            TileType.ROAD,
//            TileShape.OCTAGONAL
//        )
//        val tileShed = Tile(
//            104,
//            Coordinate(9, 5),
//            TileType.FARMSTEAD,
//            TileShape.SQUARE
//        )
//        val tiles = mutableMapOf(
//            tileA.location to tileA,
//            tileB.location to tileB,
//            tileC.location to tileC,
//            tileD.location to tileD,
//            tileShed.location to tileShed
//        )
//        val testMap = SimulationMap(tiles)
//
//        // Use Mockito to mock the real Machine class
//        val machine = mock<Machine> {
//            on { currentTile } doReturn tileShed
//            on { farmID } doReturn 1
//            on { currentHarvest } doReturn null
//            on { homeShed } doReturn tileShed
//        }
//
//        val reachableTilesWithoutHarvest = testMap.getReachableTiles(machine, -1, false)
//        assertTrue(reachableTilesWithoutHarvest.contains(tileA))
//        assertTrue(reachableTilesWithoutHarvest.contains(tileB))
//        assertTrue(reachableTilesWithoutHarvest.contains(tileC))
//        assertTrue(reachableTilesWithoutHarvest.contains(tileD))
//        assertTrue(reachableTilesWithoutHarvest.contains(tileShed))
//    }
}
