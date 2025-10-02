package general

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CloudMovement {
    private lateinit var cloudHandler: CloudHandler
    private lateinit var simMap: SimulationMap
    private lateinit var cloudOne: Cloud
    private lateinit var cloudTwo: Cloud
    private lateinit var cloudThree: Cloud
    private lateinit var cloudFour: Cloud
    val cloudsList = mutableListOf<Cloud>()
    private fun setUpMoveCloud() {
        createMap()
        this.cloudHandler = CloudHandler(simMap)
        cloudOne = Cloud(1, Coordinate(0, 0), 3, 1000)
        cloudTwo = Cloud(2, Coordinate(2, 0), 3, 1000)
        cloudThree = Cloud(3, Coordinate(4, 0), 2, 1000)
        cloudFour = Cloud(4, Coordinate(6, 0), 2, 1500)
        cloudsList.add(cloudOne)
        cloudsList.add(cloudTwo)
        cloudsList.add(cloudThree)
        cloudsList.add(cloudFour)
        cloudHandler.setMaxCloudID(4)
        cloudsList.forEach { cloudHandler.addCloudToCloudsList(it) }
        cloudsList.forEach { cloudHandler.coordinateToCloud[it.location] = it }
    }

    private fun createMap() {
        val tiles = listOf(
            Tile(
                2,
                Coordinate(0, 0),
                TileType.ROAD,
                TileShape.OCTAGONAL
            ),
            Tile(
                3,
                Coordinate(2, 0),
                TileType.ROAD,
                TileShape.OCTAGONAL
            ),
            Tile(
                4,
                Coordinate(4, 0),
                TileType.ROAD,
                TileShape.OCTAGONAL
            ),
            Tile(
                5,
                Coordinate(6, 0),
                TileType.ROAD,
                TileShape.OCTAGONAL
            )
        )
        val mapping = mutableMapOf<Coordinate, Tile>()
        tiles.forEach { mapping[it.location] = it }
        tiles.forEach { it.airflow = true }
        tiles.forEach { it.direction = Direction.EAST }
        simMap = SimulationMap(mapping)
    }

    @Test
    fun testMoveCloud() {
        setUpMoveCloud()
        cloudHandler.moveClouds()
        assertEquals(1, cloudHandler.cloudsList.size)
        assert(cloudHandler.removedClouds.isEmpty())
        assertEquals(1, cloudHandler.coordinateToCloud.values.toList().size)
        assertEquals(7, cloudHandler.cloudsList[0].id)
        assertEquals(Coordinate(6, 0), cloudHandler.coordinateToCloud.values.toList()[0].location)
        cloudsList.clear()
        cloudHandler.cloudsList.clear()
    }

    private fun createMapForDissipate() {
        val plantationTile = Tile(
            5,
            Coordinate(6, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        plantationTile.plant = Plant.createPlant(PlantType.CHERRY)
        plantationTile.currentMoisture = 40
        plantationTile.maxMoisture = 90
        plantationTile.airflow = true
        plantationTile.direction = Direction.EAST
        val tiles = listOf(
            Tile(
                2,
                Coordinate(0, 0),
                TileType.ROAD,
                TileShape.OCTAGONAL
            ),
            Tile(
                3,
                Coordinate(2, 0),
                TileType.VILLAGE,
                TileShape.OCTAGONAL
            ),
            Tile(
                4,
                Coordinate(4, 0),
                TileType.ROAD,
                TileShape.OCTAGONAL
            ),
            plantationTile
        )
        val mapping = mutableMapOf<Coordinate, Tile>()
        tiles.forEach { mapping[it.location] = it }
        simMap = SimulationMap(mapping)
    }
    private fun setUpCloudDissipate() {
        createMapForDissipate()
        this.cloudHandler = CloudHandler(simMap)
        cloudOne = Cloud(1, Coordinate(0, 0), 1, 1000)
        cloudOne.isStuck = true
        cloudTwo = Cloud(2, Coordinate(2, 0), 3, 1000)
        cloudThree = Cloud(3, Coordinate(4, 0), 2, 5500)
        cloudFour = Cloud(4, Coordinate(6, 0), 2, 5000)
        cloudsList.add(cloudOne)
        cloudsList.add(cloudTwo)
        cloudsList.add(cloudThree)
        cloudsList.add(cloudFour)
        cloudHandler.setMaxCloudID(4)
        cloudsList.forEach { cloudHandler.addCloudToCloudsList(it) }
        cloudsList.forEach { cloudHandler.coordinateToCloud[it.location] = it }
    }

    @Test
    fun testCloudDissipation() {
        setUpCloudDissipate()
        cloudHandler.moveClouds()
        assertEquals(1, cloudHandler.cloudsList.size)
        assertEquals(4, cloudHandler.coordinateToCloud[Coordinate(6, 0)]?.id)
        assertEquals(4950, cloudFour.amount)
        assertEquals(90, simMap.getTileByID(5)?.currentMoisture)
        assert(cloudHandler.removedClouds.isEmpty())
        cloudsList.clear()
        cloudHandler.cloudsList.clear()
    }
}
