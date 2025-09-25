package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class CloudMovementTestTwoClouds : ExampleSystemTestExtension() {
    override val name = "CloudMovementTestTwoClouds"
    override val description = "Tests cloud movement test for two clouds."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "cloudMoveTest/exampleFarms.json"
    override val scenario = "cloudMoveTest/exampleScenario.json"
    override val map = "cloudMoveTest/exampleMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedString = "Simulation Info: Simulation started."
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == expectedString)
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 1 within the year.")
        // skipLines(1)
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 got stuck on tile 3.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 10 to tile 8.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 2 dissipates on tile 8.")

        assertNextLine("[INFO] Cloud Rain: Cloud 3 on tile 13 rained down 70 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 3 with 5430 L water moved from tile 13 to tile 22.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 13, the amount of sunlight is 95.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 7 and 3 united to cloud 11" +
                " with 8930 L water and duration 2 on tile 22."
        )
    }
}
