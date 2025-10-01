package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val FARM_ONE_END = "[IMPORTANT] Farm: Farm 1 finished its actions."
const val MACH_ONE_END = "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 10."

/**
 * tests the behaviour for potatoes in 3 different cases
 * case 1 : perfect (all actions performed)
 * case 2 : imperfect (not all actions performed, late sown)
 * case 3 : schlecht (only sown and harvested)
 */
class OatBehaviourTest : ExampleSystemTestExtension() {
    override val name = "OatBehaviourTest"
    override val description = "Tests the behaviour of oats"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plant_behaviour/oatFarm.json"
    override val scenario = "plant_behaviour/oatScenario.json"
    override val map = "plant_behaviour/oatMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 14
    override val startYearTick = 6

    override suspend fun run() {
        // simTick: 0, yearTick 6, SOW(1)
        skipUntilString("[INFO] Simulation Info: Tick 0 started at tick 6 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + "it intends to pursue in this tick: 1.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 1.")
        assertNextLine(MACH_ONE_END)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1080000 g of OAT.")

        // simTick 1, yearTick 7, WEED(1)
        skipUntilString("[INFO] Simulation Info: Tick 1 started at tick 7 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)
        assertNextLine(MACH_ONE_END)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 874800 g of OAT.")

        // simTick 2, yearTick 8, WEED(1)
        skipUntilString("[INFO] Simulation Info: Tick 2 started at tick 8 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)
        assertNextLine(MACH_ONE_END)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 708588 g of OAT.")

        // simTick 3, yearTick 9, WEED(1)
        skipUntilString("[INFO] Simulation Info: Tick 3 started at tick 9 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)
        assertNextLine(MACH_ONE_END)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 516560 g of OAT.")

        // simTick 4, yearTick 10
        skipUntilString("[INFO] Simulation Info: Tick 4 started at tick 10 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 376571 g of OAT.")

        // simTick 5, yearTick 11
        skipUntilString("[INFO] Simulation Info: Tick 5 started at tick 11 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 274518 g of OAT.")

        // simTick 6, yearTick 12
        skipUntilString("[INFO] Simulation Info: Tick 6 started at tick 12 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 200123 g of OAT.")

        // simTick 7, yearTick 13
        skipUntilString("[INFO] Simulation Info: Tick 7 started at tick 13 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 200123 g of OAT harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 10.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 unloads 200123 g of OAT harvest in the shed.")
        assertNextLine(FARM_ONE_END)
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
