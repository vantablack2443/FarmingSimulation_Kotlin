package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests movement and logging during sowing Phase
 */
class SowingMovementBasicTest : ExampleSystemTestExtension() {
    override val name = "SowingMovementBasicTest"
    override val description = "Tests movement and logging during sowing Phase"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "sowingMovement/map.json"
    override val farms = "sowingMovement/farms.json"
    override val scenario = "sowingMovement/scenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: " +
                "scenario.json successfully parsed and validated."
        )
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 19 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 19 within the year.")

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 4 days.")
    }
}
