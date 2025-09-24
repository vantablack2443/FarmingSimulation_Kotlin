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

}