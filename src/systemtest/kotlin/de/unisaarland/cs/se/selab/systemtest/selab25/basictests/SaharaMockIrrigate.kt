package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Sahara mock irrigate
 */
class SaharaMockIrrigate : ExampleSystemTestExtension() {
    override val name = "SaharaMockIrrigate"
    override val description = "Mock test with desert map and no farms, with irrigation"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "SaharaMock/farms.json"
    override val scenario = "SaharaMockIrrigate/scenario.json"
    override val map = "SaharaMock/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 8

    val hi = """
[INFO] Initialization Info: map.json successfully parsed and validated.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
