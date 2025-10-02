package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.simulation.SimulationData
import org.junit.jupiter.api.Assertions
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
          "airflow": false,
          "farm": 0,
          "shed": true
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
        Assertions.assertFalse(farmstead?.shape == TileShape.OCTAGONAL)
        Assertions.assertFalse(field?.shape == TileShape.SQUARE)
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

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tempFile.absolutePath)
        }

        tempFile.deleteOnExit()
    }

    @Test
    fun `tile id missing validation test`() {
        val json = """
        { "tiles": [ { "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" } ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `tile id not missing validation test`() {
        val json = """
        { "tiles": [ { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" } ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertDoesNotThrow { parser.parse(tmp.absolutePath) }
    }

    @Test
    fun `tile category missing validation test`() {
        val json = """
        { "tiles": [ { "id": 5, "coordinates": { "x": 1, "y": 1 } } ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `tile category not missing validation test`() {
        val json = """
        { "tiles": [ { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" } ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertDoesNotThrow { parser.parse(tmp.absolutePath) }
    }

    @Test
    fun `tile coordinates missing validation test`() {
        val json = """
        { "tiles": [ { "id": 5, "category": "VILLAGE" } ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `tile coordinates not missing validation test`() {
        val json = """
        { "tiles": [ { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" } ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertDoesNotThrow { parser.parse(tmp.absolutePath) }
    }

    @Test
    fun `parseTiles valid test`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" },
            { "id": 2, "coordinates": { "x": 2, "y": 2 }, "category": "VILLAGE" }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertDoesNotThrow { parser.parse(tmp.absolutePath) }
    }

    @Test
    fun `parseTiles invalid test same id`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" },
            { "id": 1, "coordinates": { "x": 2, "y": 2 }, "category": "VILLAGE" }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `parseTiles invalid test same location`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" },
            { "id": 2, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE" }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `airflow on village`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "VILLAGE", "airflow": true }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `airflow missing`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "ROAD" }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `airflow missing direction`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 1, "y": 1 }, "category": "ROAD", "airflow": true }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `forest neighbour test`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 0, "y": 0 }, "category": "VILLAGE" },
            { "id": 0, "coordinates": { "x": 2, "y": 0 }, "category": "FOREST", "airflow": false }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }

    @Test
    fun `Plantation validation test missing plant`() {
        val json = """
        { "tiles": [ 
            { "id": 1, "coordinates": { "x": 0, "y": 0 }, "category": "PLANTATION" }
        ] }
        """.trimIndent()

        val tmp = File.createTempFile("testmap", ".json").apply {
            writeText(json)
            deleteOnExit()
        }

        val sim = SimulationData()
        val parser = MapParser(sim)

        Assertions.assertThrows(ValidationException::class.java) {
            parser.parse(tmp.absolutePath)
        }
    }
}
