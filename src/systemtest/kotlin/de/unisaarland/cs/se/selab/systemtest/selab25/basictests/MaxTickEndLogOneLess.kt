package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests the simulation end log for max tick 1
 * Tests if log should be Simulation Info: Simulation ended at tick 0.
 */
class MaxTickEndLogOneLess : ExampleSystemTestExtension() {
    override val name = "Max Tick End Log One Less"
    override val description = "Tests the simulation end log for max tick 1 - Log Tick = 0"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "maxTickEndLog/map.json"
    override val farms = "maxTickEndLog/farms.json"
    override val scenario = "maxTickEndLog/scenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: " +
                "scenario.json successfully parsed and validated."
        )
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 17 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 17 within the year.")

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1200000 g of CHERRY.")

        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 0.")
    }
}
