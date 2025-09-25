package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

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
        assertNextLine("[INFO] Initialization Info: farmsForSameTiles. successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        val expectedLine = "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        assert(skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == expectedLine)
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        skipLines(1)
    }
}
