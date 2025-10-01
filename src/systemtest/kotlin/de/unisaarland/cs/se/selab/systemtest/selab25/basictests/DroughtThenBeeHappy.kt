package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * drought followed by a bee happy on the same tile in the same tick
 */
class DroughtThenBeeHappy : ExampleSystemTestExtension() {
    override val name = "DroughtThenBeeHappy"
    override val description = "Tests a drought followed by a bee happy on the same tile in the same tick."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "DTBH/farms.json"
    override val scenario = "DTBH/scenario.json"
    override val map = "DTBH/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 4

    override suspend fun run() {
        val expectedLine = "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        assert(skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == expectedLine)
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 157504 g."
        )
    }
}
