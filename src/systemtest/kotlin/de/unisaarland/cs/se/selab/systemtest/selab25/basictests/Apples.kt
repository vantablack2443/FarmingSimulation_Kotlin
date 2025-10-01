package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test a normal cycle of planting and harvesting apple
 */
class Apples : ExampleSystemTestExtension() {
    override val name = "AppleNormalCycle"
    override val description = "Test a normal cycle of planting and harvesting apple"

    override val farms = "AppleNormalCycle/farms.json"
    override val scenario = "AppleNormalCycle/scenario.json"
    override val map = "AppleNormalCycle/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 13
    override val startYearTick = 4

    val hi = """
        [INFO] Initialization Info: map.json successfully parsed and validated.
        [INFO] Initialization Info: farms.json successfully parsed and validated.
        [INFO] Initialization Info: scenario.json successfully parsed and validated.
        [INFO] Simulation Info: Simulation started at tick 4 within the year.
        [INFO] Simulation Info: Tick 0 started at tick 4 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 1 for 2 days.
        [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1377000 g of APPLE.
        [INFO] Simulation Info: Tick 1 started at tick 5 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1003833 g of APPLE.
        [INFO] Simulation Info: Tick 2 started at tick 6 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 731793 g of APPLE.
        [INFO] Simulation Info: Tick 3 started at tick 7 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 533475 g of APPLE.
        [INFO] Simulation Info: Tick 4 started at tick 8 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 388902 g of APPLE.
        [INFO] Simulation Info: Tick 5 started at tick 9 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 255157 g of APPLE.
        [INFO] Simulation Info: Tick 6 started at tick 10 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 167407 g of APPLE.
        [INFO] Simulation Info: Tick 7 started at tick 11 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 0 performs MOWING on tile 1 for 2 days.
        [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 109835 g of APPLE.
        [INFO] Simulation Info: Tick 8 started at tick 12 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 72061 g of APPLE.
        [INFO] Simulation Info: Tick 9 started at tick 13 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 2 days.
        [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 47277 g of APPLE.
        [INFO] Simulation Info: Tick 10 started at tick 14 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 31017 g of APPLE.
        [INFO] Simulation Info: Tick 11 started at tick 15 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 20349 g of APPLE.
        [INFO] Simulation Info: Tick 12 started at tick 16 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 13349 g of APPLE.
        [INFO] Simulation Info: Tick 13 started at tick 17 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 0 has collected 13349 g of APPLE harvest.
        [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
        [IMPORTANT] Farm Machine: Machine 0 unloads 13349 g of APPLE harvest in the shed.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [INFO] Simulation Info: Tick 14 started at tick 18 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Simulation Info: Simulation ended at tick 15.
        [IMPORTANT] Simulation Info: Simulation statistics are calculated.
        [IMPORTANT] Simulation Statistics: Farm 0 collected 13349 g of harvest.
        [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 13349 g.
        [IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
        [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
