package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 *  Sowing and drought in one tick
 */
class SowingAndDroughtInOneTick : ExampleSystemTestExtension() {
    override val name = "SowingAndDroughtInOneTick"
    override val description = "Tests what happens if a field is sown and then a drought occurs in the same tick."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "SowingDrought/farms.json"
    override val scenario = "SowingDrought/scenario.json"
    override val map = "SowingDrought/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 19

    val hi = """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farms.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 19 within the year.
[INFO] Simulation Info: Tick 0 started at tick 19 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 2 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 0.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 0.
[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 0 g of WHEAT.
[INFO] Simulation Info: Tick 1 started at tick 20 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 0.
[IMPORTANT] Simulation Info: Simulation ended at tick 2.
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
