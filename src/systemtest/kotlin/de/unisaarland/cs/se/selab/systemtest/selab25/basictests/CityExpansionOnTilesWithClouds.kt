package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * City expansion on tiles with pre-existing cloud
 */
class CityExpansionOnTilesWithClouds : ExampleSystemTestExtension() {
    override val name = "CloudCreationOnCityExpansions"
    override val description = "Tests city expansion on tiles with pre-existing clouds."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "CloudCreationOnCityExpansions/farms.json"
    override val scenario = "CloudCreationOnCityExpansions/scenario.json"
    override val map = "CloudCreationOnCityExpansions/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        val lineIterator = result().lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun result(): String {
        return """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farms.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 1 within the year.
[INFO] Simulation Info: Tick 0 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[DEBUG] Cloud Position: Cloud 1 is on tile 2, where the amount of sunlight is 48.
[DEBUG] Cloud Position: Cloud 3 is on tile 4, where the amount of sunlight is 48.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 0 of type CITY_EXPANSION happened and affected tiles 1.
[INFO] Cloud Dissipation: Cloud 0 got stuck on tile 1.
[IMPORTANT] Incident: Incident 1 of type CITY_EXPANSION happened and affected tiles 2.
[INFO] Cloud Dissipation: Cloud 1 got stuck on tile 2.
[IMPORTANT] Incident: Incident 2 of type CITY_EXPANSION happened and affected tiles 3.
[INFO] Cloud Dissipation: Cloud 2 got stuck on tile 3.
[IMPORTANT] Incident: Incident 3 of type CITY_EXPANSION happened and affected tiles 4.
[INFO] Cloud Dissipation: Cloud 3 got stuck on tile 4.
[INFO] Simulation Info: Tick 1 started at tick 2 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 0 starts its actions.
[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Simulation Info: Simulation ended at tick 2.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.
[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 800000 g.
        """.trimIndent()
    }
}
