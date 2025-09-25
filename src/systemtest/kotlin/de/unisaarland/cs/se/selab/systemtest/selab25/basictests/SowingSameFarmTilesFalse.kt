package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests if sowing plan tiles specified by location and radius should belong to the same farm
 */
class SowingSameFarmTilesFalse : ExampleSystemTestExtension() {
    override val name = "Sowing Plan Fields Same Farm tiles"
    override val description = "Tests sowing plan parsing"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "farmParserTests/farmsForSameTiles.json"
    override val scenario = "farmParserTests/scenario.json"
    override val map = "farmParserTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmsForSameTiles.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
    }
}
