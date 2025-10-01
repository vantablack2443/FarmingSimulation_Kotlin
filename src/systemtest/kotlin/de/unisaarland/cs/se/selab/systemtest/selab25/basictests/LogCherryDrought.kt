package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Check if plant in Nov need to log 0 to 0 harvest in Nov if hit by drought.
 */
class LogCherryDrought : ExampleSystemTestExtension() {
    override val name = "LogCherryDrought"
    override val description = "Check if plant in Nov need to log 0 to 0 harvest in Nov if hit by drought."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "cherryDrought/cherryFarm.json"
    override val scenario = "cherryDrought/droughtOnPlantation.json"
    override val map = "cherryDrought/cherryMap.json"

    override val logLevel = "INFO"
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: cherryMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: cherryFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: droughtOnPlantation.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 20 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 20 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 21 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
    }
}
