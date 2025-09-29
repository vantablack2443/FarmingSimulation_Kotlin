package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests action handling phase for one tick with various actions
 */
class FarmActions : ExampleSystemTestExtension() {

    /*
    TEST ALSO MULTIPLE MACHINES WITH SAME ACTION!!!!!
     */
    override val name = "FarmActions"
    override val description = "Tests action handling phase."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "actions/farms.json"
    override val scenario = "actions/scenario.json"
    override val map = "actions/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        return
    }
}
