package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class CloudMovementTest : ExampleSystemTestExtension() {
    override val name = "CloudMovementTest"
    override val description = "Tests cloud movement phase."

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
            "[INFO] Initialization Info: " +
                "exampleScenarioCloudNotOnVillage.json successfully parsed and validated."
        )
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 1 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 1 within the year.")
        // skipLines(1)
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 10 to tile 8.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 2 dissipates on tile 8.")

        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 3 on tile 13 rained down 70 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 3 with 5430 L water moved from tile 13 to tile 22.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 13, the amount of sunlight is 95.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 7 and 3 united to cloud 11" +
                " with 8930 L water and duration 2 on tile 22."
        )
        // move cloud 5
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 5 on tile 18 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 5 with 5400 L water moved from tile 18 to tile 17.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 95.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 5 on tile 17 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 5 with 5300 L water moved from tile 17 to tile 150.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 17, the amount of sunlight is 95.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 5 on tile 150 rained down 80 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 5 with 5220 L water moved from tile 150 to tile 3.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 150, the amount of sunlight is 95.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 5 got stuck on tile 3.")

        // cloud 10 stuck
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 10 on tile 11 rained down 70 L water.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 11 on tile 22 rained down 8930 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 11 dissipates on tile 22.")

        // end of cloud phase
        assertNextLine("[DEBUG] Cloud Position: Cloud 10 is on tile 11, where the amount of sunlight is 48.")
        skipLines(3) // skip farm start and end
        // cloud creation incident
        assertNextLine(
            "[IMPORTANT] Incident: Incident 140 of type CLOUD_CREATION happened " +
                "and affected tiles 4,5,6,7,8,10,11,12,13,14,21,22,40,80."
        )
        // merge
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 10 and 18 united to cloud 19" +
                " with 11930 L water and duration 3 on tile 11."
        )

        assertNextLine(
            "[IMPORTANT] Incident: Incident 150 of type CLOUD_CREATION happened " +
                "and affected tiles 116."
        )
    }
}
