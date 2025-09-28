package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class JustMow : ExampleSystemTestExtension() {
    override val name = "Just Mow"
    override val description = "Tests mowing action of the machine in first tick"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "justAction/farmOnlyPlantation.json"
    override val scenario = "example/scenario.json"
    override val map = "justAction/mapOnlyPlantationFarm.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 11

    override suspend fun run() {
//        assertNextLine("[INFO] Initialization Info: mapOnlyPlantationFarm.json successfully parsed and validated.")
//        assertNextLine("[INFO] Initialization Info: farmOnlyPlantation.json successfully parsed and validated.")
//        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
    }
}