package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests reduction in Moisture on plantable tiles
 */
class SoilMoistureBasicTest : ExampleSystemTestExtension() {
    override val name = "SoilMoistureBasicTest"
    override val description = "Tests basic moisture reduction on plantable tiles"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "soilMoistureBasic/map.json"
    override val farms = "soilMoistureBasic/farms.json"
    override val scenario = "soilMoistureBasic/scenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 5

    /*
    Three PLANTATIONs
        ID 1: Capacity 250 -> 150, Moisture penalty 0
        ID 4: Capacity 160 -> 60, Moisture penalty 0
        ID 9: Capacity 110 -> 10, Moisture Penalty 10%x1
     */
    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: " +
                "scenario.json successfully parsed and validated."
        )
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 5 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 5 within the year.")

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.")

        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1200000 g of CHERRY.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 4 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1200000 g of CHERRY.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 9 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 1199950 g of CHERRY.")
    }
}
