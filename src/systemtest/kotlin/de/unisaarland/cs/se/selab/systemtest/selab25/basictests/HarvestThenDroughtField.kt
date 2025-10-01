package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests a harvest followed by a drought
 */
class HarvestThenDroughtField : ExampleSystemTestExtension() {
    override val name = "HarvestThenDroughtField"
    override val description = "Tests a harvest followed by a drought for fields."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "HarvestThenDrought/farms1.json"
    override val scenario = "HarvestThenDrought/scenario1.json"
    override val map = "HarvestThenDrought/map1.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 11
    override val startYearTick = 7

    val lines = """
[INFO] Initialization Info: map1.json successfully parsed and validated.
[INFO] Initialization Info: farms1.json successfully parsed and validated.
[INFO] Initialization Info: scenario1.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 7 within the year.
[INFO] Simulation Info: Tick 0 started at tick 7 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 0.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Simulation Info: Tick 1 started at tick 8 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Simulation Info: Tick 2 started at tick 9 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 1 for 4 days.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 900000 g of POTATO.
[INFO] Simulation Info: Tick 3 started at tick 10 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 810000 g of POTATO.
[INFO] Simulation Info: Tick 4 started at tick 11 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 1 for 4 days.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 729000 g of POTATO.
[INFO] Simulation Info: Tick 5 started at tick 12 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 4 days.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 656100 g of POTATO.
[INFO] Simulation Info: Tick 6 started at tick 13 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 1 for 4 days.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 590490 g of POTATO.
[INFO] Simulation Info: Tick 7 started at tick 14 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 531441 g of POTATO.
[INFO] Simulation Info: Tick 8 started at tick 15 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 1 for 4 days.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Simulation Info: Tick 9 started at tick 16 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[INFO] Simulation Info: Tick 10 started at tick 17 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 4 days.
[IMPORTANT] Farm Harvest: Machine 0 has collected 531441 g of POTATO harvest.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm Machine: Machine 0 unloads 531441 g of POTATO harvest in the shed.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1.
[IMPORTANT] Simulation Info: Simulation ended at tick 11.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 0 collected 531441 g of harvest.
[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 531441 g.
[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = lines.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
