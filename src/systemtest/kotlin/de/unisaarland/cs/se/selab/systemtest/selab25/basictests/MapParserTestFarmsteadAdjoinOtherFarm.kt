package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Does what it says
 */
class MapParserTestFarmsteadAdjoinOtherFarm : ExampleSystemTestExtension() {
    override val name = "MapParserTestFarmsteadAdjoinOtherFarm"
    override val description = "Tests Farm Validation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "MutantParserTest/Farm1.json"
    override val scenario = "MutantParserTest/Scenario1.json"
    override val map = "MutantParserTest/Map1.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Initialization Info: Map1.json is invalid.")
    }
}
