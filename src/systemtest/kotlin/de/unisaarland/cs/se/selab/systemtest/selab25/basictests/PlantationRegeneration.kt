package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class PlantationRegeneration : ExampleSystemTestExtension() {
    override val name = "Plantation Regeneration"
    override val description = "Tests plantation HE reset in november"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantationPlantTests/farmsRegeneration.json"
    override val scenario = "plantationPlantTests/scenarioRegeneration.json"
    override val map = "plantationPlantTests/mapRegeneration.json"

    override val logLevel = "INFO"
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        skipLines(5)
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to shed at tile 1.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 3 for 4 days.")
        assertNextLine(
            "[IMPORTANT] Farm Harvest: Machine 2 has collected 1028850 g" +
                " of GRAPE harvest."
        )
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to shed at tile 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 1028850 g of GRAPE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 4 for 4 days.")
        assertNextLine(
            "[IMPORTANT] Farm Harvest: Machine 3 has collected 850000 g" +
                " of APPLE harvest."
        )
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to shed at tile 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 unloads 850000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 0 g of APPLE.")
        // yeartick 21, reset HE
        skipLines(3)
    }
}
