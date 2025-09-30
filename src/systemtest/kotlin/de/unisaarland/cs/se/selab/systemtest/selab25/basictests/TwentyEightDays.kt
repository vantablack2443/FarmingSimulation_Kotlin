package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Twenty eight ticks
 */
class TwentyEightDays : ExampleSystemTestExtension() {
    override val name = "TwentyEightDays"
    override val description = "Simulation over 28 ticks"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "casual/farms.json"
    override val scenario = "casual/scenario.json"
    override val map = "casual/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 28
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        assert(skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == expectedLine)
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 296375 g."
        )
    }
}
