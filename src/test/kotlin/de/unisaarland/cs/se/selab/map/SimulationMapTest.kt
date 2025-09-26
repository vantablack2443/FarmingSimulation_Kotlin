package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.machine.PlantAndHarvest
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class SimulationMapTest {
    lateinit var map: SimulationMap
    lateinit var tiles: MutableList<Tile>

    @BeforeEach
    fun setUp() {
        tiles = mutableListOf()
        tiles.add(
            Tile(
                12, Coordinate(-1, -1), TileType.FARMSTEAD, TileShape.OCTAGONAL
            )
        )
        tiles.add(
            Tile(
                13, Coordinate(0, 0), TileType.ROAD, TileShape.OCTAGONAL

            )
        )
        tiles.add(
            Tile(
                14, Coordinate(1, -1), TileType.MEADOW, TileShape.SQUARE

            )
        )
        tiles.add(
            Tile(
                54, Coordinate(2, 0), TileType.FIELD, TileShape.OCTAGONAL

            )
        )
        tiles.add(
            Tile(
                103, Coordinate(3, -1), TileType.MEADOW, TileShape.SQUARE

            )
        )
        tiles.add(
            Tile(
                203, Coordinate(4, 0), TileType.ROAD, TileShape.OCTAGONAL

            )
        )
        tiles.add(
            Tile(
                303, Coordinate(5, -1), TileType.MEADOW, TileShape.SQUARE

            )
        )
        tiles.add(
            Tile(
                52, Coordinate(6, 0), TileType.FIELD, TileShape.OCTAGONAL
            )
        )
        tiles.add(
            Tile(
                1000, Coordinate(7, -1), TileType.MEADOW, TileShape.SQUARE
            )
        )
        tiles.add(
            Tile(
                1001, Coordinate(8, 0), TileType.ROAD, TileShape.OCTAGONAL
            ))
//        )
//        tiles.add(
//            Tile(
//                1002,
//                Coordinate(
//                    -1,
//                    1
//                ),
//                TileType.MEADOW, TileShape.SQUARE
//            )
//        )
//        tiles.add(
//            Tile(
//                1003,
//                Coordinate(
//                    0,
//                    2
//                ),
//                TileType.ROAD, TileShape.OCTAGONAL
//            )
//        ) // Fixed enum Usage
//
//        tiles.add(
//            Tile(
//                9130, Coordinate(1, 1), TileType.MEADOW, TileShape.SQUARE
//            )
//        )
//        tiles.add(
//            Tile(
//                10012, Coordinate(2, 2), TileType.ROAD, TileShape.OCTAGONAL
//            )
//        )
//        tiles.add(
//            Tile(
//                110, Coordinate(3, 1), TileType.MEADOW, TileShape.SQUARE
//            )
//        )
//        tiles.add(
//            Tile(
//                12012, Coordinate(4, 2), TileType.ROAD, TileShape.OCTAGONAL
//            )
//        )
//        tiles.add(
//            Tile(
//                48, Coordinate(6, 2), TileType.FIELD, TileShape.OCTAGONAL
//        ))
//        tiles.add(
//            Tile(
//                10011, Coordinate(7, 1), TileType.MEADOW, TileShape.SQUARE
//            )
//        )
//        tiles.add(
//            Tile(
//                50, Coordinate(8, 2), TileType.FIELD, TileShape.OCTAGONAL
//            )
//        )
//        tiles.add(
//            Tile(
//                212, Coordinate(9, 1), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                19012, Coordinate(-1, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                40, Coordinate(0, 4), TileType.FIELD, TileShape.OCTAGONAL)
//        )
//        tiles.add(
//            Tile(
//                31012, Coordinate(1, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                42, Coordinate(2, 4), TileType.FIELD, TileShape.OCTAGONAL)
//        )
//        tiles.add(
//            Tile(
//                1512, Coordinate(3, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                44, Coordinate(4, 4), TileType.FIELD, TileShape.OCTAGONAL)
//        )
//        tiles.add(
//            Tile(
//                1918, Coordinate(5, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                46, Coordinate(6, 4), TileType.FIELD, TileShape.OCTAGONAL)
//        )
//        tiles.add(
//            Tile(
//                612, Coordinate(7, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
////        tiles.add(
////            Tile(
//                43, Coordinate(8, 4), TileType.FIELD, TileShape.OCTAGONAL)
//        )
//        tiles.add(
//            Tile(
//                5168, Coordinate(9, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                561, Coordinate(-1, 5), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                1828, Coordinate(1, 5), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                9917, Coordinate(3, 5), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                1122, Coordinate(5, 5), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                5108, Coordinate(7, 3), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                591618, Coordinate(7, 5), TileType.MEADOW, TileShape.SQUARE)
//        )
//        tiles.add(
//            Tile(
//                131214, Coordinate(9, 5), TileType.MEADOW, TileShape.SQUARE)
//        )
        val tilesMap : MutableMap<Coordinate, Tile> = mutableMapOf()
        tiles.forEach { tile ->
            tilesMap[tile.location] = tile
        }
        map = SimulationMap(tilesMap)

    }
    @Test
    fun testGetTileByCoordinate() {
        assertEquals(tiles[1], map.getTileByCoordinate(Coordinate(0, 0)))
        assertEquals(tiles[0], map.getTileByCoordinate(Coordinate(-1, -1)))
        assertNull(map.getTileByCoordinate(Coordinate(99, 121)))
    }

    @Test
    fun testGetTileByID() {
        assertEquals(tiles[3], map.getTileByID(54))
        assertNull(map.getTileByID(9999))
    }

    @Test
    fun testGetTilesByRadius() {
        assertEquals(listOf(tiles[1]), map.getTilesByRadius(tiles[0], 1)) // on or the edge of the map
    }

    @Test
    fun testMachineIsReachable() {
        val startTile = map.getTileByCoordinate(Coordinate(-1, -1))!!
        val destTile = map.getTileByCoordinate(Coordinate(6, 4))!!
        val machine = Machine(
            id = 1,
            name = "someMachine",
            10,
            currentTile = startTile,
            actions = listOf(ActionType.HARVESTING),
            plants = listOf(PlantType.PUMPKIN),
            map.getTileByCoordinate(Coordinate(-1,-1))!!
        )
        assertTrue(map.isReachable(machine, destTile))
    } // need to look at it again
    @Test
    fun testLoadedMachineIsReachable() {
        val machine1 = mock<Machine>()
        val machine2 = mock<Machine>()
        val tile1 = Tile(1, Coordinate(4, 2), TileType.FIELD, TileShape.OCTAGONAL)
        val tile2 = Tile(2, Coordinate(6, 2), TileType.VILLAGE, TileShape.OCTAGONAL)
        val tile6 = Tile(6, Coordinate(8, 2), TileType.VILLAGE, TileShape.OCTAGONAL)
        whenever(machine1.currentHarvest).thenReturn((PlantAndHarvest(PlantType.PUMPKIN,190999)))
        whenever(machine2.currentHarvest).thenReturn(null)
        whenever(machine1.currentTile).thenReturn(tile1)
        whenever(machine2.currentTile).thenReturn(tile1)
        val tiles1 = listOf(tile1, tile2,tile6)
        val tilesMap : MutableMap<Coordinate, Tile> = mutableMapOf()
        tiles1.forEach { tile ->
            tilesMap[tile.location] = tile
        }
        val map1 = SimulationMap(tilesMap)



        assertNull(map1.isReachable(machine1, tile6))
        assertTrue(map1.isReachable(machine2, tile6))
    }

    @Test
    fun testGetNeighbor() {
        assertNull(map.getNeighbor(tiles[0], Direction.NORTH_WEST))
    }


    @Test
    fun getPlantableTiles() {
        val expected = tiles.filter { it.category == TileType.FIELD || it.category == TileType.PLANTATION }
        assertEquals(expected.toSet(), map.getPlantableTiles().toSet())
    }

}