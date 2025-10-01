package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test scenario where machine can't go home
 */
class NoWayHome : ExampleSystemTestExtension() {
    override val name = "NoWayHome"
    override val description = "checks the case where a machine can't go back to its shed"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val map = "machine_basic/noWayHomeMap.json"
    override val farms = "machine_basic/noWayHomeFarms.json"
    override val scenario = "machine_basic/noWayHomeScenario.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 1083000 g of GRAPE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished but failed to return.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 1.")
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
