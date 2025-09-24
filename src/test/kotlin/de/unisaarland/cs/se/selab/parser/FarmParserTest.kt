package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.simulation.SimulationData
import org.junit.jupiter.api.Assertions.*
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

}