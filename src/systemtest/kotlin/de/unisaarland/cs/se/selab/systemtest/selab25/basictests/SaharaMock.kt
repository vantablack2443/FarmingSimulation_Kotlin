package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

class SaharaMock : ExampleSystemTestExtension() {
    override val name = "SaharaMock"
    override val description = "Mock test with desert map and no farms"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "SaharaMock/farms.json"
    override val scenario = "SaharaMock/scenario.json"
    override val map = "SaharaMock/map.json"

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
 [IMPORTANT] Simulation Info: Simulation ended at tick 1.
 [IMPORTANT] Simulation Info: Simulation statistics are calculated.
 [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested:
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
