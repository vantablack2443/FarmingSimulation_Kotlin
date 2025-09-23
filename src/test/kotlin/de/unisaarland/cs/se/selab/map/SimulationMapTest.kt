package de.unisaarland.cs.se.selab.map

/**
 * import de.unisaarland.cs.se.selab.coordinate.Coordinate
 * import de.unisaarland.cs.se.selab.enumerations.Direction
 * import de.unisaarland.cs.se.selab.enumerations.TileShape
 * import de.unisaarland.cs.se.selab.enumerations.TileType
 * import de.unisaarland.cs.se.selab.tile.Tile
 * import org.junit.jupiter.api.BeforeEach
 * import org.junit.jupiter.api.Test
 * import kotlin.test.assertEquals
 * import kotlin.test.assertNull
 *
 * class SimulationMapTest {
 *     private lateinit var map: SimulationMap
 *     private lateinit var tiles: MutableList<Tile>
 *
 *     @BeforeEach
 *     fun setUp() {
 *         // i sent the old setup function definition to mahmoud
 *         tiles = mutableListOf(
 *             Tile(0, Coordinate(-1, -1), TileType.FARMSTEAD, TileShape.SQUARE),
 *             Tile(1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL),
 *             Tile(2, Coordinate(1, 0), TileType.PLANTATION, TileShape.OCTAGONAL),
 *             Tile(3, Coordinate(0, 1), TileType.FARMSTEAD, TileShape.SQUARE),
 *             Tile(54, Coordinate(10, 10), TileType.VILLAGE, TileShape.OCTAGONAL)
 *         )
 *         val tileMap = tiles.associateBy { it.location }.toMutableMap()
 *         map = SimulationMap(tileMap)
 *     }
 *
 *     @Test
 *     fun testGetTileByCoordinate() {
 *         assertEquals(tiles[1], map.getTileByCoordinate(Coordinate(0, 0)))
 *         assertEquals(tiles[0], map.getTileByCoordinate(Coordinate(-1, -1)))
 *         assertNull(map.getTileByCoordinate(Coordinate(99, 121)))
 *     }
 *
 *     @Test
 *     fun testGetTileByID() {
 *         assertEquals(tiles[3], map.getTileByID(54))
 *         assertNull(map.getTileByID(9999))
 *     }
 *
 *     @Test
 *     fun testGetTilesByRadius() {
 *         assertEquals(listOf(tiles[1]), map.getTilesByRadius(tiles[0], 1)) // on or the edge of the map
 *     }
 *
 *     @Test
 *     fun testGetNeighbor() {
 *         assertNull(map.getNeighbor(tiles[0], Direction.NORTH_WEST))
 *     }
 *
 *     @Test
 *     fun testIsReachable() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testGetAccessibleTiles() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testFindTargetShed() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testGetPlantableTiles() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testFilterForPlantable() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testFilterByType() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testGetTiles() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun testSetTiles() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getTileByCoordinate() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getTileByID() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getTilesByRadius() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getNeighbor() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun isReachable() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getAccessibleTiles() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun findTargetShed() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getPlantableTiles() {
 *         // val expected = Tiles.filter { it.category == TileType.FIELD || it.category == TileType.PLANTATION }
 *         // assertEquals(expected.toSet(), map.getPlantableTiles().toSet())
 *     }
 *
 *     @Test
 *     fun filterForPlantable() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun filterByType() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun getTiles() {
 *         // TODO
 *     }
 *
 *     @Test
 *     fun setTiles() {
 *         // TODO
 *     }
 * }
 */
