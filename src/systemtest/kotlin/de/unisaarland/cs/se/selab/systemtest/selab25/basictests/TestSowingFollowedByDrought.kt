package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * Test that sowing followed by drought works and the harvestestimate is working
 */
class TestSowingFollowedByDrought : ExampleSystemTestExtension() {
    override val name = "testSowingFollowedByDrought"
    override val description = "Test that sowing followed by drought works and the harvestestimate is working "
    override val farms = "sowingFollowedByDroughtIncident/farm.json"
    override val scenario = "sowingFollowedByDroughtIncident/scenario.json"
    override val map = "sowingFollowedByDroughtIncident/map.json"
    override val logLevel = "DEBUG"
    override val maxTicks = 11
    override val startYearTick = 15
    override suspend fun run() {
        val expectedLines = buildList {
            addAll(initializationInfo().lines())
            addAll(simulationTicks().lines())
            addAll(harvestEstimates().lines())
            addAll(remainingTicks().lines())
            addAll(simulationStatistics().lines())
        }
        val lineIterator = expectedLines.iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun initializationInfo(): String = """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farm.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 15 within the year.
[INFO] Simulation Info: Tick 0 started at tick 15 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 104.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 4,5,6,7.
""".trim()

    private fun simulationTicks(): String = """
[INFO] Simulation Info: Tick 1 started at tick 16 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 104.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 2 started at tick 17 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 76.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 3 started at tick 18 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 76.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 4 started at tick 19 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 62.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1.
[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 4 for 4 days.
[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.
[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 5 for 4 days.
[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.
[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 6 for 4 days.
[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.
[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 8.
[IMPORTANT] Farm: Farm 1 finished its actions.
""".trim()

    private fun harvestEstimates(): String = """
[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 0 g of WHEAT.
[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 0 g of WHEAT.
[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of WHEAT.
[INFO] Simulation Info: Tick 5 started at tick 20 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 62.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
""".trim()

    private fun remainingTicks(): String = """
[INFO] Simulation Info: Tick 6 started at tick 21 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 48.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 7 started at tick 22 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 48.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 8 started at tick 23 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 34.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 9 started at tick 24 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 34.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[INFO] Simulation Info: Tick 10 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 4, where the amount of sunlight is 48.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
""".trim()

    private fun simulationStatistics(): String = """
[IMPORTANT] Simulation Info: Simulation ended at tick 11.
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
""".trim()
}
