package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Drought system test
 */
class DroughtSystemTest : ExampleSystemTestExtension() {
    override val name = "DroughtTest"
    override val description = "Tests drought scenario over 5 ticks."

    override val map = "drought/map.json"
    override val farms = "drought/farms.json"
    override val scenario = "drought/scenario.json"

    override val startYearTick = 1
    override val maxTicks = 5
    override val logLevel = "DEBUG"

    override suspend fun run() {
        val tileIDs = listOf(27, 31, 37)
        // val expectedLine = "[IMPORTANT] Farm: Farm 0 finished its actions. 16\n"

        // assert(skipUntilLogType(LogLevel.IMPORTANT, LogType.FARM) == expectedLine)
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT\n" + "happened and affected tiles $tileIDs.")
    }
}
