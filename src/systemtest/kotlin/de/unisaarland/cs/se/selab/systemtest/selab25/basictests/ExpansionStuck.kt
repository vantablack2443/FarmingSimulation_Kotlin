package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests city expansion then machine goes to harvest then breaks
 */
class ExpansionStuck : ExampleSystemTestExtension() {
    override val name = "ExpansionStuck"
    override val description = "Tests that a machine that is broken and then sown is still broken"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "ExpansionStuck/farm.json"
    override val scenario = "ExpansionStuck/scenario.json"
    override val map = "ExpansionStuck/map.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 3
    override val startYearTick = 16

    val hi = """
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 0 of type CITY_EXPANSION happened and affected tiles 1.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 2 for 4 days.
[IMPORTANT] Farm Harvest: Machine 0 has collected 1115370 g of APPLE harvest.
[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Incident: Incident 1 of type BROKEN_MACHINE happened and affected tiles 2.
[IMPORTANT] Farm: Farm 0 starts its actions.
[IMPORTANT] Farm: Farm 0 finished its actions.
[IMPORTANT] Simulation Info: Simulation ended at tick 3.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 0 collected 1115370 g of harvest.
    """.trimIndent()

    override suspend fun run() {
        val lineIterator = hi.lines().iterator()
        while (lineIterator.hasNext()) {
            val currentLine = lineIterator.next()
            assertNextLine(currentLine)
        }
    }
}
