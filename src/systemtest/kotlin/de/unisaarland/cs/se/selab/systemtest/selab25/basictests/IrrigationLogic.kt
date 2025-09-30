package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

// import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests default behavior of machines
 */
class IrrigationLogic : ExampleSystemTestExtension() {
    override val name = "Irrigation Logic"
    override val description = "Tests irrigation action of the machine in tile order 1,2,4,3 "

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "justAction/farmForIrrigation.json"
    override val scenario = "example/scenario.json"
    override val map = "justAction/mapIrrigationLogic.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 3
    override val startYearTick = 21

    override suspend fun run() {
//        assertNextLine("[INFO] Initialization Info: mapOnlyPlantationFarm.json successfully parsed and validated.")
//        assertNextLine("[INFO] Initialization Info: farmForIrrigation.json successfully parsed and validated.")
//        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
//        skipUntilLogType(LogLevel.DEBUG, LogType.SIMULATION_INFO)

        skipLines(1)
//        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 1 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 0.")
        skipLines(4)
//        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 2 days.")
//        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 2 days.")
//        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
//        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
// NEXT TICK
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 4 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 2 days.") // Order 1,2,3
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 2 days.")
//        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 4 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        // NEXT TICK
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 2 days.") // Order 1,2,4,3
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 4 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

//        skipUntilString(
//            "[IMPORTANT] Simulation Statistics: Total harvest estimate " +
//                "still in fields and plantations: 4638250 g."
//        )
//        assertEnd()
//    }
//
//    private suspend fun skipUntilString(startString: String): String {
//        val line: String = getNextLine()
//            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
//        return if (line.startsWith(startString)) {
//            line
//        } else {
//            skipUntilString(startString)
//        }
    }
}
