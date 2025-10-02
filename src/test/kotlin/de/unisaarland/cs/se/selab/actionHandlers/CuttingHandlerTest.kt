package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class CuttingHandlerTest {
    private lateinit var handler: CuttingHandler
    private lateinit var machine: Machine
    private lateinit var simulationMap: SimulationMap
    private lateinit var farm: Farm
    private lateinit var farmstead: Tile
    private lateinit var plantation1: Tile
    private lateinit var plantation2: Tile
    private lateinit var plantation3: Tile
    private lateinit var plantation4: Tile

    @BeforeEach
    fun setUp() {
        createMap()
        plantation1.actionsNeeded.add(ActionType.CUTTING)
        plantation2.actionsNeeded.add(ActionType.CUTTING)
        plantation3.actionsNeeded.add(ActionType.CUTTING)
        plantation4.actionsNeeded.add(ActionType.CUTTING)

        machine = Machine(
            0, "#0", 3, farmstead, listOf(ActionType.CUTTING),
            listOf(PlantType.ALMOND, PlantType.APPLE, PlantType.GRAPE, PlantType.CHERRY), farmstead
        )

        handler = CuttingHandler(simulationMap, PlantData())

        farm = Farm(
            0, "Farm", listOf(farmstead),
            mutableListOf(), mutableListOf(plantation1, plantation2, plantation3, plantation4),
            listOf(machine), mutableMapOf()
        )
        machine.farmID = farm.getId()
        plantation1.farmID = farm.getId()
        plantation2.farmID = farm.getId()
        plantation3.farmID = farm.getId()
        plantation4.farmID = farm.getId()
        farmstead.farmID = farm.getId()
    }

    private fun createMap() {
        plantation1 = Tile(
            1, Coordinate(0, 0),
            TileType.PLANTATION, TileShape.OCTAGONAL
        )
        plantation1.plant = Plant.createPlant(PlantType.ALMOND)
        plantation1.currentCrop = PlantType.ALMOND
        plantation1.plantationDamaged = false

        plantation2 = Tile(
            2, Coordinate(2, 0),
            TileType.PLANTATION, TileShape.OCTAGONAL
        )
        plantation2.plant = Plant.createPlant(PlantType.APPLE)
        plantation2.currentCrop = PlantType.APPLE
        plantation2.plantationDamaged = false

        plantation3 = Tile(
            3, Coordinate(4, 0),
            TileType.PLANTATION, TileShape.OCTAGONAL
        )
        plantation3.plant = Plant.createPlant(PlantType.GRAPE)
        plantation3.currentCrop = PlantType.GRAPE
        plantation3.plantationDamaged = false

        plantation4 = Tile(
            4, Coordinate(6, 0),
            TileType.PLANTATION, TileShape.OCTAGONAL
        )
        plantation4.plant = Plant.createPlant(PlantType.CHERRY)
        plantation4.currentCrop = PlantType.CHERRY
        plantation4.plantationDamaged = false

        farmstead = Tile(
            5, Coordinate(-1, -1),
            TileType.FARMSTEAD, TileShape.SQUARE
        )

        val tiles = mutableMapOf<Coordinate, Tile>()
        tiles[plantation1.location] = plantation1
        tiles[plantation2.location] = plantation2
        tiles[plantation3.location] = plantation3
        tiles[plantation4.location] = plantation4
        tiles[farmstead.location] = farmstead
        simulationMap = SimulationMap(tiles)
    }

    @Test
    fun testSimpleCutting() {
        handler.startPhase(farm, 4, 0)
        assertFalse(plantation1.actionsNeeded.contains(ActionType.CUTTING))
        assertFalse(plantation2.actionsNeeded.contains(ActionType.CUTTING))
        assertFalse(plantation3.actionsNeeded.contains(ActionType.CUTTING))
        assertFalse(plantation4.actionsNeeded.contains(ActionType.CUTTING))
    }

    @Test
    fun testCuttingNotRequired() {
        plantation1.actionsNeeded.clear()
        plantation2.actionsNeeded.clear()
        plantation3.actionsNeeded.clear()
        plantation4.actionsNeeded.clear()

        handler.startPhase(farm, 4, 0)

        assertFalse(plantation1.actionsNeeded.contains(ActionType.CUTTING))
        assertFalse(plantation2.actionsNeeded.contains(ActionType.CUTTING))
        assertFalse(plantation3.actionsNeeded.contains(ActionType.CUTTING))
        assertFalse(plantation4.actionsNeeded.contains(ActionType.CUTTING))
    }

    @Test
    fun testMachineRunsOutOfTimeDuringContinue() {
        plantation2.actionsNeeded.clear()
        plantation3.actionsNeeded.clear()
        plantation4.actionsNeeded.clear()

        machine.elapsedTime = machine.duration * 3

        handler.startPhase(farm, 4, 0)

        assertTrue(machine.currentTile == machine.homeShed)
        assertFalse(plantation1.actionsNeeded.contains(ActionType.CUTTING))
    }
}
