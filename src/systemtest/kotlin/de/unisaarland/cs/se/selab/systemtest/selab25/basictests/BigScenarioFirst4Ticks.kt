package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * Big Scenario for the first 4 year ticks
 *
 * This test uses the big scenario files and only runs the first 4 ticks of the first year.
 * It checks if the output is as expected.
 */
class BigScenarioFirst4Ticks : ExampleSystemTestExtension() {
    override val name = "BigScenario"
    override val description = "Big Scenario for the first 4 year ticks"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "BigOneYearScenario/farm.json"
    override val scenario = "BigOneYearScenario/scenario.json"
    override val map = "BigOneYearScenario/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1
    override suspend fun run() {
        val lineIterator = result().lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun result(): String {
        return """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farm.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 1 within the year.
[INFO] Simulation Info: Tick 0 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 5,23,24,25,43.
[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 0 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 24 changed to 0 g of CHERRY.
[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 0 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 1530000 g of APPLE.
[INFO] Simulation Info: Tick 1 started at tick 2 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 1377000 g of APPLE.
[INFO] Simulation Info: Tick 2 started at tick 3 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 1115370 g of APPLE.
[INFO] Simulation Info: Tick 3 started at tick 4 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2.
[IMPORTANT] Farm: Farm 1 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 600000 g of CHERRY.
[DEBUG] Harvest Estimate: Required actions on tile 9 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 400000 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 28 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 400000 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 45 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 45 changed to 600000 g of CHERRY.
[DEBUG] Harvest Estimate: Required actions on tile 47 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 451724 g of APPLE.
[IMPORTANT] Simulation Info: Simulation ended at tick 4.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.
[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 3651724 g.
        """.trimIndent()
    }
}
