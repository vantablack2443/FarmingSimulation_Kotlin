package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Checks if after harvesting plantation tiles lose moisture at 70L/tick
 */
class PlantationHarvestMoistureSeventy : ExampleSystemTestExtension() {
    override val name = "PlantationHarvestMoistureSeventy"
    override val description = "Checks if after harvesting plantation tiles lose moisture at 70L/tick"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "plantationHarvestMoisture/map.json"
    override val farms = "plantationHarvestMoisture/farms.json"
    override val scenario = "plantationHarvestMoisture/scenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 19

    /*
        ID 1: Capacity 250 -> 150, Moisture penalty 0
     */
    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: " +
                "scenario.json successfully parsed and validated."
        )
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 19 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 19 within the year.")

        val expectedLine = "[INFO] Simulation Info: Tick 1 started at tick 20 within the year."
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == expectedLine)

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        val expectedLine2 = "[INFO] Simulation Info: Tick 1 started at tick 21 within the year."
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == expectedLine2)

        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
    }
}
