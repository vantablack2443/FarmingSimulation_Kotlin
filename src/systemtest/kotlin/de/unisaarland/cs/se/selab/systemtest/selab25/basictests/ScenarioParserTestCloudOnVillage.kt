package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Does what it says
 */
class ScenarioParserTestCloudOnVillage : ExampleSystemTestExtension() {
    override val name = "ScenarioParserTestCloudOnVillage"
    override val description = "Tests Scenario Validation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "MutantParserTest/Farm3.json"
    override val scenario = "MutantParserTest/Scenario4.json"
    override val map = "MutantParserTest/Map4.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: Map4.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: Farm3.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: Scenario4.json is invalid.")
    }
}
