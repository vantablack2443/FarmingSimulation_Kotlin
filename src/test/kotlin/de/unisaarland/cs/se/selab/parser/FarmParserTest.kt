package de.unisaarland.cs.se.selab.parser

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class FarmParserTest {

    @Test
    fun testParserWithSimpleConfigs() {
        val mapJsonPath = "src/systemtest/resources/farmParserTest/validMap.json"
        val farmJsonPath = "src/systemtest/resources/farmParserTest/invalidFarm.json"
        val scenarioJsonPath = "src/systemtest/resources/farmParserTest/validScenario.json"

        val parser = Parser()
        val result = parser.parse(listOf(mapJsonPath, farmJsonPath, scenarioJsonPath))
        assertNotNull(result, "Parsing returned null SimulationData")
    }

    @Test
    fun testMapWithNearbyFarms() {
        val mapJsonPath = "src/systemtest/resources/farmParserTest/farmsteadFightMap.json"
        val farmJsonPath = "src/systemtest/resources/farmParserTest/invalidFarm.json"
        val scenarioJsonPath = "src/systemtest/resources/farmParserTest/validScenario.json"

        val parser = Parser()
        assertFailsWith<Exception> {
            parser.parse(listOf(mapJsonPath, farmJsonPath, scenarioJsonPath))
        }
    }
}
