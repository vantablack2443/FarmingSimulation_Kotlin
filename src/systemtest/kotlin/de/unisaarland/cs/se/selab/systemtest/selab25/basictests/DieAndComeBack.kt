package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests soil moisture going to 0 and irrigation bringing it back up
 */
class DieAndComeBack : ExampleSystemTestExtension() {
    override val name = "DieAndComeBack"
    override val description = "Tests soil moisture going to 0 and irrigation bringing it back up"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "justAction/farmForDieAndBack.json"
    override val scenario = "example/scenario.json"
    override val map = "justAction/mapOnlyPlantationFarm.json"

    override val logLevel = "INFO"
    override val maxTicks = 2
    override val startYearTick = 11

    override suspend fun run() {
        // --- Initialization Logs (Assuming success) ---
        assertNextLine("[INFO] Initialization Info: mapOnlyPlantationFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmForDieAndBack.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 11 within the year.")

        // --- TICK 0 (Year Tick 11: Early June) ---
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 11 within the year.")

        // 1. Moisture Reduction: Tile 2 (8 L capacity) immediately drops below the 100 L requirement.
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.")

        // 2. Farm Actions (Farm 0)
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
//        assertNextLine(
//            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
//        )

        // IRRIGATING is prioritized for Plantation tiles (Tile 1 then 2). Duration 2 days.
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 2 days.")

        // Tile 2 moisture refills from 0 L up to max capacity (8 L) [3].

        // Machine returns to shed 0
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // 3. Harvest Estimate Calculation:
        // Harvest Estimate drops to 0g because the moisture level reached 0 L [2].
//        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: .")
        skipLines(2)
//        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of APPLE.")
//        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of APPLE.")

        // --- TICK 1 (Year Tick 12: Late June) ---
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 12 within the year.")

        // 1. Moisture Reduction: Tile 2 (8 L capacity) drops below threshold again (8L -> 0L).
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.")

        // 2. Farm Actions (Farm 0)
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
//        assertNextLine(
//            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
//        )

        // IRRIGATING is required because the harvest estimate is 0 g [3].
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 2 days.")

        // Machine returns to shed 0
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // 3. Harvest Estimate Calculation:
        skipLines(2)
        // Harvest estimate remains 0 g, as it is only reset annually in November (tick 21) [6].
//        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: .")

        // --- End of Simulation ---
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 2.")
    }
}
