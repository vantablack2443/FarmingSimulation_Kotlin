package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

class NoFarmPhaseCloudCreationValidPlusMergeTestingOrder : ExampleSystemTestExtension() {
    override val name = "NoFarmPhaseCloudCreationValidPlusMergeTestingOrder"
    override val description ="No farm phase and cloud creation is valid plus merge that tests the order of merges and creation of clouds"
    override val farms ="NoFarmCloudValidMergeOrder/farm.json"

    override val scenario ="NoFarmCloudValidMergeOrder/scenario.json"
    override val map = "NoFarmCloudValidMergeOrder/map.json"
    override val logLevel= "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1


    override suspend fun run() {
        val lineIterator = result().lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
    private fun result() : String {
        return """
 [INFO] Initialization Info: map.json successfully parsed and validated.
 [INFO] Initialization Info: farm.json successfully parsed and validated.
 [INFO] Initialization Info: scenario.json successfully parsed and validated.
 [INFO] Simulation Info: Simulation started at tick 1 within the year.
 [INFO] Simulation Info: Tick 0 started at tick 1 within the year.
 [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
 [IMPORTANT] Farm: Farm 1 starts its actions.
 [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 1.
 [IMPORTANT] Farm: Farm 1 finished its actions.
 [IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles 2,3,4.
 [IMPORTANT] Cloud Union: Clouds 3 and 2 united to cloud 4 with 5000 L water and duration 4 on tile 2.
 [IMPORTANT] Cloud Union: Clouds 6 and 1 united to cloud 7 with 6000 L water and duration 4 on tile 4.
 [IMPORTANT] Simulation Info: Simulation ended at tick 1.
 [IMPORTANT] Simulation Info: Simulation statistics are calculated.
 [IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.
 [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
 [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g.
    """.trimIndent()
    }
}