package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests if machine actions should match plant types
 */
class ValidateMachineActionPlantTrue : ExampleSystemTestExtension() {
    override val name = "ValidateMachineActionPlantTrue"
    override val description = "Tests matching between action and plant types"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineActionsTest/farms.json"
    override val scenario = "machineActionsTest/scenario.json"
    override val map = "machineActionsTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farms.json is invalid.")
    }
}
