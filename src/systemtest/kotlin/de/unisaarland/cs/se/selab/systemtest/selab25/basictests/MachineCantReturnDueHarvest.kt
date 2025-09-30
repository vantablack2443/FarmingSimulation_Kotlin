package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Does what it says
 */
class MachineCantReturnDueHarvest : ExampleSystemTestExtension() {
    override val name = "MachineCantReturnDueHarvest"
    override val description = "Tests Farm Validation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineActionsTest/machineHarvestCantReturn/farms.json"
    override val scenario = "machineActionsTest/machineHarvestCantReturn/scenario.json"
    override val map = "machineActionsTest/machineHarvestCantReturn/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        val lineIterator = result().lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }

    private fun result(): String {
        return """
[INFO] Initialization Info: animalAttackmap.json successfully parsed and validated.
[INFO] Initialization Info: animalAttackfarms.json successfully parsed and validated.
[INFO] Initialization Info: animalAttackscenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 1 within the year.
[INFO] Simulation Info: Tick 0 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles .
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 1200000 g of CHERRY.
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
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1200000 g.
        """.trimIndent()
    }
}
