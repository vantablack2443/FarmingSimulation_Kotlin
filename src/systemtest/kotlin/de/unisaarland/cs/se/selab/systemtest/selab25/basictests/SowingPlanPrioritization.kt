package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val RETURN_SHED_ZWEI = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2."
const val FERTIG_FARM_ZERO = "[IMPORTANT] Farm: Farm 0 finished its actions."

/**
 * test the prioritization of the action sowing
 */
class SowingPlanPrioritization : ExampleSystemTestExtension() {
    override val name = "SowingPlanPrioritizationTest"
    override val description = "Tests sowing prioritization (tick -> id)"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "sowing_basic/farmsPrioritizationTest.json"
    override val scenario = "sowing_basic/scenarioSimplePlan.json"
    override val map = "sowing_basic/mapPrioritizationTest.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 9

    override suspend fun run() {
        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 1."
        )
        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 0,1,2."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 1.")
        assertNextLine(RETURN_SHED_ZWEI)
        assertNextLine(FERTIG_FARM_ZERO)
        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 0,2."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 5 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 0.")
        assertNextLine(RETURN_SHED_ZWEI)
        assertNextLine(FERTIG_FARM_ZERO)
        skipUntilString(
            TRY_SOWING_PLAN +
                " it intends to pursue in this tick: 2."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 2.")
        assertNextLine(RETURN_SHED_ZWEI)
        assertNextLine(FERTIG_FARM_ZERO)
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
