package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

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
    override val maxTicks = 24
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString("Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0.")
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
