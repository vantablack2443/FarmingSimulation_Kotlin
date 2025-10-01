package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IncidentsTest {
    private lateinit var mockMap: SimulationMap
    private lateinit var mockFarm: Farm
    private lateinit var potato: FieldPlant
    private lateinit var oat: Plant
    private lateinit var grape: Plant
    private lateinit var apple: Plant
    private lateinit var oatTile: Tile
    private lateinit var grapeTile: Tile
    private lateinit var appleTile: Tile
    private lateinit var potatoTile: Tile
    private lateinit var mockMachine: Machine

    @BeforeEach
    fun setUp() {
        createMap()
        apple = Plant.createPlant(PlantType.APPLE)
        grape = Plant.createPlant(PlantType.GRAPE)
        potato = Plant.createPlant(PlantType.POTATO) as FieldPlant
        potato.sownSimTick = 10
        oat = Plant.createPlant(PlantType.OAT)
        potatoTile.plant = potato
        potatoTile.currentCrop = PlantType.POTATO
        oatTile.plant = oat
        oatTile.currentCrop = PlantType.OAT
        grapeTile.plant = grape
        grapeTile.currentCrop = PlantType.GRAPE
        appleTile.plant = apple
        appleTile.currentCrop = PlantType.APPLE
        val farmFields = mockMap.getPlantableTiles().filter { it.category == TileType.FIELD } + oatTile + potatoTile
        val farmstead = Tile(
            40,
            Coordinate(9, 3),
            TileType.FARMSTEAD,
            TileShape.SQUARE
        )
        mockMachine = Machine(
            1, "machine", 2, mockMap.getTileByID(4)!!,
            listOf(ActionType.IRRIGATING), emptyList(), farmstead
        )
        mockFarm = Farm(
            1, "farm", listOf(farmstead),
            farmFields.toMutableList(), mutableListOf(appleTile, grapeTile),
            mutableListOf(mockMachine), mutableMapOf(), mutableMapOf()
        )
        mockFarm.getFields().forEach { it.farmID = mockFarm.getId() }
        mockFarm.getPlantation().forEach { it.farmID = mockFarm.getId() }
        mockFarm.getFarmstead().forEach { it.farmID = mockFarm.getId() }
    }
    private fun createMap() {
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
        val villageTile = Tile(11, Coordinate(5, 1), TileType.VILLAGE, TileShape.SQUARE)
        val fieldTiles = listOf(
            Tile(4, Coordinate(6, 2), TileType.FIELD, TileShape.OCTAGONAL),
            Tile(5, Coordinate(8, 2), TileType.FIELD, TileShape.OCTAGONAL)
        )
        val forestTiles = listOf(
            Tile(22, Coordinate(8, 4), TileType.FOREST, TileShape.OCTAGONAL),
            Tile(23, Coordinate(10, 4), TileType.FOREST, TileShape.OCTAGONAL)
        )
        val roadTile = Tile(20, Coordinate(7, 3), TileType.ROAD, TileShape.SQUARE)
        val meadowTiles = listOf(
            Tile(45, Coordinate(5, 3), TileType.MEADOW, TileShape.SQUARE),
            Tile(46, Coordinate(5, -1), TileType.MEADOW, TileShape.SQUARE)
        )
        val tiles = mutableMapOf(
            potatoTile.location to potatoTile,
            oatTile.location to oatTile,
            grapeTile.location to grapeTile,
            appleTile.location to appleTile,
            roadTile.location to roadTile,
            villageTile.location to villageTile
        )
        forestTiles.forEach { tiles[it.location] = it }
        fieldTiles.forEach { tiles[it.location] = it }
        meadowTiles.forEach { tiles[it.location] = it }
        mockMap = SimulationMap(tiles)
    }

//    @Test
//    fun testAnimalAttack() {
//        appleTile.actionsNeeded.add(ActionType.MOWING)
//        val animalAttack = AnimalAttack(
//            1,
//            16,
//            IncidentType.ANIMAL_ATTACK,
//            mockMap.getTileByID(20)!!,
//            2
//        )
//        animalAttack.execute(mockMap, 17)
//        assertEquals(0.9, apple.animalAttackPenalty[0])
//        assertTrue(grape.animalAttackPenalty.isEmpty())
//        assertTrue(oat.animalAttackPenalty.isEmpty())
//        assertTrue(potato.animalAttackPenalty.isEmpty())
//    }

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
    }

    @Test
    fun testCityExpansion() {
        val affectedTile = mockMap.getTileByID(4)!!
        val cityExpansion = CityExpansion(
            3,
            16,
            IncidentType.CITY_EXPANSION,
            affectedTile,
            listOf(mockFarm)
        )
        cityExpansion.execute(mockMap, 17)
        assertEquals(TileType.VILLAGE, affectedTile.category)
        assert(!mockFarm.getFields().contains(affectedTile))
        assertNull(affectedTile.currentMoisture)
        assertEquals(0, affectedTile.currentSunlight)
        assertNull(affectedTile.plant)
        assertTrue { mockMachine.isStuck }
    }

    @Test
    fun brokenMachineTest() {
        val brokenMachine = BrokenMachine(
            4,
            16,
            IncidentType.BROKEN_MACHINE,
            mockMachine,
            Duration(17, 20)
        )
        brokenMachine.execute(mockMap, 17)
        assertTrue(mockMachine.brokenFor != null)
        assertEquals(17, mockMachine.brokenFor!!.startTick)
        assertEquals(20, mockMachine.brokenFor!!.endTick)
    }

    @Test
    fun beeHappyTest() {
        val beeHappy = BeeHappy(
            5,
            16,
            IncidentType.BEE_HAPPY,
            30,
            mockMap.getTileByCoordinate(Coordinate(8, 2))!!,
            4
        )

        beeHappy.execute(mockMap, 9) // apple is blooming
//        assertEquals(1.3, apple.pollination)
//        assertEquals(1.0, grape.pollination)
//        assertEquals(1.0, oat.pollination)
//        assertEquals(1.0, potato.pollination)
    }

    @Test
    fun beeHappyOnFields() {
        val beeHappy = BeeHappy(
            6,
            14,
            IncidentType.BEE_HAPPY,
            35,
            mockMap.getTileByCoordinate(Coordinate(8, 2))!!,
            4
        )
        val pumpkin = Plant.createPlant(PlantType.PUMPKIN) as FieldPlant
        pumpkin.sownSimTick = 10
        val pumpkinTile = mockMap.getTileByCoordinate(Coordinate(6, 2))!!
        pumpkinTile.plant = pumpkin
        beeHappy.execute(mockMap, 14) // potato and pumpkin blooming
//        assertEquals(1.0, potato.pollination)
//        assertEquals(1.35, pumpkin.pollination)
    }
}
