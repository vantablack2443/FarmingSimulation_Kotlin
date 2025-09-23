package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.Drought
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class IncidentsTest {
    private lateinit var mockMap: SimulationMap
    private lateinit var mockFarm: Farm
    private lateinit var potato: Plant
    private lateinit var oat: Plant
    private lateinit var grape: Plant
    private lateinit var apple: Plant
    private lateinit var oatTile: Tile
    private lateinit var grapeTile: Tile
    private lateinit var appleTile: Tile
    private lateinit var potatoTile: Tile

    @BeforeEach
    fun setUp() {
        potatoTile = Tile(
            1,
            Coordinate(6, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        oatTile = Tile(
            2,
            Coordinate(8, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        grapeTile = Tile(8, Coordinate(4, 4), TileType.PLANTATION, TileShape.OCTAGONAL)
        appleTile = Tile(10, Coordinate(6, 4), TileType.PLANTATION, TileShape.OCTAGONAL)
        val fieldTiles = listOf(
            Tile(4, Coordinate(6, 2), TileType.FIELD, TileShape.OCTAGONAL),
            Tile(5, Coordinate(8, 2), TileType.FIELD, TileShape.OCTAGONAL)
        )
        val forestTiles = listOf(
            Tile(22, Coordinate(8, 4), TileType.FOREST, TileShape.OCTAGONAL),
            Tile(23, Coordinate(10, 4), TileType.FOREST, TileShape.OCTAGONAL)
        )
        val roadTile = Tile(20, Coordinate(7, 3), TileType.ROAD, TileShape.SQUARE)
        val tiles = mutableMapOf(
            potatoTile.location to potatoTile,
            oatTile.location to oatTile,
            grapeTile.location to grapeTile,
            appleTile.location to appleTile,
            roadTile.location to roadTile
        )
        forestTiles.forEach { tiles[it.location] = it }
        fieldTiles.forEach { tiles[it.location] = it }
        mockMap = SimulationMap(tiles)
        apple = Plant.createPlant(PlantType.APPLE)
        grape = Plant.createPlant(PlantType.GRAPE)
        potato = Plant.createPlant(PlantType.POTATO)
        oat = Plant.createPlant(PlantType.OAT)
        potatoTile.plant = potato
        oatTile.plant = oat
        grapeTile.plant = grape
        appleTile.plant = apple
        val farmFields = fieldTiles + oatTile + potatoTile
        mockFarm = Farm(
            1, "farm", listOf(),
            farmFields.toMutableList(), mutableListOf(appleTile, grapeTile),
            mutableListOf(), mutableMapOf(), mutableMapOf()
        )
        apple.actionsNeeded.add(ActionType.MOW)
    }

    @Test
    fun testAnimalAttack() {
        val animalAttack = AnimalAttack(
            1,
            16,
            IncidentType.ANIMAL_ATTACK,
            mockMap.getTileByID(20)!!,
            2
        )
        animalAttack.execute(mockMap, 17)
        assertEquals(0.9, apple.animalAttackPenalty)
        assertEquals(0.5, grape.animalAttackPenalty)
        assertEquals(0.5, oat.animalAttackPenalty)
        assertEquals(0.5, potato.animalAttackPenalty)
        assertFalse { apple.actionsNeeded.contains(ActionType.MOW) }
    }

    @Test
    fun droughtTest() {
        val drought = Drought(
            2,
            16,
            IncidentType.DROUGHT,
            mockMap.getTileByID(20)!!,
            2
        )
        drought.execute(mockMap, 17)
        assertEquals(0, oatTile.currentMoisture)
        assertEquals(0, grapeTile.currentMoisture)
        assertEquals(0, appleTile.currentMoisture)
        assertEquals(0, potatoTile.currentMoisture)
        assertEquals(true, grapeTile.plantationDamaged)
        assertEquals(true, appleTile.plantationDamaged)
        assertNull(oatTile.plant)
        assertNull(potatoTile.plant)
    }

    @Test
    fun testCityExpansion() {
        TODO()
    }

    @Test
    fun brokenMachineTest() {
        TODO()
    }

    @Test
    fun cloudCreationTest() {
        TODO()
    }
}
