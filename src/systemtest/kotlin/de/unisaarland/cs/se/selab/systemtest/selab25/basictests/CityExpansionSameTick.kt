package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * City expansion over same tick
 */
class CityExpansionSameTick : ExampleSystemTestExtension() {
    override val name = "CityExpansionSameTick"
    override val description = "Tests that city expansions in the same tick work correctly."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "CityExpansionSameTick/farms.json"
    override val scenario = "CityExpansionSameTick/scenario.json"
    override val map = "CityExpansionSameTick/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario.json is invalid.")
    }
}
