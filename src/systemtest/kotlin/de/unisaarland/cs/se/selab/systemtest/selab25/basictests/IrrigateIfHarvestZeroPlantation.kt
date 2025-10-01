package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * irrigate plantation even if harvest is zero
 */
class IrrigateIfHarvestZeroPlantation : ExampleSystemTestExtension() {
    override val name = "IrrigateIfHarvestZeroPlantation"
    override val description = "Irrigate plantation even if harvest is zero"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "IrrigateIfHarvestZeroPlantation/farms.json"
    override val scenario = "IrrigateIfHarvestZeroPlantation/scenario.json"
    override val map = "IrrigateIfHarvestZeroPlantation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 17

    val hi = """
 [INFO] Initialization Info: map.json successfully parsed and validated.
 [INFO] Initialization Info: farms.json successfully parsed and validated.
 [INFO] Initialization Info: scenario.json successfully parsed and validated.
 [INFO] Simulation Info: Simulation started at tick 8 within the year.
 [INFO] Simulation Info: Tick 0 started at tick 8 within the year.
 [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
 [IMPORTANT] Farm: Farm 0 starts its actions.
 [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
 [IMPORTANT] Farm: Farm 0 finished its actions.
 [INFO] Simulation Info: Tick 1 started at tick 9 within the year.
 [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
 [IMPORTANT] Farm: Farm 0 starts its actions.
 [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
 [IMPORTANT] Farm: Farm 0 finished its actions.
 [INFO] Simulation Info: Simulation ended at tick 2.
 [IMPORTANT] Simulation Info: Simulation statistics are calculated.
 [IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.
 [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
 [IMPORTANT]
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
