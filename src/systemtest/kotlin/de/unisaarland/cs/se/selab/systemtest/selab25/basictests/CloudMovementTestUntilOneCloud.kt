package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class CloudMovementTestUntilOneCloud : ExampleSystemTestExtension() {
    override val name = "CloudMovementTestUntilOneCloud"
    override val description = "Tests cloud movement test for a single cloud."

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
    }
}
