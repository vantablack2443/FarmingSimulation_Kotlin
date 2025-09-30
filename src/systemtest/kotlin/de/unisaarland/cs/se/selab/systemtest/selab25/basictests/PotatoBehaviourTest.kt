package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val FARM_ONE_SOW_PLAN = "[DEBUG] Farm: Farm 1 has the following active sowing plans "

/**
 * tests the behaviour for potatoes in 3 different cases
 * case 1 : perfect (all actions performed)
 * case 2 : imperfect (not all actions performed, late sown)
 * case 3 : schlecht (only sown and harvested)
 */
class PotatoBehaviourTest : ExampleSystemTestExtension() {
    override val name = "SowingPlanFieldsWithTwoSimplePlans"
    override val description = "Tests sowing for the next year"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "sowing_basic/farmsSimplePlan.json"
    override val scenario = "sowing_basic/scenarioSimplePlan.json"
    override val map = "sowing_basic/mapSimplePlan.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 16
    override val startYearTick = 5

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 2 started at tick 7 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + "it intends to pursue in this tick: 1.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        skipUntilString("[INFO] Simulation Info: Tick 4 started at tick 9 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + "it intends to pursue in this tick: .")
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
