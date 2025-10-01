package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests harvesting logic
 */
class HarvestingLogic : ExampleSystemTestExtension() {
    override val name = "HarvestingLogic"
    override val description = "Tests harvesting logic"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "harvestingLogic/farms.json"
    override val scenario = "harvestingLogic/scenario.json"
    override val map = "harvestingLogic/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 20
    override val startYearTick = 16

    val hi = """
        asdasd
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
