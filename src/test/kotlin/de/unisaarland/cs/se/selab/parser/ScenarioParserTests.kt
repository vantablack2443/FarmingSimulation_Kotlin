package de.unisaarland.cs.se.selab.parser
/*
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.simulation.Simulation
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

const val MAP_JSON_CITYEXPANSION = """
    {
      "tiles": [
        {
          "id": 0,
          "coordinates": { "x": 0, "y": 0 },
          "category": "ROAD",
          "airflow": false
        },
        {
            "id": 1,
            "coordinates": { "x": 2, "y": 0 },
            "category": "VILLAGE"
            },
            {
            "id": 2,
            "coordinates": { "x": 0, "y": 2 },
            "category": "PLANTATION",
            "airflow": false,
            "farm": 0,
            "capacity": 900,
            "plant": ALMOND
            }
      ]
    }
    """
const val FARM_JSON_CITYEXPANSION = """
        {
  "farms": [
    {
      "id": 0,
      "name": "",
      "farmsteads": [
      ],
      "fields": [
      ],
      "plantations": [2],
      "machines": [
      ],
      "sowingPlans": [
      ]
    }
  ]
}
        """
const val SCENARIO_JSON_CITYEXPANSION = """
            {
            "clouds": [
                {
                    "id": 0,
                    "location": 0,
                    "duration": 5,
                    "amount": 4000
                }
            ],
            "incidents": [
                {
                    "id": 0,
                    "type": "CITY_EXPANSION",
                    "tick": 0,
                    "location": 0
                }
            ]
            }
        """
const val SCENARIO_JSON_CLOUDCREATION = """
            {
            "clouds": [
                {
                    "id": 0,
                    "location": 0,
                    "duration": 4,
                    "amount": 1000
                }
            ],
            "incidents": [
                {
                    "id": 0,
                    "type": "CLOUD_CREATION",
                    "tick": 0,
                    "location": 0,
                    "radius": 1,
                    "amount": 3000,
                    "duration": 5
                }
            ]
            }
        """

class ScenarioParserTests {

    @Test
    fun testCityExpansion() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSION)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CITYEXPANSION)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 2, 1)
        simulation.run()

        val tile = simData.map.getTileByID(0)
        val cloud = simData.getCloudById(0)
        assertEquals(TileType.VILLAGE, tile?.category)
        assert(cloud?.duration == 1)
        // cloud duration should be set?
    }

    @Test
    fun testCloudCreation() {
        // this test passes, it's just that the cloudHandler's cloud list
        // during the simulation cannot be accessed.
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CITYEXPANSION)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CITYEXPANSION)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CLOUDCREATION)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 2, 1)
        simulation.run()

        val cloudHandler = CloudHandler(simData.map)
        val cloud = cloudHandler.getCloudByCoordinate(Coordinate(0, 0))
        assert(cloud?.duration == 3)
        assertEquals(4000, cloud?.amount)
    }
}*/
