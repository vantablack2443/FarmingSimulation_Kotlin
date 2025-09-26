package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class MachineDefaultActionSecondFarm : ExampleSystemTestExtension() {
    override val name = "Machine test including second farm"
    override val description = "Tests default behavior of the machines and some incidents"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineActionsTest/farmsMachineDefault.json"
    override val scenario = "machineActionsTest/scenarioMachineDefault.json"
    override val map = "machineActionsTest/mapMachineDefault.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 25 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 10.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 14 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 27 for 8 days.")
        // collect harvest
        skipLines(1)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 7.")
        skipLines(1) // unloads at shed
        assertNextLine("[IMPORTANT] Farm: Farm 2 finished its actions.")
    }
}
