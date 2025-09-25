package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.simulation.Simulation
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

const val MAP_JSON_CloudKeeper = """
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
const val FARM_JSON_CloudKeeper = """
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
const val SCENARIO_JSON_CloudKeeper = """
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

class ScenarioParserTests {

    @Test
    fun testCloudKeeper() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_CloudKeeper)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_CloudKeeper)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_CloudKeeper)

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
    // do unit test for cloud creation
}
