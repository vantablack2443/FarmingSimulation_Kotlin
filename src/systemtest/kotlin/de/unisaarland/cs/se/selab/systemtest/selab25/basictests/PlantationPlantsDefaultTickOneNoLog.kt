package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class PlantationPlantsDefaultTickOneNoLog : ExampleSystemTestExtension() {
    override val name = "PlantationPlantsDefaultTickOneNoLog"
    override val description = "Tests if missed actions should be logged after harvesting"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantationPlantTests/farms.json"
    override val scenario = "plantationPlantTests/scenario.json"
    override val map = "plantationPlantTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 17

    override suspend fun run() {
        skipLines(5)
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 2 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 1700000 g of APPLE harvest.")
        assertNextLine(MACHINE_TWO_END)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 1700000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs MOWING on tile 3 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 1.")
        assertNextLine(FARM_FINISH)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of APPLE.")
    }
}
