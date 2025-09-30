package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Machine behavior test
 */
class MachineBehaviorTest : ExampleSystemTestExtension() {
    override val name = "MachineBehaviorTest"
    override val description = "Tests the behavior of machines throughout ticks."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineActionsTest/machineBehaviorTest/farms.json"
    override val scenario = "machineActionsTest/machineBehaviorTest/scenario.json"
    override val map = "machineActionsTest/machineBehaviorTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 8

    val lines = """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farms.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 1 within the year.
[INFO] Simulation Info: Tick 0 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 1530000 g of APPLE.
[INFO] Simulation Info: Tick 1 started at tick 2 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 1377000 g of APPLE.
[INFO] Simulation Info: Tick 2 started at tick 3 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm Action: Machine 1 performs CUTTING on tile 6 for 2 days.
[IMPORTANT] Farm Action: Machine 1 performs CUTTING on tile 12 for 2 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 16.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 1115370 g of APPLE.
[INFO] Simulation Info: Tick 3 started at tick 4 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 903449 g of APPLE.
[INFO] Simulation Info: Tick 4 started at tick 5 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles 6.
[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 816000 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 658613 g of APPLE.
[INFO] Simulation Info: Tick 5 started at tick 6 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 2 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 0.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 16.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1080000 g of OAT.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 480127 g of APPLE.
[INFO] Simulation Info: Tick 6 started at tick 7 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 3 for 2 days.
[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 874800 g of OAT.
[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: MOWING.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 1080000 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 350011 g of APPLE.
[INFO] Simulation Info: Tick 7 started at tick 8 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 3 for 2 days.
[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 6.
[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 708588 g of OAT.
[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 734400 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 255157 g of APPLE.
[INFO] Simulation Info: Tick 8 started at tick 9 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 3 for 2 days.
[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 516560 g of OAT.
[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 660960 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 167407 g of APPLE.
[INFO] Simulation Info: Tick 9 started at tick 10 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 376571 g of OAT.
[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 594864 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 109835 g of APPLE.
[IMPORTANT] Simulation Info: Simulation ended at tick 10.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.
[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2161270 g.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = lines.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
