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
        [INFO] Simulation Info: Simulation started at tick 8 within the year.
        [INFO] Simulation Info: Tick 0 started at tick 8 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
        [IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 2 days.
        [IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 0.
        [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 622080 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 1239300 g of APPLE.
        [INFO] Simulation Info: Tick 1 started at tick 9 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 3 for 2 days.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 453496 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 720000 g of ALMOND.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 813104 g of APPLE.
        [INFO] Simulation Info: Tick 2 started at tick 10 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 3 for 2 days.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 330598 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 648000 g of ALMOND.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 533477 g of APPLE.
        [INFO] Simulation Info: Tick 3 started at tick 11 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 3 for 2 days.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 241005 g of OAT.
        [DEBUG] Harvest Estimate: Required actions on tile 6 were not performed: MOWING.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 524880 g of ALMOND.
        [DEBUG] Harvest Estimate: Required actions on tile 12 were not performed: MOWING.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 315012 g of APPLE.
        [INFO] Simulation Info: Tick 4 started at tick 12 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles .
        [INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 175692 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 472392 g of ALMOND.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 206679 g of APPLE.
        [INFO] Simulation Info: Tick 5 started at tick 13 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 3 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 2 has collected 175692 g of OAT harvest.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm Machine: Machine 2 unloads 175692 g of OAT harvest in the shed.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 425152 g of ALMOND.
        [DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: MOWING.
        [INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 1080000 g of GRAPE.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 135602 g of APPLE.
        [INFO] Simulation Info: Tick 6 started at tick 14 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 1 performs CUTTING on tile 7 for 2 days.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 382636 g of ALMOND.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 88968 g of APPLE.
        [INFO] Simulation Info: Tick 7 started at tick 15 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 0,5,6.
        [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 344372 g of ALMOND.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 58371 g of APPLE.
        [INFO] Simulation Info: Tick 8 started at tick 16 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 6 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 2 has collected 344372 g of ALMOND harvest.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm Machine: Machine 2 unloads 344372 g of ALMOND harvest in the shed.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 38297 g of APPLE.
        [INFO] Simulation Info: Tick 9 started at tick 17 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 7 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 2 has collected 1080000 g of GRAPE harvest.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm Machine: Machine 2 unloads 1080000 g of GRAPE harvest in the shed.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [DEBUG] Harvest Estimate: Required actions on tile 12 were not performed: MOWING.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 25126 g of APPLE.
        [IMPORTANT] Simulation Info: Simulation ended at tick 10.
        [IMPORTANT] Simulation Info: Simulation statistics are calculated.
        [IMPORTANT] Simulation Statistics: Farm 0 collected 1600064 g of harvest.
        [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 175692 g.
        [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 1080000 g.
        [IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 344372 g.
        [IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 25126 g.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = lines.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
