package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests broken machine then sow
 */
class BrokenMachineThenSow : ExampleSystemTestExtension() {
    override val name = "BrokenMachineThenSow"
    override val description = "Tests that a machine that is broken and then sown is still broken"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "BrokenMachineThenSow/farms.json"
    override val scenario = "BrokenMachineThenSow/scenario.json"
    override val map = "BrokenMachineThenSow/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 7

    val hi = """
    [INFO] Initialization Info: map.json successfully parsed and validated.
    [INFO] Initialization Info: farms.json successfully parsed and validated.
    [INFO] Initialization Info: scenario.json successfully parsed and validated.
    [INFO] Simulation Info: Simulation started at tick 7 within the year.
    [INFO] Simulation Info: Tick 0 started at tick 7 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 0 starts its actions.
    [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
    [IMPORTANT] Farm: Farm 0 finished its actions.
    [IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 0.
    [INFO] Simulation Info: Tick 1 started at tick 8 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 0 starts its actions.
    [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
    [IMPORTANT] Farm: Farm 0 finished its actions.
    [INFO] Simulation Info: Tick 2 started at tick 9 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 0 starts its actions.
    [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
    [IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.
    [IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 0.
    [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
    [IMPORTANT] Farm: Farm 0 finished its actions.
    [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 900000 g of POTATO.
    [INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1080000 g of CHERRY.
    [IMPORTANT] Simulation Info: Simulation ended at tick 3.
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
    [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1980000 g.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
