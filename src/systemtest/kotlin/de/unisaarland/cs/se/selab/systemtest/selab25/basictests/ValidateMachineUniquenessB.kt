package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests if machine actions should match plant types
 */
class ValidateMachineUniquenessB : ExampleSystemTestExtension() {
    override val name = "ValidateMachineUniqueName"
    override val description = "Tests if the uniqueness of machine name is invalid"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "validating_basic/machineUniqueBFarm.json"
    override val scenario = "validating_basic/emptyScenario.json"
    override val map = "validating_basic/plantCouplingMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: plantCouplingMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: machineUniqueBFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: emptyScenario.json successfully parsed and validated.")
    }
}
