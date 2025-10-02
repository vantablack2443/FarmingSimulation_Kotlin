package de.unisaarland.cs.se.selab.basicTests

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.harvestestimatehandler.HarvestEstimateHandler
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Oat
import de.unisaarland.cs.se.selab.plant.Pumpkin
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HarvestEstimateHandlerTest {

    @Test
    fun `apply drought test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Pumpkin()
        tile.droughtHit = true
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyDrought(tile)
        assertEquals(true, acted)
    }

    @Test
    fun `apply drought test null plant`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyDrought(tile)
        assertEquals(false, acted)
    }

    @Test
    fun `apply beehappy test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Pumpkin()
        tile.plant?.pollination?.add(2.0)
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyBeeHappy(tile)
        assertEquals(true, acted)
    }

    @Test
    fun `apply beehappy test null plant`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyBeeHappy(tile)
        assertEquals(false, acted)
    }

    @Test
    fun `apply animal attack test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Pumpkin()
        tile.plant?.animalAttack = true
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyAnimalAttack(tile)
        assertEquals(true, acted)
    }

    @Test
    fun `apply animal atttack null plant`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyAnimalAttack(tile)
        assertEquals(false, acted)
    }

    @Test
    fun `apply missed mowing test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Pumpkin()
        tile.actionsNeeded.add(ActionType.MOWING)
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyMissedMowing(tile)
        assertEquals(true, acted)
    }

    @Test
    fun `apply missed mowing null plant`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyMissedMowing(tile)
        assertEquals(false, acted)
    }

    @Test
    fun `apply late harvest test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Oat()
        tile.actionsNeeded.add(ActionType.HARVESTING)
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyLateHarvest(tile, 17)
        assertEquals(true, acted)
    }

    @Test
    fun `apply late harvest null plant`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyLateHarvest(tile, 14)
        assertEquals(false, acted)
    }

    @Test
    fun `apply moisture full test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Pumpkin()
        tile.actionsNeeded.add(ActionType.MOWING)
        tile.maxMoisture = 1000
        tile.currentMoisture = 1000
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyMoisture(tile)
        assertEquals(false, acted)
    }

    @Test
    fun `apply moisture zero test`() {
        val tile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        tile.plant = Pumpkin()
        tile.plant?.harvestEstimate = 10000
        tile.maxMoisture = 80
        tile.currentMoisture = 0
        val simMap = SimulationMap(mutableMapOf(Pair(tile.location, tile)))
        val harvestEstHandler = HarvestEstimateHandler(simMap)
        val acted = harvestEstHandler.applyMoisture(tile)
        assertEquals(true, acted)
    }
}
