package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * No farm phase and multiple animal attacks are valid, tesing if the tile would only be affected once
 */
class NoFarmMultipleValidAnimalAttack : ExampleSystemTestExtension() {
    override val name = "NoFarmMultipleValidAnimalAttack"
    override val description = "No farm phase and multiple animal attacks are valid," +
        " tesing if the tile would only be affected once"

    override val farms = "NoFarmMultipleValidAnimalAttack/farm.json"
    override val scenario = "NoFarmMultipleValidAnimalAttack/scenario.json"
    override val map = "NoFarmMultipleValidAnimalAttack/map.json"
    override val logLevel = "DEBUG"
    override val maxTicks = 1
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
[INFO] Initialization Info: farm.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 1 within the year.
[INFO] Simulation Info: Tick 0 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 5.
[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 5.
[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1239300 g of APPLE.
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
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1239300 g.
        """.trimIndent()
    }
}
