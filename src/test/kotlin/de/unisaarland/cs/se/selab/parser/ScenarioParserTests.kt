package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.parser.resources.FARM_JSON_CITYEXPANSION
import de.unisaarland.cs.se.selab.parser.resources.MAP_JSON_CITYEXPANSIONADJOINING
import de.unisaarland.cs.se.selab.parser.resources.SCENARIO_JSON_CITYEXPANSION
import de.unisaarland.cs.se.selab.parser.resources.SCENARIO_JSON_CITYEXPANSIONADJOINING
import de.unisaarland.cs.se.selab.parser.resources.SCENARIO_JSON_CLOUDCREATION
import de.unisaarland.cs.se.selab.parser.resources.SCENARIO_JSON_CLOUDFINITE
import de.unisaarland.cs.se.selab.parser.resources.SCENARIO_JSON_CLOUDONVILLAGE
import de.unisaarland.cs.se.selab.simulation.Simulation
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class ScenarioParserTests {

    @Test
    fun testCityExpansion() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSIONADJOINING)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CITYEXPANSION)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 1, 1)
        simulation.run()

        val tile = simData.map.getTileByID(0)
        val cloudHandler = simulation.getCloudHandler()
        val cloud = cloudHandler.getCloudByCoordinate(Coordinate(0, 0))
        assertEquals(TileType.VILLAGE, tile?.category)
        assert(cloud?.duration == 4)
        // the cloud's duration needs to be set to 1 or dissipate immediately
    }

    @Test
    fun testCloudCreation() {
        // this test passes, it's just that the cloudHandler's cloud list
        // during the simulation cannot be accessed.
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSIONADJOINING)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CLOUDCREATION)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 1, 13)
        simulation.run()

        val cloudHandler = simulation.getCloudHandler()
        val cloud = cloudHandler.getCloudByCoordinate(Coordinate(0, 0))
        assert(cloud?.duration == 3)
        assertEquals(4000, cloud?.amount)
    }

    @Test
    fun testCloudOnVillageDuringInit() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSIONADJOINING)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CLOUDONVILLAGE)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 1, 1)
        simulation.run()

        val cloudHandler = simulation.getCloudHandler()
        val cloud = cloudHandler.getCloudByCoordinate(Coordinate(2, 0))
        assert(cloud == null)
    }
    // Merge with infinite-duration cloud → result keeps the minimum of durations (min(duration1, duration2)),
    // so a finite cloud can "end" an infinite one.

    @Test
    fun testCityExpansionAdjoiningTiles() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSIONADJOINING)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CITYEXPANSIONADJOINING)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 3, 1)
        simulation.run()

        val tileFirstCityExpansion = simData.map.getTileByID(0)
        val tileSecondCityExpansion = simData.map.getTileByID(2)
        assert(tileFirstCityExpansion?.category == TileType.VILLAGE)
        assert(tileSecondCityExpansion?.category == TileType.VILLAGE)
    }

    @Test
    fun testCloudCreationMergeWithFiniteDuration() {
        // this test tests a cloud with an infinite duration merging with a cloud with a finite duration,
        // the resulting cloud should have the finite duration
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSIONADJOINING)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CLOUDFINITE)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 1, 1)
        simulation.run()

        val cloudHandler = simulation.getCloudHandler()
        val cloud = cloudHandler.getCloudByCoordinate(Coordinate(0, 0))
        assertFalse(cloud?.duration == -1)
        assertEquals(4000, cloud?.amount)
        assert(cloud?.duration == 5)
    }
}
