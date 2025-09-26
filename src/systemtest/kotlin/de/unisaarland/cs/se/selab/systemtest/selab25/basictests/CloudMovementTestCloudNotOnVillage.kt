package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class CloudMovementTestCloudNotOnVillage : ExampleSystemTestExtension() {
    override val name = "CloudMovementTestCloudNotOnVillage"
    override val description = "Tests configs for cloud movement test."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "cloudMoveTest/exampleFarms.json"
    override val scenario = "cloudMoveTest/exampleScenarioCloudNotOnVillage.json"
    override val map = "cloudMoveTest/exampleMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: exampleMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: exampleFarms.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info:" +
                " exampleScenarioCloudNotOnVillage.json successfully parsed and validated."
        )
        // assertNextLine("[INFO] Simulation Info: Simulation started at tick 1 within the year.")
        // assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 1 within the year.")
    }
}
