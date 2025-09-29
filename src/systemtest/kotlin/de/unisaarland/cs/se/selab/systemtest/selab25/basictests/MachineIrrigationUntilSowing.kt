package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests order of irrigation, mowing and weeding
 */
class MachineIrrigationUntilSowing : ExampleSystemTestExtension() {
    override val name = "Machine Irrigation Until Sowing (ascending tile ID)"
    override val description = "sowing by ascending tile ID"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineActionsTest/farmsIrrigation.json"
    override val scenario = "machineActionsTest/scenarioIrrigation.json"
    override val map = "machineActionsTest/mapIrrigation.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        // start late march, can sow oat
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 2 for 4 days.")
        assertNextLine(SOWING)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 5 for 4 days.")
        assertNextLine(SOWING)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 8 for 4 days.")
        assertNextLine(SOWING)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 1.")
    }
}
