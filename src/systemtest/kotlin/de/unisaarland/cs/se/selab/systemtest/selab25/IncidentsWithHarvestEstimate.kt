package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val MACHINE_TWO_END = "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1."
const val FARM_FINISH = "[IMPORTANT] Farm: Farm 1 finished its actions."

/**
 * tests default behavior of machines
 */
class IncidentsWithHarvestEstimateOneTick : ExampleSystemTestExtension() {
    override val name = "Incidents With Harvest Estimate One Tick"
    override val description = "Tests bee happy and animal attack on plantation plants and HE"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "harvestEstimation/farms.json"
    override val scenario = "harvestEstimation/scenario.json"
    override val map = "harvestEstimation/map.json"

    override val logLevel = "INFO"
    override val maxTicks = 1
    override val startYearTick = 9

    override suspend fun run() {
        skipLines(5)
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 3,4,10.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 4,10.")
        assertNextLine("[IMPORTANT] Incident: Incident 3 of type BEE_HAPPY happened and affected tiles 3,4,10.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1389960 g of CHERRY.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 780000 g of GRAPE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1435480 g of GRAPE.")
        skipLines(1) // simulation end
        assertNextLine("[IMPORTANT] Simulation Info: Simulation Statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.")
        skipLines(8)
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: " +
                "Total harvest estimate still in fields and plantations: 3605440 g."
        )
    }
}
