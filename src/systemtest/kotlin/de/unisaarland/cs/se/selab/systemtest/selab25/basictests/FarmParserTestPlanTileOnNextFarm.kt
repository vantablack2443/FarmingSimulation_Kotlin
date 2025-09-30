package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Does what it says
 */
class FarmParserTestPlanTileOnNextFarm : ExampleSystemTestExtension() {
    override val name = "FarmParserTestPlanTileOnNextFarm"
    override val description = "Tests Farm Validation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "MutantParserTest/Farm6.json"
    override val scenario = "MutantParserTest/Scenario1.json"
    override val map = "MutantParserTest/Map6.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: Map6.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: Farm6.json is invalid.")
    }
}
