package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * tests cloud phase for one tick with cloud creation incidents
 */
class OatWheatYearTestAlternative : ExampleSystemTestExtension() {
    override val name = "OatWheatYearTestAlternative"
    override val description = "Tests simulation for a year on oat and wheat"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullSim/farmsOatWheat.json"
    override val scenario = "fullSim/scenarioOatWheat.json"
    override val map = "fullSim/mapOatWheat.json"

    override val logLevel = "INFO"
    override val maxTicks = 24
    override val startYearTick = 20

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapOatWheat.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmsOatWheat.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenarioOatWheat.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 20 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 20 within the year.")
        skipLines(1) // soil moisture
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 4 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        // assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1500000 g of WHEAT.")
        firstHalf()
        secondHalf()
        statistics()
    }

    private suspend fun weedOat() {
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 3 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
    }
    private suspend fun statistics() {
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 24.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 623765 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 423642 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 200123 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
    }
    private suspend fun firstHalf() {
        // yeartick 21, nothing
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 21 within the year.")
        skipLines(3)
        // yeartick 22, nothing
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 22 within the year.")
        skipLines(3)
        // yeartick 23
        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 23 within the year.")
        skipLines(3)
        // yeartick 24
        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 24 within the year.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 4 for 3 days.")
        skipLines(2) // return and finish
        // yeartick 1
        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 1 within the year.")
        skipLines(3)
        // yeartick 2
        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 2 within the year.")
        skipLines(3)
        // yeartick 3
        assertNextLine("[INFO] Simulation Info: Tick 7 started at tick 3 within the year.")
        skipLines(3)
        // yeartick 4
        assertNextLine("[INFO] Simulation Info: Tick 8 started at tick 4 within the year.")
        skipLines(3)
        // yeartick 5, weed wheat
        assertNextLine("[INFO] Simulation Info: Tick 9 started at tick 5 within the year.")
        skipLines(3)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1350000 g of WHEAT.")
        // yeartick 6
        assertNextLine("[INFO] Simulation Info: Tick 10 started at tick 6 within the year.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 3 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 2.")
        skipLines(1) // return
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 4 for 3 days.")
        skipLines(2) // return and finish
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1215000 g of WHEAT.")
        // yeartick 7
        assertNextLine("[INFO] Simulation Info: Tick 11 started at tick 7 within the year.")
        skipLines(3)
        // weedOat()
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 874800 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 984150 g of WHEAT.")
        // yeartick 8
        assertNextLine("[INFO] Simulation Info: Tick 12 started at tick 8 within the year.")
        skipLines(2)
        weedOat()
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 708588 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 797161 g of WHEAT.")
        // yeartick 9
        assertNextLine("[INFO] Simulation Info: Tick 13 started at tick 9 within the year.")
        skipLines(2)
        weedOat()
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 516560 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 581129 g of WHEAT.")
        // yeartick 10
        assertNextLine("[INFO] Simulation Info: Tick 14 started at tick 10 within the year.")
        skipLines(3) // nothing
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 376571 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 423642 g of WHEAT.")
        // yeartick 11
        assertNextLine("[INFO] Simulation Info: Tick 15 started at tick 11 within the year.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 4 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 423642 g of WHEAT harvest.")
        skipLines(1)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 423642 g of WHEAT harvest in the shed.")
        skipLines(1)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 274518 g of OAT.")
    }
    private suspend fun secondHalf() {
        // yeartick 12
        assertNextLine("[INFO] Simulation Info: Tick 16 started at tick 12 within the year.")
        skipLines(3)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 200123 g of OAT.")
        // yeartick 13
        assertNextLine("[INFO] Simulation Info: Tick 17 started at tick 13 within the year.")
        skipLines(2)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 3 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 200123 g of OAT harvest.")
        skipLines(1)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 unloads 200123 g of OAT harvest in the shed.")
        skipLines(1)
        // yeartick 14
        assertNextLine("[INFO] Simulation Info: Tick 18 started at tick 14 within the year.")
        skipLines(3)
        // yeartick 15
        assertNextLine("[INFO] Simulation Info: Tick 19 started at tick 15 within the year.")
        skipLines(3)
        // yeartick 16
        assertNextLine("[INFO] Simulation Info: Tick 20 started at tick 16 within the year.")
        skipLines(3)
        // yeartick 17
        assertNextLine("[INFO] Simulation Info: Tick 21 started at tick 17 within the year.")
        skipLines(3)
        // yeartick 18
        assertNextLine("[INFO] Simulation Info: Tick 22 started at tick 18 within the year.")
        skipLines(3)
        // yeartick 19
        assertNextLine("[INFO] Simulation Info: Tick 23 started at tick 19 within the year.")
        skipLines(3)
    }
}
