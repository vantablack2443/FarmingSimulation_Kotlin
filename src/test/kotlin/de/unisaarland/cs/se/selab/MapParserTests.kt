package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.simulation.SimulationData
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class MapParserTests {

    @Test
    fun testSimpleMapParsing() {
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
        }
      ]
    }
    """
        val tempFile = File.createTempFile("testmap", ".json")
        tempFile.writeText(jsonString)
        val simData = SimulationData()
        val parser = MapParser(simData)
        parser.parse(tempFile.absolutePath)
        tempFile.deleteOnExit()
        val map = simData.map

        val farmstead = map.getTileByID(0)
        assertEquals(1, farmstead?.location?.x)
        assertEquals(1, farmstead?.location?.y)
        assertEquals(TileType.FARMSTEAD, farmstead?.category)
        assertEquals(0, farmstead?.farmID)
        assertEquals(true, farmstead?.shed)
        assertEquals(false, farmstead?.airflow)
    }

    @Test
    fun testDontGrowOnSquare() {
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
        val tempFile = File.createTempFile("testmap", ".json")
        tempFile.writeText(jsonString)
        val simData = SimulationData()
        val parser = MapParser(simData)
        parser.parse(tempFile.absolutePath)
        tempFile.deleteOnExit()
        val map = simData.map

        val farmstead = map.getTileByID(0)
        val field = map.getTileByID(1)
        assertFalse(farmstead?.shape == TileShape.OCTAGONAL)
        assertFalse(field?.shape == TileShape.SQUARE)
    }

    @Test
    fun testNoWildlifeNearMyCity() {
        val jsonString = """
    {
      "tiles": [
        {
          "id": 0,
          "coordinates": { "x": 1, "y": 1 },
          "category": "VILLAGE"
        },
        {
          "id": 1,
          "coordinates": { "x": 2, "y": 2 },
          "category": "FOREST",
          "airflow": false
        }
      ]
    }
    """
        val tempFile = File.createTempFile("testmap", ".json")
        tempFile.writeText(jsonString)
        val simData = SimulationData()
        val parser = MapParser(simData)

        assertThrows(de.unisaarland.cs.se.selab.parser.ValidationException::class.java) {
            parser.parse(tempFile.absolutePath)
        }

        tempFile.deleteOnExit()
    }
}
