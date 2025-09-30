package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Actions missed correct order
 */
class ActionsMissedCorrectOrder : ExampleSystemTestExtension() {
    override val name = "ActionsMissedCorrectOrder"
    override val description = "Tests that actions are executed in the correct order when some actions are missed."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "ActionsMissedOrder/farms.json"
    override val scenario = "ActionsMissedOrder/scenario.json"
    override val map = "ActionsMissedOrder/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 16

    override suspend fun run() {
        val lineIterator = result().lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun result(): String {
        return """
     [INFO] Initialization Info: map.json successfully parsed and validated.
     [INFO] Initialization Info: farm.json successfully parsed and validated.
     [IMPORTANT] Initialization Info: scenario.json is invalid.
        """.trimIndent()
    }
}
