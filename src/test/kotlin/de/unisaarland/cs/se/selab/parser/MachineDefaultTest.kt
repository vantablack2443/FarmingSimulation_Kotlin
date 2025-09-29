package de.unisaarland.cs.se.selab.parser

import kotlin.test.Test
import kotlin.test.assertNotNull

class MachineDefaultTest {

    @Test
    fun testParserWithSimpleConfigs() {
        val mapJsonPath = "src/systemtest/resources/machineActionsTest/mapMachineDefault.json"
        val farmJsonPath = "src/systemtest/resources/machineActionsTest/farmsMachineDefault.json"
        val scenarioJsonPath = "src/systemtest/resources/machineActionsTest/scenarioMachineDefault.json"

        val parser = Parser()
        val result = parser.parse(listOf(mapJsonPath, farmJsonPath, scenarioJsonPath))
        assertNotNull(result, "Parsing returned null SimulationData")

//        assertFailsWith<Exception> {
//            parser.parse(listOf(mapJsonPath, farmJsonPath, scenarioJsonPath))
//        }
    }
}
