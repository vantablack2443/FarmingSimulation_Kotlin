package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * City expansion on a tile with pre-existing cloud
 */
class CityExpansionDissipation : ExampleSystemTestExtension() {
    override val name = "CityExpansionDissipation"
    override val description = "Tests City Expansion and Dissipation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "CityExpansionDissipation/farms.json"
    override val scenario = "CityExpansionDissipation/scenario.json"
    override val map = "CityExpansionDissipation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val lineIterator = result().lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun result(): String {
        return """
            asdasd
        """.trimIndent()
    }
}
