package de.unisaarland.cs.se.selab.moistureReduction

import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.parser.Parser
import de.unisaarland.cs.se.selab.simulation.Simulation
import org.junit.jupiter.api.Test
import java.io.File

const val MAP_JSON_SIMPLE: String = """
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
      "coordinates": { "x": 4, "y": 0 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": ALMOND
    },
     {
    "id": 3,
    "coordinates": { "x": 5, "y": 1},
    "category": "FARMSTEAD",
    "airflow": false,
    "farm": 0,
    "shed": true
    },
    {
      "id": 4,
      "coordinates": { "x": 0, "y": 2 },
      "category": "FOREST",
      "airflow": false
    },
   {
      "id": 5,
      "coordinates": { "x": 2, "y": 2 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": ALMOND
    },
    {
      "id": 6,
      "coordinates": { "x": 4, "y": 2 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": ALMOND
    },
    {
    "id": 7,
    "coordinates": { "x": 6, "y": 2 },
    "category": "FIELD",
    "airflow": false,
    "farm": 0,
    "capacity": 1000,
    "possiblePlants": ["PUMPKIN", "WHEAT"]
    },
    {
      "id": 8,
      "coordinates": { "x": 8, "y": 2 },
      "category": "ROAD",
      "airflow": false
    },
    {
    "id": 9,
    "coordinates": { "x": 8, "y": 0 },
    "category": "FIELD",
    "airflow": false,
    "farm": 0,
    "capacity": 1000,
    "possiblePlants": ["PUMPKIN", "WHEAT"]
    }
  ]
}
"""
const val FARM_JSON_SIMPLE: String = """
{
  "farms": [
    {
      "id": 0,
      "name": "",
      "farmsteads": [3],
      "fields": [7, 9],
      "plantations": [2, 5, 6],
      "machines": [
      {
          "id": 0,
          "name": "Tractor",
          "actions": ["SOWING", "IRRIGATING"],
          "plants": ["PUMPKIN", "WHEAT"],
          "duration": 4,
          "location": 3
        }
      ],
      "sowingPlans": [
      ]
    }
  ]
}
"""
const val SCENARIO_JSON_SIMPLE: String = """
    {
    "clouds": [
    ],
    "incidents": [
    ]
    }
"""

class MoistureReductionTests {

    @Test
    fun testSimpleMoistureReduction() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAP_JSON_SIMPLE)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARM_JSON_SIMPLE)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO_JSON_SIMPLE)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 1, 1)
        simulation.run()

        val plantableTiles = simData.map.getPlantableTiles()
        val fieldTiles = simData.map.filterByType(TileType.FIELD, plantableTiles)
        val plantationTiles = simData.map.filterByType(TileType.PLANTATION, plantableTiles)

        for (tile in fieldTiles) {
            assert(tile.currentMoisture == 930)
        }
        for (tile in plantationTiles) {
            assert(tile.currentMoisture == 700)
        }
    }
}
