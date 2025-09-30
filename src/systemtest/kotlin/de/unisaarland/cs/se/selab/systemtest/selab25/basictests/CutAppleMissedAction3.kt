package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests if first missed cutting is added in the needed List
 */
class CutAppleMissedAction3 : ExampleSystemTestExtension() {
    override val name = "CutAppleMissedAction3"
    override val description = "Tests if first missed cutting is added in the needed List"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "justAction/cutAppleMissedFarm.json"
    override val scenario = "example/scenario.json"
    override val map = "justAction/cutAppleMissedMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 21

    override suspend fun run() {
//        assertNextLine("")
        assertNextLine("[INFO] Initialization Info: cutAppleMissedMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: cutAppleMissedFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 21 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 21 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing" +
                " plans it intends to pursue in this tick: ."
        )
//        val line = skipUntilString("Farm: Farm 0 finished its actions")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        skipLines(4)
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 22 within the year.")
    }

//    private suspend fun skipUntilString(startString: String): String {
//        val line: String = getNextLine()
//            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
//        return if (line.startsWith(startString)) {
//            line
//        } else {
//            skipUntilString(startString)
//        }
//    }
}
