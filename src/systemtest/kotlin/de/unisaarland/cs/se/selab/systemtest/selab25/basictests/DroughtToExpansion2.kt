package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Drought hits and then city expansion on the same tick
 */
class DroughtToExpansion2 : ExampleSystemTestExtension() {
    override val name = "DroughtToExpansion2"
    override val description = "Tests Drought and then City Expansion happening after in same tick."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "JustIncident/droughtCityFarm.json"
    override val scenario = "JustIncident/droughtCityScenario.json"
    override val map = "JustIncident/droughtCityMap.json"

    override val logLevel = "INFO"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: droughtCityMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: droughtCityFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: droughtCityScenario.json successfully parsed and validated.")
    }
}
