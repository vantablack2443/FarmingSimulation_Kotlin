package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * validation for machines to be on the shed belonging to the farm
 */
class MachineWrongShed : ExampleSystemTestExtension() {
    override val name = "machine wrong shed"
    override val description = "validating machine in its farm's shed"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "farmParserTests/farmsMachineWrongShed.json"
    override val scenario = "farmParserTests/scenario.json"
    override val map = "farmParserTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farmsMachineWrongShed.json is invalid.")
    }
}
