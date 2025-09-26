package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class MachineDefaultActionParsing : ExampleSystemTestExtension() {
    override val name = "MachineDefaultActionParsing"
    override val description = "Tests machine test config parse"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineActionsTest/farmsMachineDefault.json"
    override val scenario = "machineActionsTest/scenarioMachineDefault.json"
    override val map = "machineActionsTest/mapMachineDefault.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
    }
}
