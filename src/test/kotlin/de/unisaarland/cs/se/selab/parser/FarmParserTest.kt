package de.unisaarland.cs.se.selab.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FarmParserTest {

    @Test
    fun testParseValidFarmConfig() {

        val jsonString = """
        {
            "farms": [
                {
                    "id": 1,
                    "name": "Sunny Farm",
                    "initialMoney": 1000,
                    "startingTileID": 10
                },
                {
                    "id": 2,
                    "name": "Green Acres",
                    "initialMoney": 1500,
                    "startingTileID": 20
                }
            ]
        }
    """.trimIndent()

        val simData = SimulationData()
        val farmParser = FarmParser(simData)
        farmParser.parseFromString(jsonString)

        val farms = simData.farms
        assertEquals(2, farms.size)

        val farm1 = farms[0]
        assertEquals(1, farm1.id)
        assertEquals("Sunny Farm", farm1.name)
        assertEquals(1000, farm1.initialMoney)
        assertEquals(10, farm1.startingTileID)

        val farm2 = farms[1]
        assertEquals(2, farm2.id)
        assertEquals("Green Acres", farm2.name)
        assertEquals(1500, farm2.initialMoney)
        assertEquals(20, farm2.startingTileID)
    }

}
