package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
const val SOWING_EXECUTED_PLAN_ZERO = "[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 0."
const val SOWING_EXECUTED_PLAN_ONE = "[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 1."
const val TRY_SOWING_PLAN = "[DEBUG] Farm: Farm 0 has the following active sowing plans"

/**
 * test simple action of sowing on a small map
 */
class SowingPlanSimplePlan : ExampleSystemTestExtension() {
    override val name = "Sowing Plan Fields With One Simple Plan"
    override val description = "Tests sowing plan parsing"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "sowing_basic/farmsSimplePlan.json"
    override val scenario = "sowing_basic/scenarioSimplePlan.json"
    override val map = "sowing_basic/mapSimplePlan.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 26
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 0."
        )

        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 4 days.")
        assertNextLine(SOWING_EXECUTED_PLAN_ZERO)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.")
        assertNextLine(SOWING_EXECUTED_PLAN_ZERO)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 4 days.")
        assertNextLine(SOWING_EXECUTED_PLAN_ZERO)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 1."
        )

        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 4 days.")
        assertNextLine(SOWING_EXECUTED_PLAN_ONE)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.")
        assertNextLine(SOWING_EXECUTED_PLAN_ONE)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }

    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
