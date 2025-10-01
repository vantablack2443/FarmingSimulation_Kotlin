package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests changing houses
 */
class AnotherWayHome : ExampleSystemTestExtension() {
    override val name = "anotherWayHome"
    override val description = "checks the case where a machine can go back to a different shed"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "machine_basic/anotherWayHomeMap.json"
    override val farms = "machine_basic/anotherWayHomeFarms.json"
    override val scenario = "machine_basic/anotherWayHomeScenario.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 1083000 g of GRAPE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 11.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 unloads 1083000 g of GRAPE harvest in the shed.")
        skipUntilString("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 1083000 g of harvest.")
    }

    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError(
                "End of log reached when there should be more,\n" +
                    "expected: " + startString
            )
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
