package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests city expansion then machine goes to harvest then breaks
 */
class ExpansionStuck : ExampleSystemTestExtension() {
    override val name = "ExpansionStuck"
    override val description = "Tests that a machine that is broken and then sown is still broken"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "ExpansionStuck/farm.json"
    override val scenario = "ExpansionStuck/scenario.json"
    override val map = "ExpansionStuck/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 14

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 0 of type CITY_EXPANSION happened and affected tiles 2.")

        val hi = """
            [IMPORTANT] Incident: Incident 2 of type BROKEN_MACHINE happened and affected tiles 2.
            [IMPORTANT] Simulation Info: Simulation ended at tick 4.
            [IMPORTANT] Simulation Info: Simulation statistics are calculated.
            [IMPORTANT] Simulation Statistics: Farm 0 collected 209952 g of harvest.
            [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 209952 g.
            [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
            [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 209952 g.
        """.trimIndent()

        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError(
                "End of log reached when there should be more,\n" +
                    "expected: " + startString
            )
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
