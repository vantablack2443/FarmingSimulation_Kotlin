package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.simulation.SimulationData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class FarmParserTest {

    @Test
    fun testParseValidFarmConfig() {
        val jsonString = """
        {
          "farms": [
            {
              "id": 0,
              "name": "Very Cool Farm",
              "farmsteads": [0],
              "fields": [2],
              "plantations": [1],
              "machines": [
                {
                  "id": 0,
                  "name": "Tractor",
                  "actions": ["SOWING", "IRRIGATING"],
                  "plants": ["PUMPKIN", "WHEAT"],
                  "duration": 4,
                  "location": 0
                }
              ],
              "sowingPlans": [
              ]
            }
          ]
        }

        """.trimIndent()

        val tempFile = File.createTempFile("testFarm", ".json")
        tempFile.writeText(jsonString)
        val simData = SimulationData()
        val parser = FarmParser(simData)
        parser.parse(tempFile.absolutePath)
        tempFile.deleteOnExit()
        val farm = simData.getFarms()

        assertEquals(1, farm.size)

        val farm1 = farm[0]
        assertEquals(0, farm1.getId())
        assertEquals("Very Cool Farm", farm1.getName())
        assertEquals(1, farm1.getFarmstead().size)
        assertEquals(1, farm1.getFields().size)
        assertEquals(1, farm1.getPlantation().size)
        assertEquals(1, farm1.getMachines().size)
        assertEquals(0, farm1.getMachines()[0].id)
        assertEquals("Tractor", farm1.getMachines()[0].name)
        assertEquals(4, farm1.getMachines()[0].duration)
        assertEquals(2, farm1.getMachines()[0].actions.size)
    }
}
