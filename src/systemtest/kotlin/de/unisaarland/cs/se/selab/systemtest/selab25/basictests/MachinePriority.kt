package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests that machine priority is handled correctly.
 */
class MachinePriority : ExampleSystemTestExtension() {
    override val name = "MachinePriority"
    override val description = "Tests that machine priority is handled correctly."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "justAction/machinePriorityMap.json"
    override val farms = "justAction/machinePriorityFarm.json"
    override val scenario = "example/scenario.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs MOWING on tile 2 for 7 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs MOWING on tile 3 for 7 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
    }
}
