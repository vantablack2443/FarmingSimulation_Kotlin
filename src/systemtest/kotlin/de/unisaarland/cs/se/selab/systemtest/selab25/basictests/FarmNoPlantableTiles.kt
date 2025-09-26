package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * validation for farms to have at least one field or plantation tile
 */
class FarmNoPlantableTiles : ExampleSystemTestExtension() {
    override val name = "Farm With No Plantable Tiles"
    override val description = "validates parsing with no plantable tiles"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "farmParserTests/farmsNoPlantables.json"
    override val scenario = "farmParserTests/scenario.json"
    override val map = "farmParserTests/mapForFarmNoPlantables.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        skipLines(1)
        assertNextLine("[IMPORTANT] Initialization Info: farmsNoPlantables.json is invalid.")
    }
}
