package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * Clouds strange behavior test
 */
class CloudsStrange : ExampleSystemTestExtension() {

    override val name = "CloudsStrange"
    override val description = "Clouds strange behavior test"
    override val farms = "CloudsStrange/farm.json"
    override val scenario = "CloudsStrange/scenario.json"
    override val map = "CloudsStrange/map.json"
    override val logLevel = "DEBUG"
    override val maxTicks = 14
    override val startYearTick = 1

    override suspend fun run() {
        assertLines(result1a())
        assertLines(result1b())
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
            [INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 11 to tile 10.
            [INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 10 to tile 9.
            [IMPORTANT] Cloud Union: Clouds 3 and 2 united to cloud 6 with 8000 L water and duration -1 on tile 9.
            [INFO] Cloud Movement: Cloud 4 with 4000 L water moved from tile 1 to tile 2.
            [INFO] Cloud Movement: Cloud 4 with 4000 L water moved from tile 2 to tile 3.
            [INFO] Cloud Movement: Cloud 4 with 4000 L water moved from tile 3 to tile 4.
            [IMPORTANT] Cloud Union: Clouds 5 and 4 united to cloud 7 with 8000 L water and duration -1 on tile 4.
            [IMPORTANT] Cloud Rain: Cloud 6 on tile 9 rained down 8000 L water.
            [INFO] Cloud Dissipation: Cloud 6 dissipates on tile 9.
            [IMPORTANT] Cloud Rain: Cloud 7 on tile 4 rained down 8000 L water.
            [INFO] Cloud Dissipation: Cloud 7 dissipates on tile 4.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 1 started at tick 2 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 2 started at tick 3 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 3 started at tick 4 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 4 started at tick 5 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 5 started at tick 6 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 6 started at tick 7 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 7 started at tick 8 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
    """.trimIndent()
    private fun result1b(): String = """
            [INFO] Simulation Info: Tick 8 started at tick 9 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 9 started at tick 10 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 10 started at tick 11 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 11 started at tick 12 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 12 started at tick 13 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [INFO] Simulation Info: Tick 13 started at tick 14 within the year.
            [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
            [IMPORTANT] Farm: Farm 1 starts its actions.
            [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
            [IMPORTANT] Farm: Farm 1 finished its actions.
            [IMPORTANT] Simulation Info: Simulation ended at tick 14.
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
            [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g.
    """.trimIndent()
}
