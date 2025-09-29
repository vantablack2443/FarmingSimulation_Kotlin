package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val SOWING = "[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 1."

/**
 * tests order of irrigation, mowing and weeding
 */
class MachineIrrigationTest : ExampleSystemTestExtension() {
    override val name = "Machine Irrigation Test"
    override val description = "Tests machine behavior for irrigation, weeding and mowing"

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

        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 4 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 6 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 7 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")

        assertNextLine("[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 4.")

        // next tick, early april, need to weed oat and mow grapes
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 5 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")

        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs MOWING on tile 6 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs MOWING on tile 7 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
    }
}
