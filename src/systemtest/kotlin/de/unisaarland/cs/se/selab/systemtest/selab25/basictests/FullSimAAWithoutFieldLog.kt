package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class FullSimAAWithoutFieldLog : ExampleSystemTestExtension() {
    override val name = "FullSim test with animal attack NOT logging field tiles"
    override val description = "Tests if animal attack should log affected field tiles without plant"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullSim/farms.json"
    override val scenario = "fullSim/scenarioAA.json"
    override val map = "fullSim/map.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 4

    override suspend fun run() {
        tickZero()
    }

    private suspend fun tickZero() {
        skipLines(4)
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 2 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 20.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 2.")
        assertNextLine("[IMPORTANT] Incident: Incident 3 of type BEE_HAPPY happened and affected tiles 10.")
        assertNextLine("[IMPORTANT] Incident: Incident 4 of type DROUGHT happened and affected tiles 5.")
    }
}
