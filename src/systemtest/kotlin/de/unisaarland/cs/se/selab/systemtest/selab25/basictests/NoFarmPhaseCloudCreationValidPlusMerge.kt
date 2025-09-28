package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

class NoFarmPhaseCloudCreationValidPlusMerge : ExampleSystemTestExtension() {
    override val name = "NoFarmPhaseCloudCreationValidPlusMerge"
    override val description = "No farm phase and cloud creation is valid plus merge"
    override val farms = "NoFarmPhaseCloudCreationValidPlusMerge/farm.json"

    override val scenario = "NoFarmPhaseCloudCreationValidPlusMerge/scenario.json"
    override val map = "NoFarmPhaseCloudCreationValidPlusMerge/map.json"
    override val logLevel= "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1


    override suspend fun run() {
        val lines = result().lines()
        var afterMarker = false
        for (line in lines) {
            assertNextLine(line)
            if (line == "[IMPORTANT] Farm: Farm 1 finished its actions.") {
                afterMarker = true
            }
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
 [IMPORTANT] Cloud Union: Clouds 4 and 1 united to cloud 5 with 6000 L water and duration 4 on tile 4.
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
