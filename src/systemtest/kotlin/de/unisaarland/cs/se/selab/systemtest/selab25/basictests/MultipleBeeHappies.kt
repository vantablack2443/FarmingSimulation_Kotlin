package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * multiple bee happies
 */
class MultipleBeeHappies : ExampleSystemTestExtension() {
    override val name = "MultipleBeeHappies"
    override val description = "Multiple bee happies"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "MultipleBeeHappies/farms.json"
    override val scenario = "MultipleBeeHappies/scenario.json"
    override val map = "MultipleBeeHappies/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
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
 [IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles 1.
 [IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 1.
 [IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles 1.
 [INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1315154 g of APPLE.
 [IMPORTANT] Simulation Info: Simulation ended at tick 1.
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
 [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1315154 g.
    """.trimIndent()

//    val hi = """
// [INFO] Initialization Info: map.json successfully parsed and validated.
// [INFO] Initialization Info: farms.json successfully parsed and validated.
// [INFO] Initialization Info: scenario.json successfully parsed and validated.
// [INFO] Simulation Info: Simulation started at tick 8 within the year.
// [INFO] Simulation Info: Tick 0 started at tick 8 within the year.
// [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
// [IMPORTANT] Farm: Farm 0 starts its actions.
// [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
// [IMPORTANT] Farm: Farm 0 finished its actions.
// [IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles 1.
// [IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 1.
// [IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles 1.
//    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
