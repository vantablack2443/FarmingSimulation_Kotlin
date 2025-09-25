package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * validation for farms to have at least one field or plantaion tile
 */
class FarmNoPlantableTiles : ExampleSystemTestExtension() {
    override val name = "Sowing Plan Fields Diff Farms"
    override val description = "Tests sowing plan parsing"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "farmParserTests/farms.json"
    override val scenario = "farmParserTests/scenario.json"
    override val map = "farmParserTests/mapForFarmNoPlantables.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Initialization Info: mapForFarmNoPlantables.json is invalid.")
    }
}
