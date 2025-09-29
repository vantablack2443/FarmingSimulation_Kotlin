package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks if after harvesting plantation tiles lose moisture at 100L/tick
 */
class PlantationHarvestMoistureHundred : ExampleSystemTestExtension() {
    override val name = "PlantationHarvestMoistureHundred"
    override val description = "Checks if after harvesting plantation tiles lose moisture at 100L/tick"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "plantationHarvestMoisture/map.json"
    override val farms = "plantationHarvestMoisture/farms.json"
    override val scenario = "plantationHarvestMoisture/scenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 19

    /*
        ID 1: Capacity 250 -> 150, Moisture penalty 0
     */
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
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 800000 g of ALMOND harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 800000 g of ALMOND harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of ALMOND.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 20 within the year.")

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 0 g of ALMOND harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 0 g of ALMOND harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of ALMOND.")
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 21 within the year.")

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
    }
}
