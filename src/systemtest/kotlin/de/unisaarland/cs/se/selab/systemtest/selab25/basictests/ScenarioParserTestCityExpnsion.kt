package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Does what it says
 */
class ScenarioParserTestCityExpnsion : ExampleSystemTestExtension() {
    override val name = "ScenarioParserTestCityExpnsion"
    override val description = "Tests Scenario Validation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "MutantParserTest/Farm7.json"
    override val scenario = "MutantParserTest/Scenario7.json"
    override val map = "MutantParserTest/Map7.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: Map7.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: Farm7.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: Scenario7.json successfully parsed and validated.")
    }
}
