package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val FARM_ONE_SOW_PLAN = "[DEBUG] Farm: Farm 1 has the following active sowing plans "
const val FARM_TWO_SOW_PLAN = "[DEBUG] Farm: Farm 2 has the following active sowing plans "
const val FARM_THREE_SOW_PLAN = "[DEBUG] Farm: Farm 3 has the following active sowing plans "
const val FARM_ONE_WEEDING = "[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 1 for 4 days."
const val EMPTY_SOW_PLAN = "it intends to pursue in this tick: ."

/**
 * tests the behaviour for potatoes in 3 different cases
 * case 1 : perfect (all actions performed)
 * case 2 : imperfect (not all actions performed, late sown)
 * case 3 : schlecht (only sown and harvested)
 */
class PotatoBehaviourTest : ExampleSystemTestExtension() {
    override val name = "PotatoBehaviourTest"
    override val description = "Tests the behaviour of potatoes"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plant_behaviour/potatoFarm.json"
    override val scenario = "plant_behaviour/potatoScenario.json"
    override val map = "plant_behaviour/potatoMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 12
    override val startYearTick = 5

    override suspend fun run() {
        // simTick: 2, yearTick 7, SOW(1)
        skipUntilString("[INFO] Simulation Info: Tick 2 started at tick 7 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + "it intends to pursue in this tick: 1.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 1.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1000000 g of POTATO.")

        // simTick 4, yearTick 9, WEED(1)
        skipUntilString("[INFO] Simulation Info: Tick 4 started at tick 9 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)

        // simTick 6, yearTick 11, WEED(1), SOW_LATE(2)
        skipUntilString("[INFO] Simulation Info: Tick 6 started at tick 11 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)
        skipUntilString(FARM_TWO_SOW_PLAN + "it intends to pursue in this tick: 2.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 2 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 2 has sowed POTATO according to sowing plan 2.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 800000 g of POTATO.")

        // simTick 7, yearTick 12, SOW_LATE(3)
        skipUntilString("[INFO] Simulation Info: Tick 7 started at tick 12 within the year.")
        skipUntilString(FARM_THREE_SOW_PLAN + "it intends to pursue in this tick: 3.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 3 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 3 has sowed POTATO according to sowing plan 3.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 640000 g of POTATO.")

        // simTick 8, yearTick 13, WEED(1), WEED(2)
        skipUntilString("[INFO] Simulation Info: Tick 8 started at tick 13 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)
        skipUntilString(FARM_TWO_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 2 for 4 days.")

        // simTick 9, yearTick 14, WEED_MISS(3)
        skipUntilString("[INFO] Simulation Info: Tick 9 started at tick 14 within the year.")
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 576000 g of POTATO.")

        // simTick 10, yearTick 15, WEED(1), WEED_MISS(2)
        skipUntilString("[INFO] Simulation Info: Tick 10 started at tick 15 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine(FARM_ONE_WEEDING)
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 720000 g of POTATO.")

        // simTick 11, yearTick 16, WEED_MISS(3)
        skipUntilString("[INFO] Simulation Info: Tick 11 started at tick 16 within the year.")
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 518400 g of POTATO.")

        // simTick 12, yearTick 17, HARVEST(1), HARVEST(2), HARVEST(3)
        skipUntilString("[INFO] Simulation Info: Tick 11 started at tick 16 within the year.")
        skipUntilString(FARM_ONE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 1000000 g of POTATO harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 10.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 unloads 1000000 g of POTATO harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        skipUntilString(FARM_TWO_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 2 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 720000 g of POTATO harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 20.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 720000 g of POTATO harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 3 starts its actions.")
        skipUntilString(FARM_THREE_SOW_PLAN + EMPTY_SOW_PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 3 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 3 has collected 518400 g of POTATO harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 30.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 unloads 518400 g of POTATO harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 3 finished its actions.")
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
