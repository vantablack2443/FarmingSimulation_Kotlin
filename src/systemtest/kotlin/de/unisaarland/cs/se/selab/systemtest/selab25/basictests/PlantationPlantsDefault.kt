package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val MACHINE_TWO_END = "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1."
const val FARM_FINISH = "[IMPORTANT] Farm: Farm 1 finished its actions."

/**
 * tests default behavior of machines
 */
class PlantationPlantsDefault : ExampleSystemTestExtension() {
    override val name = "PlantationPlantsDefault"
    override val description = "Tests plantation plant actions"

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
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 799950 g of ALMOND.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 5 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1139952 g of GRAPE.")
        skipLines(1)
        // year tick 18
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 3 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 799950 g of ALMOND harvest.")
        assertNextLine(MACHINE_TWO_END)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 799950 g of ALMOND harvest in the shed.")
        assertNextLine(FARM_FINISH)
        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 5 were not performed:" +
                " IRRIGATING,HARVESTING."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1082859 g of GRAPE.")
        skipLines(4)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 5 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 1082859 g of GRAPE harvest.")
        assertNextLine(MACHINE_TWO_END)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 1082859 g of GRAPE harvest in the shed.")
        assertNextLine(FARM_FINISH)
        skipLines(1) // simulation end
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 3582809 g of harvest.")
        skipLines(4)
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 1700000 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 1082859 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 799950 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
    }
}
