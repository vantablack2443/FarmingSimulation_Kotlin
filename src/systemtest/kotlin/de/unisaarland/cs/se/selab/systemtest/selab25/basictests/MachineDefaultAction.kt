package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class MachineDefaultAction : ExampleSystemTestExtension() {
    override val name = "Machine Default Actions"
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
        assertNextLine("[IMPORTANT] Incident: Incident 10 of type BROKEN_MACHINE happened and affected tiles 1.")
        assertNextLine("[IMPORTANT] Incident: Incident 11 of type BROKEN_MACHINE happened and affected tiles 1.")
        assertNextLine("[IMPORTANT] Incident: Incident 12 of type BROKEN_MACHINE happened and affected tiles 7.")
        assertNextLine("[IMPORTANT] Incident: Incident 13 of type CITY_EXPANSION happened and affected tiles 15.")
        assertNextLine("[IMPORTANT] Incident: Incident 14 of type CITY_EXPANSION happened and affected tiles 16.")
        assertNextLine("[IMPORTANT] Incident: Incident 15 of type CITY_EXPANSION happened and affected tiles 17.")
        assertNextLine("[IMPORTANT] Incident: Incident 17 of type CITY_EXPANSION happened and affected tiles 26.")
        assertNextLine("[IMPORTANT] Incident: Incident 18 of type CITY_EXPANSION happened and affected tiles 29.")
        assertNextLine("[IMPORTANT] Incident: Incident 140 of type DROUGHT happened and affected tiles 14.")
        // next tick
        skipLines(2) // farm 1 can't do anything
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 28 for 8 days.")
        skipLines(1) // collected harvest but can't go back
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished but failed to return.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 finished its actions.")
    }
}
