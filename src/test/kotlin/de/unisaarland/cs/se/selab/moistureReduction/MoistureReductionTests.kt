package de.unisaarland.cs.se.selab.moistureReduction

/*

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

const val MAP = """
    {
  "tiles": [
    {
      "id": 0,
      "coordinates": { "x": 0, "y": 0 },
      "category": "FIELD",
      "airflow": false,
      "farm": 0,
      "capacity": 1000,
      "possiblePlants": ["PUMPKIN", "WHEAT"]
    },
    {
      "id": 1,
      "coordinates": { "x": 2, "y": 0 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": "APPLE"
    },
    {
      "id": 2,
      "coordinates": { "x": 4, "y": 0 },
      "category": "FIELD",
      "airflow": false,
      "farm": 0,
      "capacity": 1000,
      "possiblePlants": ["POTATO", "OAT"]
    },
    {
      "id": 3,
      "coordinates": { "x": 6, "y": 0 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": "GRAPE"
    },
    {
      "id": 4,
      "coordinates": { "x": 8, "y": 0 },
      "category": "FIELD",
      "airflow": false,
      "farm": 0,
      "capacity": 1000,
      "possiblePlants": ["PUMPKIN", "WHEAT"]
    },
    {
      "id": 5,
      "coordinates": { "x": 0, "y": 2 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": "CHERRY"
    },
    {
      "id": 6,
      "coordinates": { "x": 2, "y": 2 },
      "category": "FIELD",
      "airflow": false,
      "farm": 0,
      "capacity": 1000,
      "possiblePlants": ["POTATO", "OAT"]
    },
    {
      "id": 7,
      "coordinates": { "x": 4, "y": 2 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": "ALMOND"
    },
    {
      "id": 8,
      "coordinates": { "x": 6, "y": 2 },
      "category": "FIELD",
      "airflow": false,
      "farm": 0,
      "capacity": 1000,
      "possiblePlants": ["PUMPKIN", "WHEAT"]
    },
    {
      "id": 9,
      "coordinates": { "x": 8, "y": 2 },
      "category": "PLANTATION",
      "airflow": false,
      "farm": 0,
      "capacity": 800,
      "plant": "ALMOND"
    },
    {
      "id": 10,
      "coordinates": { "x": 3, "y": 1},
      "category": "FARMSTEAD",
      "airflow": false,
      "farm": 0,
      "shed": true
    }
  ]
}
"""

const val FARM = """
    {
  "farms": [
    {
      "id": 0,
      "name": "",
      "farmsteads": [10],
      "fields": [0, 2, 4, 6, 8],
      "plantations": [1, 3, 5, 7, 9],
      "machines": [
        {
          "id": 0,
          "name": "0",
          "actions": ["SOWING", "WEEDING"],
          "plants": ["PUMPKIN", "WHEAT"],
          "duration": 4,
          "location": 10
        },
        {
          "id": 1,
          "name": "1",
          "actions": ["CUTTING", "MOWING"],
          "plants": ["APPLE", "GRAPE"],
          "duration": 4,
          "location": 10
        },
        {
          "id": 2,
          "name": "2",
          "actions": ["HARVESTING", "IRRIGATING"],
          "plants": ["PUMPKIN", "WHEAT", "OAT", "POTATO", "CHERRY", "ALMOND", "GRAPE", "APPLE"],
          "duration": 4,
          "location": 10
        }
      ],
      "sowingPlans": [
      ]
    }
  ]
}
"""
const val SCENARIO = """
    {
  "clouds": [
  ],
  "incidents": [
  ]
}
"""
const val MAPSTWO = """
    {
  "tiles": [
    {
      "id": 10,
      "coordinates": { "x": 6, "y": 0 },
      "category": "ROAD",
      "airflow": false
    },
    {
      "id": 12,
      "coordinates": { "x": 8, "y": 0 },
      "category": "ROAD",
      "airflow": false
    },
    {
      "id": 14,
      "coordinates": {"x": 10, "y": 0 },
      "category": "ROAD",
      "airflow": false
    },
    {
      "id": 40,
      "coordinates": {"x": 10, "y":  2},
      "category": "FIELD",
      "farm": 0,
      "capacity": 8000,
      "possiblePlants": ["PUMPKIN", "WHEAT"],
      "airflow": false
    },
    {
      "id": 11,
      "coordinates": { "x": 6, "y": 2 },
      "category": "ROAD",
      "airflow": false
    },
    {
      "id": 13,
      "coordinates": { "x": 8, "y": 2 },
      "category": "FIELD",
      "farm": 0,
      "capacity": 10000,
      "possiblePlants": ["OAT", "WHEAT"],
      "airflow": false
    },
    {
      "id": 24,
      "coordinates": { "x": 10, "y": 4 },
      "category": "FIELD",
      "farm": 1,
      "capacity": 8000,
      "possiblePlants": ["POTATO", "WHEAT"],
      "airflow": false
    },
    {
      "id": 22,
      "coordinates": { "x": 8, "y": 4 },
      "category": "FIELD",
      "farm": 1,
      "capacity": 8000,
      "possiblePlants": ["OAT"],
      "airflow": false
    },
    {
      "id": 18,
      "coordinates": { "x": 6, "y": 4 },
      "category": "PLANTATION",
      "plant": "CHERRY",
      "capacity": 8000,
      "farm": 0,
      "airflow": false
    },
    {
      "id": 116,
      "coordinates": { "x": 7, "y": 3 },
      "category": "VILLAGE"
    },
    {
      "id": 117,
      "coordinates": { "x": 7, "y": 1 },
      "category": "FOREST",
      "airflow": false
    },
    {
      "id": 118,
      "coordinates": { "x": 9, "y": 1 },
      "category": "VILLAGE"
    },
    {
      "id": 15,
      "coordinates": { "x": 9, "y": 3 },
      "category": "FOREST",
      "airflow": false

    },
    {
      "id": 30,
      "coordinates": { "x": 9, "y": 5 },
      "category": "FARMSTEAD",
      "airflow": false,
      "farm": 1,
      "shed": true
    },
    {
      "id": 300,
      "coordinates": {"x": 11, "y": 1},
      "category": "FARMSTEAD",
      "airflow": false,
      "farm": 0,
      "shed": true
    }
  ]
}
"""
const val FARMSTWO = """
    {
  "farms": [
    {
      "id": 1,
      "name": "Farm",
      "farmsteads": [30, 300],
      "fields": [40, 13, 22, 24],
      "plantations": [18],
      "machines": [
        {
          "id": 1,
          "name": "1",
          "actions": ["SOWING", "MOWING"],
          "plants": ["OAT", "CHERRY"],
          "duration": 4,
          "location": 300
        },
        {
          "id": 2,
          "name": "2",
          "actions": ["IRRIGATING", "WEEDING"],
          "plants": ["OAT"],
          "duration": 4,
          "location": 300
        },
        {
          "id": 3,
          "name": "3",
          "actions": ["IRRIGATING", "MOWING"],
          "plants": ["POTATO", "CHERRY"],
          "duration": 3,
          "location": 30
        }
      ],
      "sowingPlans": [
        {
          "id": 0,
          "tick": 0,
          "plant": "OAT",
          "fields": [13, 22]
        }
      ]
    }
  ]
}
"""

class MoistureReductionTests {

    @Test
    fun testSimpleMoistureReduction() {
        val mapFile = File.createTempFile("testmap", ".json")
        mapFile.writeText(MAPSTWO)
        val farmFile = File.createTempFile("testfarm", ".json")
        farmFile.writeText(FARMSTWO)
        val scenarioFile = File.createTempFile("testscenario", ".json")
        scenarioFile.writeText(SCENARIO)

        val parser = Parser()
        val simData =
            parser.parse(listOf(mapFile.absolutePath, farmFile.absolutePath, scenarioFile.absolutePath))
        val simulation = Simulation(simData, 1, 17)
        simulation.run()

        val plantableTiles = simData.map.getPlantableTiles()
        val fieldTiles = simData.map.filterByType(TileType.FIELD, plantableTiles)
        val plantationTiles = simData.map.filterByType(TileType.PLANTATION, plantableTiles)

        for (tile in fieldTiles) {
            assert(tile.currentMoisture == 0)
        }
        for (tile in plantationTiles) {
            assert(tile.currentMoisture == 800)
        }
    }
}

 */
