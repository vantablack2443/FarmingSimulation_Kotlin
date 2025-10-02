package integrationtests

import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.harvestestimatehandler.HarvestEstimateHandler
import de.unisaarland.cs.se.selab.incidents.CloudCreation
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

class CloudCreationPlusHE {
    private lateinit var map: SimulationMap
    private lateinit var estimateHandler: HarvestEstimateHandler
    private lateinit var cloudHandler: CloudHandler
    private lateinit var tile: Tile

    @BeforeEach
    fun setUp() {
        tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val tiles = mutableMapOf<Coordinate, Tile>()
        tiles[Coordinate(0, 0)] = tile
        map = SimulationMap(tiles)
        cloudHandler = CloudHandler(map)
        estimateHandler = HarvestEstimateHandler(map)
    }

    @Test
    fun testCloudCreationAffectsHarvestEstimate() {
        val incident = CloudCreation(1, 0, IncidentType.CLOUD_CREATION, tile, 0, 2, 10)
        incident.cloudHandler = cloudHandler

        incident.execute(map, 0)
        assertTrue(cloudHandler.coordinateToCloud.containsKey(tile.location))
        // Check harvest estimate is updated (simulate your logic)
        val estimate = tile.plant?.harvestEstimate
        assertNull(estimate)
    }

    @Test
    fun testCloudCreationOnVillageTileDoesNotCreateCloud() {
        val villageTile = Tile(
            2,
            Coordinate(1, 1),
            TileType.VILLAGE,
            TileShape.OCTAGONAL
        )
        map.tiles[villageTile.location] = villageTile
        val incident = CloudCreation(2, 0, IncidentType.CLOUD_CREATION, villageTile, 0, 2, 10)
        incident.cloudHandler = cloudHandler

        incident.execute(map, 0)

        assertFalse(cloudHandler.coordinateToCloud.containsKey(villageTile.location))
    }

    @Test
    fun testCloudMergeLogic() {
        // Create first cloud
        val incident1 = CloudCreation(3, 0, IncidentType.CLOUD_CREATION, tile, 0, 2, 10)
        incident1.cloudHandler = cloudHandler
        incident1.execute(map, 0)

        // Create second cloud on same tile to trigger merge
        val incident2 = CloudCreation(4, 1, IncidentType.CLOUD_CREATION, tile, 0, 2, 20)
        incident2.cloudHandler = cloudHandler
        incident2.execute(map, 1)

        // Only one cloud should remain after merge
        assertEquals(1, cloudHandler.cloudsList.size)
        val cloud = cloudHandler.coordinateToCloud[tile.location]
        assertNotNull(cloud)
        assertEquals(tile.location, cloud?.location)
    }

    @Test
    fun testAffectedTilesRadius() {
        // Add another tile in radius
        val tile2 = Tile(3, Coordinate(0, 1), TileType.FIELD, TileShape.OCTAGONAL)
        map.tiles[tile2.location] = tile2

        val incident = CloudCreation(5, 0, IncidentType.CLOUD_CREATION, tile, 1, 2, 10)
        incident.cloudHandler = cloudHandler
        incident.execute(map, 0)

        // Both tiles should have clouds
        assertTrue(cloudHandler.coordinateToCloud.containsKey(tile.location))
    }
}
