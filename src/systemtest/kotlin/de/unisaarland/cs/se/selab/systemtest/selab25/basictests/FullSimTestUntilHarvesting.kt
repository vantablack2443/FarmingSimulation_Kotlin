package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class FullSimTestUntilHarvesting : ExampleSystemTestExtension() {
    override val name = "FullSimTestUntilHarvesting"
    override val description = "Tests simulation until year tick 14"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullSim/farms.json"
    override val scenario = "fullSim/scenario.json"
    override val map = "fullSim/map.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 12
    override val startYearTick = 4

    override suspend fun run() {
        tickUntilMay()
        // yeartick 10
        skipLines(5)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 18 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed PUMPKIN according to sowing plan 4.")
        skipLines(4)
        // resets mowing for APPLE
        assertNextLine("[IMPORTANT] Incident: Incident 5 of type ANIMAL_ATTACK happened and affected tiles 2,4,11.")
        // yeartick 11
        skipLines(5)
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs MOWING on tile 10 for 3 days.")
        skipLines(3)
        assertNextLine("[IMPORTANT] Farm Action: Machine 5 performs WEEDING on tile 5 for 3 days.")
        skipLines(2)
        // yeartick 12
        skipLines(5)
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 18 for 3 days.")
        skipLines(4)
        // yeartick 13
        skipLines(7)
        assertNextLine("[IMPORTANT] Farm Action: Machine 5 performs WEEDING on tile 5 for 3 days.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Incident: Incident 7 of type BROKEN_MACHINE happened and affected tiles 1.")
        assertNextLine("[IMPORTANT] Incident: Incident 8 of type BROKEN_MACHINE happened and affected tiles 6.")
        assertNextLine("[IMPORTANT] Incident: Incident 9 of type BROKEN_MACHINE happened and affected tiles 6.")
        // yeartick 14
        skipLines(5)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 11 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 72945 g of OAT harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 unloads 72945 g of OAT harvest in the shed.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 5 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 4 has collected 708588 g of OAT harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 6.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 unloads 708588 g of OAT harvest in the shed.")
        skipLines(1)
        // yeartick 15
        skipLines(4)
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 finished its actions.")
    }

    private suspend fun tickUntilMay() {
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 4 rained down 70 L water.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 1 on tile 5 rained down 70 L water.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 2 on tile 10 rained down 100 L water.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 3 on tile 20 rained down 100 L water.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 2 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 2 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 2,4,11.")
        assertNextLine("[IMPORTANT] Incident: Incident 3 of type BEE_HAPPY happened and affected tiles 10.")
        assertNextLine("[IMPORTANT] Incident: Incident 4 of type DROUGHT happened and affected tiles 5.")
        // tick 1, yearTick 5, only clouds and incident
        skipLines(8)
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 10.")
        // yeartick 6
        skipLines(5)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 11 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 1.")
        skipLines(3)
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 4 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 4 has sowed OAT according to sowing plan 5.")
        skipLines(2)
        // no incidents, start yeartick 7
        skipLines(5)
        assertNextLine(WEEDING_MESSAGE_M3)
        skipLines(3)
        assertNextLine("[IMPORTANT] Farm Action: Machine 5 performs WEEDING on tile 4 for 3 days.")
        skipLines(1)
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs MOWING on tile 20 for 3 days.")
        skipLines(2)
        // start yeartick 8
        skipLines(5)
        assertNextLine(WEEDING_MESSAGE_M3)
        skipLines(3)
        assertNextLine(WEEDING_MESSAGE_M5)
        skipLines(2)
        // yeartick 9
        skipLines(5)
        assertNextLine(WEEDING_MESSAGE_M3)
        skipLines(3)
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 5 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 4 has sowed POTATO according to sowing plan 6.")
        skipLines(1)
        assertNextLine(WEEDING_MESSAGE_M5)
        skipLines(2)
    }
}
