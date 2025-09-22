package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SimulationMapTest {
    private lateinit var map: SimulationMap
    private lateinit var Tiles: MutableList<Tile>

    @BeforeEach
    fun setUp() {
        Tiles = mutableListOf()

        Tiles.add(
            Tile(
                12, Coordinate(-1, -1), TileType.FARMSTEAD, TileShape.OCTAGONAL, airflow = false,
                direction = null, shed = true, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                13, Coordinate(0, 0), TileType.ROAD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                14, Coordinate(1, -1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                54, Coordinate(2, 0), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                103, Coordinate(3, -1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                203, Coordinate(4, 0), TileType.ROAD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                303, Coordinate(5, -1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = true, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                52, Coordinate(6, 0), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1000, Coordinate(7, -1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1001, Coordinate(8, 0), TileType.ROAD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1002,
                Coordinate(
                    -1,
                    1
                ),
                TileType.MEADOW, TileShape.SQUARE, airflow = null, direction = null, shed = null,
                possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1003,
                Coordinate(
                    0,
                    2
                ),
                TileType.ROAD, TileShape.OCTAGONAL, airflow = null, direction = null, shed = null,
                possiblePlants = null, maxMoisture = null
            )
        ) // Fixed enum usage

        Tiles.add(
            Tile(
                9130, Coordinate(1, 1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                10012, Coordinate(2, 2), TileType.ROAD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                110, Coordinate(3, 1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                12012, Coordinate(4, 2), TileType.ROAD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                48, Coordinate(6, 2), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                10011, Coordinate(7, 1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                50, Coordinate(8, 2), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                212, Coordinate(9, 1), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                19012, Coordinate(-1, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                40, Coordinate(0, 4), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                31012, Coordinate(1, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                42, Coordinate(2, 4), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1512, Coordinate(3, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                44, Coordinate(4, 4), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1918, Coordinate(5, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                46, Coordinate(6, 4), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                612, Coordinate(7, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                43, Coordinate(8, 4), TileType.FIELD, TileShape.OCTAGONAL,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                5168, Coordinate(9, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                561, Coordinate(-1, 5), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1828, Coordinate(1, 5), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                9917, Coordinate(3, 5), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                1122, Coordinate(5, 5), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                5108, Coordinate(7, 3), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                591618, Coordinate(7, 5), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        Tiles.add(
            Tile(
                131214, Coordinate(9, 5), TileType.MEADOW, TileShape.SQUARE,
                airflow = null, direction = null, shed = null, possiblePlants = null, maxMoisture = null
            )
        )
        val tilesMap: MutableMap<Coordinate, Tile> = Tiles.associateBy { it.location }.toMutableMap()

        map = SimulationMap(tilesMap)
    }

    @Test
    fun testGetTileByCoordinate() {
        assertEquals(Tiles[1], map.getTileByCoordinate(Coordinate(0, 0)))
        assertEquals(Tiles[0], map.getTileByCoordinate(Coordinate(-1, -1)))
        assertNull(map.getTileByCoordinate(Coordinate(99, 121)))
    }

    @Test
    fun testGetTileByID() {
        assertEquals(Tiles[3], map.getTileByID(54))
        assertNull(map.getTileByID(9999))
    }

    @Test
    fun testGetTilesByRadius() {
        assertEquals(listOf(Tiles[1]), map.getTilesByRadius(Tiles[0], 1)) // on or the edge
        // of the map
    }

    @Test
    fun testGetNeighbor() {
        assertNull(map.getNeighbor(Tiles[0]), Direction.NORTH_WEST)
    }
}

@Test
fun testIsReachable() {
    // TODO
}

@Test
fun testGetAccessibleTiles() {
    // TODO
}

@Test
fun testFindTargetShed() {
    // TODO
}

@Test
fun testGetPlantableTiles() {
    // TODO
}

@Test
fun testFilterForPlantable() {
    // TODO
}

@Test
fun testFilterByType() {
    // TODO
}

@Test
fun testGetTiles() {
    // TODO
}

@Test
fun testSetTiles() {
    // TODO
}

@Test
fun getTileByCoordinate() {
    // TODO
}

@Test
fun getTileByID() {
    // TODO
}

@Test
fun getTilesByRadius() {
    // TODO
}

@Test
fun getNeighbor() {
    // TODO
}

@Test
fun isReachable() {
    // TODO
}

@Test
fun getAccessibleTiles() {
    // TODO
}

@Test
fun findTargetShed() {
    // TODO
}

@Test
fun getPlantableTiles() {
}

@Test
fun filterForPlantable() {
    // TODO
}

@Test
fun filterByType() {
    // TODO
}

@Test
fun getTiles() {
    // TODO
}

@Test
fun setTiles() {
    // TODO
}
}
