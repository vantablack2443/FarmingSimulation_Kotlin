package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BasicSimulationTest {
    private lateinit var sim: Simulation
    private lateinit var simData: SimulationData
    private lateinit var simMap: SimulationMap
    private lateinit var cherryTile: Tile
    private lateinit var grapeTile: Tile
    private lateinit var almondTile: Tile
    private lateinit var appleTile: Tile
    private lateinit var oatTile: Tile

    @BeforeEach
    fun setUp() {
        cherryTile = Tile(
            2, Coordinate(6, 2), TileType.PLANTATION,
            TileShape.OCTAGONAL
        )
        cherryTile.currentCrop = PlantType.CHERRY
        cherryTile.plant = Plant.createPlant(PlantType.CHERRY)
        cherryTile.currentMoisture = 1000
        cherryTile.maxMoisture = 1000
        grapeTile = Tile(
            4, Coordinate(10, 2), TileType.PLANTATION,
            TileShape.OCTAGONAL
        )
        grapeTile.currentCrop = PlantType.GRAPE
        grapeTile.plant = Plant.createPlant(PlantType.GRAPE)
        grapeTile.currentMoisture = 1000
        grapeTile.maxMoisture = 1000
        almondTile = Tile(
            5, Coordinate(12, 2), TileType.PLANTATION,
            TileShape.OCTAGONAL
        )
        almondTile.currentMoisture = 1000
        almondTile.maxMoisture = 1000
        almondTile.plant = Plant.createPlant(PlantType.ALMOND)
        almondTile.currentCrop = PlantType.ALMOND
        appleTile = Tile(
            6, Coordinate(14, 2), TileType.PLANTATION,
            TileShape.OCTAGONAL
        )
        appleTile.currentMoisture = 1000
        appleTile.maxMoisture = 1000
        appleTile.plant = Plant.createPlant(PlantType.APPLE)
        oatTile = Tile(
            3, Coordinate(8, 2),
            TileType.FIELD, TileShape.OCTAGONAL
        )
        oatTile.currentMoisture = 1000
        oatTile.maxMoisture = 1000
        oatTile.plant = Plant.createPlant(PlantType.OAT)
        oatTile.currentCrop = PlantType.OAT
        simMap = SimulationMap(
            mutableMapOf(
                Coordinate(5, 1) to
                    Tile(
                        1, Coordinate(5, 1), TileType.FARMSTEAD,
                        TileShape.SQUARE
                    ),
                Coordinate(6, 2) to cherryTile,
                Coordinate(8, 2) to oatTile,
                Coordinate(10, 2) to grapeTile,
                Coordinate(12, 2) to almondTile,
                Coordinate(14, 2) to appleTile
            )
        )
        simData = SimulationData()
        simData.map = simMap
        sim = Simulation(simData, 2, 20)
    }

    @Test
    fun testOneTick() {
        sim.run()
        simMap.getPlantableTiles().forEach { assertEquals(98, it.currentSunlight) }
        assertEquals(2, sim.currentTick)
        assertEquals(22, sim.currentYearTick)
        assertEquals(1200000, cherryTile.plant?.harvestEstimate)
        assertEquals(1200000, grapeTile.plant?.harvestEstimate)
        assertEquals(800000, almondTile.plant?.harvestEstimate)
        assertEquals(1530000, appleTile.plant?.harvestEstimate)
    }

    @Test
    fun testGrapeLateHarvestStart() {
        sim = Simulation(simData, 1, 19)
        sim.run()
        assertEquals(1083000, grapeTile.plant?.harvestEstimate)
        assertEquals(800000, almondTile.plant?.harvestEstimate)
    }

    @Test
    fun testCherryLateHarvestStart() {
        cherryTile.plant?.resetHarvestEstimate()
        sim = Simulation(simData, 1, 15)
        sim.run()
        assertEquals(324000, cherryTile.plant?.harvestEstimate)
    }

    @Test
    fun testZeroTick() {
        sim = Simulation(simData, 0, 1)
        sim.run()
        assertEquals(0, sim.currentTick)
        assertEquals(1, sim.currentYearTick)
    }
}
