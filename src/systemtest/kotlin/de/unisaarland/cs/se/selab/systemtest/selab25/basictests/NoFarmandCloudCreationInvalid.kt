package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
class NoFarmandCloudCreationInvalid : ExampleSystemTestExtension() {
    override val name = "NoFarmandCloudCreationInvalid"
    override val description = "No farm phase and cloud creation is invalid"
    override val farms = "SimpleNoFarmandCloudCreationInvalid/farm.json"

    override val scenario = "SimpleNoFarmandCloudCreationInvalid/scenario.json"
    override val map = "SimpleNoFarmandCloudCreationInvalid/map.json"
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
private fun result() : String {
    return """
     [INFO] Initialization Info: map.json successfully parsed and validated.
     [INFO] Initialization Info: farm.json successfully parsed and validated.
     [IMPORTANT] Initialization Info: scenario.json is invalid.
    """.trimIndent()
    }
}