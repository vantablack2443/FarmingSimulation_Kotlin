package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests priority of irrigation for fields and plantations
 */
class IrrigationTestPriority : ExampleSystemTestExtension() {
    override val name = "IrrigationTestPriority"
    override val description = "Tests irrigation for fields and plantations priority"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "irrigationPriority/farms.json"
    override val scenario = "irrigationPriority/scenario.json"
    override val map = "irrigationPriority/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 8

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
    }
}
