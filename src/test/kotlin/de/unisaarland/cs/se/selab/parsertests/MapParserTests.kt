package de.unisaarland.cs.se.selab.parsertests

import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.simulation.SimulationData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MapParserTests {

    @Test
    fun testExample() {
        val jsonString = """
        {
  "tiles": [
    {
      "id": 0,
      "coordinates": { "x": 1, "y": 1 },
      "category": "FARMSTEAD",
      "farm": 0,
      "shed": true,
      "airflow": false
    },
    {
      "id": 1,
      "coordinates": { "x": 0, "y": 2 },
      "category": "PLANTATION",
      "farm": 0,
      "capacity": 8000,
      "plant": "APPLE",
      "airflow": false
    },
    {
      "id": 2,
      "coordinates": { "x": 2, "y": 2 },
      "category": "FIELD",
      "farm": 0,
      "capacity": 10000,
      "possiblePlants": ["PUMPKIN", "WHEAT"],
      "airflow": false
    }
  ]
}
        """
        val simData = SimulationData()
        val parser = MapParser(simData)

        // When
        parser.parse(jsonString)
        val map = simData.map

        val farmstead = map.getTileByID(0)
        assertEquals(1, farmstead.coordinates.x)
        assertEquals(1, farmstead.coordinates.y)
        assertEquals("FARMSTEAD", farmstead.category)
        assertEquals(0, farmstead.farm)
        assertEquals(true, farmstead.shed)
        assertEquals(false, farmstead.airflow)

        val plantation = map.getTileByID(1)
        assertEquals(0, plantation.coordinates.x)
        assertEquals(2, plantation.coordinates.y)
        assertEquals("PLANTATION", plantation.category)
        assertEquals(0, plantation.farm)
        assertEquals(8000, plantation.capacity)
        assertEquals("APPLE", plantation.plant)
        assertEquals(false, plantation.airflow)

        val field = map.getTileByID(2)
        assertEquals(2, field.coordinates.x)
        assertEquals(2, field.coordinates.y)
        assertEquals("FIELD", field.category)
        assertEquals(0, field.farm)
        assertEquals(10000, field.capacity)
        assertTrue(field.possiblePlants.contains("PUMPKIN"))
        assertTrue(field.possiblePlants.contains("WHEAT"))
        assertEquals(false, field.airflow)
    }
}
