package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * drought, rain then sow
 */
class DroughtRainThenSow : ExampleSystemTestExtension() {
    override val name = "DroughtRainThenSow"
    override val description = "Drought, rain then sow"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "DroughtRainThenSow/farms.json"
    override val scenario = "DroughtRainThenSow/scenario.json"
    override val map = "DroughtRainThenSow/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 8

    val hi = """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farms.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 8 within the year.
[INFO] Simulation Info: Tick 0 started at tick 8 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1.
[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles 1.
[INFO] Simulation Info: Tick 1 started at tick 9 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Cloud Rain: Cloud 0 on tile 1 rained down 1000 L water.
[DEBUG] Cloud Position: Cloud 0 is on tile 1, where the amount of sunlight is 118.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 0.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.
[IMPORTANT] Farm: Farm 0 finished its actions.
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
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1000000 g.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
