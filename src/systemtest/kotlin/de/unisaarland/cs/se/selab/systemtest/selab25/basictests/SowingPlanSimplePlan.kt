package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test simple action of sowing on a small map
 */
class SowingPlanSimplePlan : ExampleSystemTestExtension() {
    override val name = "Sowing Plan Fields"
    override val description = "Tests sowing plan parsing"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "sowing_basic/farmsSimplePlan.json"
    override val scenario = "sowing_basic/scenarioSimplePlan.json"
    override val map = "sowing_basic/mapSimplePlan.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
//        skipUntilString(
//            "[DEBUG] Farm: Farm 0 has the following active sowing plans" +
//                " it intends to pursue in this tick: 0."
//        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
}
