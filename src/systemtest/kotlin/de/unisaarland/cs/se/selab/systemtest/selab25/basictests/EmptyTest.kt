package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Does what it says
 */
class EmptyTest : ExampleSystemTestExtension() {
    override val name = "EmptyTest"
    override val description = "Tests Nothing."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "EmptySysTest/Farm1.json"
    override val scenario = "EmptySysTest/Scenario1.json"
    override val map = "EmptySysTest/Map1.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 0

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: exampleMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: exampleFarms.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: exampleFarms.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 0 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 0 within the year.")
        val expectedLine = "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        assert(skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == expectedLine)
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1700000 g."
        )
    }
}
