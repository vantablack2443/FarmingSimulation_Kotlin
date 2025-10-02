package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * Just a bigger scenario to test performance
 */
class EvenBiggerScenario : ExampleSystemTestExtension() {
    override val name = "EvenBiggerScenario"
    override val description = "Big, Complext scenario for full year"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "EvenBiggerScenario/farm.json"
    override val scenario = "EvenBiggerScenario/scenario.json"
    override val map = "EvenBiggerScenario/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 48
    override val startYearTick = 1

    override suspend fun run() {
        assertLines(result1a())
        assertLines(result1b())
        assertLines(result2a())
        assertLines(result2b())
        assertLines(result3a())
        assertLines(result3b())
        assertLines(result4())
        assertLines(result5())
        assertLines(result55())
        assertLines(result6())
    }

    private suspend fun assertLines(expected: String) {
        val lineIterator = expected.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun result1a(): String = """
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
   [INFO] Simulation Info: Tick 4 started at tick 5 within the year.
   [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
   [IMPORTANT] Farm: Farm 1 starts its actions.
   [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2.
   [IMPORTANT] Farm: Farm 1 finished its actions.
   [INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 329305 g of APPLE.
    """.trimIndent()
    private fun result1b(): String = """
   [INFO] Simulation Info: Tick 5 started at tick 6 within the year.
   [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
   [IMPORTANT] Farm: Farm 1 starts its actions.
   [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2.
   [IMPORTANT] Farm: Farm 1 finished its actions.
   [INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 240062 g of APPLE.
   [INFO] Simulation Info: Tick 6 started at tick 7 within the year.
   [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
   [IMPORTANT] Farm: Farm 1 starts its actions.
   [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.
   [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 9 for 5 days.
   [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 28 for 5 days.
   [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
   [IMPORTANT] Farm: Farm 1 finished its actions.
   [DEBUG] Harvest Estimate: Required actions on tile 26 were not performed: MOWING.
   [INFO] Harvest Estimate: Harvest estimate on tile 26 changed to 1080000 g of GRAPE.
   [INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 175004 g of APPLE.
   [INFO] Simulation Info: Tick 7 started at tick 8 within the year.
   [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
   [IMPORTANT] Farm: Farm 1 starts its actions.
   [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.
   [IMPORTANT] Farm: Farm 1 finished its actions.
    """.trimIndent()
    private fun result2a(): String = """
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 127576 g of APPLE.
[INFO] Simulation Info: Tick 8 started at tick 9 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 7 for 5 days.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 45 for 5 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 540000 g of CHERRY.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 360000 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 26 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 26 changed to 1079950 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 360000 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 45 changed to 540000 g of CHERRY.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 83701 g of APPLE.
[INFO] Simulation Info: Tick 9 started at tick 10 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 486000 g of CHERRY.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 324000 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 26 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 26 changed to 0 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 324000 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 45 changed to 486000 g of CHERRY.
[DEBUG] Harvest Estimate: Required actions on tile 47 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 0 g of APPLE.
    """.trimIndent()
    private fun result2b(): String = """
[INFO] Simulation Info: Tick 10 started at tick 11 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Incident: Incident 2 of type DROUGHT happened and affected tiles 23,42.
[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: MOWING.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 393660 g of CHERRY.
[DEBUG] Harvest Estimate: Required actions on tile 9 were not performed: MOWING.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 262440 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 28 were not performed: MOWING.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 262440 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 45 were not performed: MOWING.
[INFO] Harvest Estimate: Harvest estimate on tile 45 changed to 393660 g of CHERRY.
[INFO] Simulation Info: Tick 11 started at tick 12 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,3.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 25 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 3.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 44 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 3.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 354294 g of CHERRY.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 236196 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 25 changed to 0 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 236196 g of ALMOND.
[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 450000 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 45 changed to 354294 g of CHERRY.
[INFO] Simulation Info: Tick 12 started at tick 13 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
   [IMPORTANT] Farm: Farm 1 starts its actions.
   [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.
   [IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 7 for 5 days.
   [IMPORTANT] Farm Harvest: Machine 1 has collected 354294 g of CHERRY harvest.
   [IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 45 for 5 days.
   [IMPORTANT] Farm Harvest: Machine 1 has collected 354294 g of CHERRY harvest.
   [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
   [IMPORTANT] Farm Machine: Machine 1 unloads 708588 g of CHERRY harvest in the shed.
   [IMPORTANT] Farm: Farm 1 finished its actions.
    """.trimIndent()
    private fun result3a(): String = """
    [INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 212576 g of ALMOND.
    [INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 212576 g of ALMOND.
    [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 405000 g of PUMPKIN.
    [INFO] Simulation Info: Tick 13 started at tick 14 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 1 starts its actions.
    [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
    [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 9 for 5 days.
    [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 28 for 5 days.
    [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
    [IMPORTANT] Farm: Farm 1 finished its actions.
    [INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 191318 g of ALMOND.
    [INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 191318 g of ALMOND.
    [DEBUG] Harvest Estimate: Required actions on tile 44 were not performed: WEEDING.
    [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 328050 g of PUMPKIN.
    [INFO] Simulation Info: Tick 14 started at tick 15 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 1 starts its actions.
    [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
    [IMPORTANT] Farm: Farm 1 finished its actions.
    [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 295245 g of PUMPKIN.
    [INFO] Simulation Info: Tick 15 started at tick 16 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 1 starts its actions.
    [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
    [IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 9 for 5 days.
    [IMPORTANT] Farm Harvest: Machine 1 has collected 191318 g of ALMOND harvest.
    [IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 28 for 5 days.
    [IMPORTANT] Farm Harvest: Machine 1 has collected 191318 g of ALMOND harvest.
    [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
    [IMPORTANT] Farm Machine: Machine 1 unloads 382636 g of ALMOND harvest in the shed.
    [IMPORTANT] Farm: Farm 1 finished its actions.
    [DEBUG] Harvest Estimate: Required actions on tile 44 were not performed: WEEDING.
    [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 239148 g of PUMPKIN.
    [INFO] Simulation Info: Tick 16 started at tick 17 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 1 starts its actions.
    [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
    [IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 44 for 5 days.
    [IMPORTANT] Farm Harvest: Machine 1 has collected 239148 g of PUMPKIN harvest.
    [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
    [IMPORTANT] Farm Machine: Machine 1 unloads 239148 g of PUMPKIN harvest in the shed.
    [IMPORTANT] Farm: Farm 1 finished its actions.
    [INFO] Simulation Info: Tick 17 started at tick 18 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 1 starts its actions.
    [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
    [IMPORTANT] Farm: Farm 1 finished its actions.
    """.trimIndent()
    private fun result3b(): String = """
    [INFO] Simulation Info: Tick 18 started at tick 19 within the year.
    [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
    [IMPORTANT] Farm: Farm 1 starts its actions.
    [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
    [IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 4 for 4 days.
    [IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 4.
    [IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 6 for 4 days.
    [IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 4.
    [IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 8 for 4 days.
    [IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 4.
    [IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 34.
    [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 23 for 5 days.
    [IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 4.
    [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 25 for 5 days.
    [IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 4.
    [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
    [IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 46 for 14 days.
    [IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 4.
    [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 34.
    [IMPORTANT] Farm: Farm 1 finished its actions.
    [INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 0 g of WHEAT.
    [INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of WHEAT.
    [INFO] Harvest Estimate: Harvest estimate on tile 23 changed to 0 g of WHEAT.
    [INFO] Harvest Estimate: Harvest estimate on tile 25 changed to 0 g of WHEAT.
    [INFO] Harvest Estimate: Harvest estimate on tile 46 changed to 0 g of WHEAT.
    """.trimIndent()
    private fun result4(): String = """
[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 28 for 5 days.
[IMPORTANT] Farm Harvest: Machine 1 has collected 191318 g of ALMOND harvest.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm Machine: Machine 1 unloads 382636 g of ALMOND harvest in the shed.
[IMPORTANT] Farm: Farm 1 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 44 were not performed: WEEDING.
[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 239148 g of PUMPKIN.
[INFO] Simulation Info: Tick 16 started at tick 17 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 2 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 44 for 5 days.
[IMPORTANT] Farm Harvest: Machine 1 has collected 239148 g of PUMPKIN harvest.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm Machine: Machine 1 unloads 239148 g of PUMPKIN harvest in the shed.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 17 started at tick 18 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 18 started at tick 19 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,4.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 4 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 4.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 6 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 4.
[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 8 for 4 days.
[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 4.
[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 34.
[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 23 for 5 days.
[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 4.
[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 25 for 5 days.
[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 4.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 46 for 14 days.
[IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 4.
[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 4 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 0 g of WHEAT.
[DEBUG] Harvest Estimate: Required actions on tile 6 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of WHEAT.
[DEBUG] Harvest Estimate: Required actions on tile 23 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 23 changed to 0 g of WHEAT.
[DEBUG] Harvest Estimate: Required actions on tile 25 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 25 changed to 0 g of WHEAT.
[DEBUG] Harvest Estimate: Required actions on tile 46 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 46 changed to 0 g of WHEAT.
    """.trimIndent()
    private fun result5(): String = """
[DEBUG] Harvest Estimate: Required actions on tile 25 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 25 changed to 0 g of WHEAT.
[DEBUG] Harvest Estimate: Required actions on tile 46 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 46 changed to 0 g of WHEAT.
[INFO] Simulation Info: Tick 19 started at tick 20 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 20 started at tick 21 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 6 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 7 for 5 days.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 9 for 5 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Incident: Incident 3 of type BROKEN_MACHINE happened and affected tiles 34.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 1200000 g of CHERRY.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 800000 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 26 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 26 changed to 0 g of GRAPE.
[DEBUG] Harvest Estimate: Required actions on tile 28 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 799950 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 45 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 45 changed to 0 g of CHERRY.
[DEBUG] Harvest Estimate: Required actions on tile 47 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 47 changed to 0 g of APPLE.
[INFO] Simulation Info: Tick 21 started at tick 22 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 28 for 5 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 8 were not performed: WEEDING.
[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 1350000 g of WHEAT.
[INFO] Simulation Info: Tick 22 started at tick 23 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 23 started at tick 24 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 24 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 25 started at tick 2 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.

    """.trimIndent()
    private fun result55(): String = """
     [IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 26 started at tick 3 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 27 started at tick 4 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 9 for 5 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 600000 g of CHERRY.
[DEBUG] Harvest Estimate: Required actions on tile 8 were not performed: WEEDING.
[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 1215000 g of WHEAT.
[DEBUG] Harvest Estimate: Required actions on tile 9 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 400000 g of ALMOND.
[DEBUG] Harvest Estimate: Required actions on tile 28 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 28 changed to 399975 g of ALMOND.
[INFO] Simulation Info: Tick 28 started at tick 5 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 28 for 5 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 1093500 g of WHEAT.
[INFO] Simulation Info: Tick 29 started at tick 6 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 7 for 5 days.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 34.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 984150 g of WHEAT.
[INFO] Simulation Info: Tick 30 started at tick 7 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 797161 g of WHEAT.
[INFO] Simulation Info: Tick 31 started at tick 8 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,5.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 645699 g of WHEAT.
[IMPORTANT] Simulation Info: Simulation ended at tick 32.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 1 collected 1330372 g of harvest.
[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.


    """.trimIndent()
    private fun result6(): String = """
[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 239148 g.
[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 382636 g.
[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 708588 g.
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2045674 g.
    """.trimIndent()
}
