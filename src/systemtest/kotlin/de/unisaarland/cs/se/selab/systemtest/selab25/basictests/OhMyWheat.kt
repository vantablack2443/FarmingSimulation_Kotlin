package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * Does what it says
 */
class OhMyWheat : ExampleSystemTestExtension() {
    override val name = "OhMyWheat"
    override val description = "tests the entire wheat growth cycle"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "AllWheat/farm.json"
    override val scenario = "AllWheat/scenario.json"
    override val map = "AllWheat/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 48
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
 
        """.trimIndent()
    }
}
