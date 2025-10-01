package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Drought hits and then city expansion on the same tick
 */
class DroughtToExpansionValid : ExampleSystemTestExtension() {
    override val name = "DroughtToExpansionValid"
    override val description = "Tests Drought and then City Expansion happening after in same tick."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "JustIncident/droughtCityFarm.json"
    override val scenario = "JustIncident/droughtCityScenarioValid.json"
    override val map = "JustIncident/droughtCityMap.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipLines(3)
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 0 within the year.")
    }
}
