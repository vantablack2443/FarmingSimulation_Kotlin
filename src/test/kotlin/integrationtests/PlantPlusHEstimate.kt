package integrationtests

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.harvestestimatehandler.HarvestEstimateHandler
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlantPlusHEstimate {

    @Test
    fun testPlantAndHarvestEstimate() {
        val plantation = Tile(
            0,
            Coordinate(0, 0),
            TileType.PLANTATION,
            TileShape.OCTAGONAL
        )

        plantation.currentCrop = PlantType.ALMOND
        plantation.plant = Plant.createPlant(PlantType.ALMOND)
        plantation.maxMoisture = 3000
        plantation.plant?.neededMoisture = 500
        plantation.currentMoisture = 3000
        plantation.currentSunlight = 50
        plantation.plant?.neededSunlight = 50

        val tiles = mutableMapOf<Coordinate, Tile>()
        tiles[Coordinate(0, 0)] = plantation
        val simulationMap = SimulationMap(tiles)

        val handler = HarvestEstimateHandler(simulationMap)
        handler.estimateHarvest(6, 0)
        val estimate = plantation.plant?.harvestEstimate

        // Assert: Check the estimate is as expected
        assertEquals<Int>(800000, estimate ?: 0) // Replace 100 with expected value
    }

    @Test
    fun testHarvestEstimateVariousScenarios() {
        val plantation = Tile(
            1,
            Coordinate(1, 1),
            TileType.PLANTATION,
            TileShape.OCTAGONAL
        )
        plantation.currentCrop = PlantType.APPLE
        plantation.plant = Plant.createPlant(PlantType.APPLE)
        plantation.maxMoisture = 2000
        plantation.currentMoisture = 1000 // Not enough moisture
        plantation.currentSunlight = 20 // Not enough sunlight
        plantation.plant?.neededMoisture = 1500
        plantation.plant?.neededSunlight = 30

        // Simulate animal attack and pollination
        plantation.plant?.animalAttack = true
        plantation.plant?.doAnimalAttack()
        plantation.plant?.doBeeHappy(1.2)
        plantation.plant?.applyPollinationBuff()

        // Simulate late sowing
        val lateActions = mutableListOf<de.unisaarland.cs.se.selab.enumerations.ActionType>()
        plantation.plant?.checkLateSowing(lateActions, 100)
        plantation.plant?.applyLateSowingPenalty()

        // Simulate missed actions
        plantation.plant?.applyCuttingPenalty()
        plantation.plant?.applyMowingPenalty()
        plantation.plant?.applyMissedWeedingPenalty()

        // Add to map and estimate
        val tiles = mutableMapOf<Coordinate, Tile>()
        tiles[plantation.location] = plantation
        val simulationMap = SimulationMap(tiles)
        val handler = HarvestEstimateHandler(simulationMap)
        handler.estimateHarvest(6, 0)
        val estimate = plantation.plant?.harvestEstimate

        // Assert: Estimate should be reduced due to penalties
        assert(estimate != null)
        assertEquals(estimate, 991170)
    }
}
