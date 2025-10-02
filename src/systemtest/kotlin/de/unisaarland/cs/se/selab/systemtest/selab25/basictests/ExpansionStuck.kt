package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

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
    override val maxTicks = 3
    override val startYearTick = 16

    val hi = """

    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
