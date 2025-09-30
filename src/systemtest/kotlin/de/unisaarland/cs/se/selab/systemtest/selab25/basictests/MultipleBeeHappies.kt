package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * multiple bee happies
 */
class MultipleBeeHappies : ExampleSystemTestExtension() {
    override val name = "MultipleBeeHappies"
    override val description = "Multiple bee happies"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "MultipleBeeHappies/farms.json"
    override val scenario = "MultipleBeeHappies/scenario.json"
    override val map = "MultipleBeeHappies/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 8

    val hi = """
        asdsad
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
