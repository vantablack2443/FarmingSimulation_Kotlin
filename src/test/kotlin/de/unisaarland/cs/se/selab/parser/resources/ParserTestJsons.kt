package de.unisaarland.cs.se.selab.parser.resources

const val MAP_JSON_CITYEXPANSIONADJOINING = """
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
      "category": "ROAD",
      "airflow": false
    },
    {
    "id": 3,
    "coordinates": { "x": 4, "y": 0 },
    "category": "FIELD",
    "airflow": false,
    "farm": 0,
    "capacity": 900,
    "possiblePlants": ["PUMPKIN", "WHEAT"]
    },
    {
    "id": 4,
    "coordinates": { "x": 6, "y": 0},
    "category": "PLANTATION",
    "airflow": false,
    "farm": 0,
    "capacity": 900,
    "plant": "ALMOND"
    },
    {
    "id": 5,
    "coordinates": { "x": 5, "y": -1},
    "category": "FARMSTEAD",
    "airflow": false,
    "farm": 0,
    "shed": true
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
      "farmsteads": [5],
      "fields": [3],
      "plantations": [4],
      "machines": [
      {
          "id": 0,
          "name": "Tractor",
          "actions": ["SOWING", "IRRIGATING"],
          "plants": ["PUMPKIN", "WHEAT"],
          "duration": 4,
          "location": 5
        }
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
const val SCENARIO_JSON_CLOUDONVILLAGE = """
    {
    "clouds": [
    {
        "id": 0,
        "location": 1,
        "duration": 4,
        "amount": 1000
        }
    ],
    "incidents": [
    ]
    }
"""
const val SCENARIO_JSON_CITYEXPANSIONADJOINING = """
{
  "clouds": [
  ],
  "incidents": [
    {
      "id": 0,
      "type": "CITY_EXPANSION",
      "tick": 0,
      "location": 0
    },
    {
    "id": 1,
    "type": "CITY_EXPANSION",
    "tick": 1,
    "location": 2
    }
  ]
}
"""
const val SCENARIO_JSON_CLOUDFINITE = """
    {
    "clouds": [
    {
        "id": 0,
        "location": 0,
        "duration": -1,
        "amount": 2000
        }
    ],
    "incidents": [
    {
      "id": 0,
      "type": "CLOUD_CREATION",
      "tick": 0,
      "location": 0,
      "radius": 0,
      "amount": 2000,
      "duration": 5
    }
    ]
    }
"""
