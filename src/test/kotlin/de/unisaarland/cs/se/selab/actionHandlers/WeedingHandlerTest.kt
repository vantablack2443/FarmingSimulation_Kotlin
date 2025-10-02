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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WeedingHandlerTest {
    private lateinit var handler: WeedingHandler
    private lateinit var machine: Machine
    private lateinit var simulationMap: SimulationMap
    private lateinit var farm: Farm
    private lateinit var farmstead: Tile
    private lateinit var fieldOne: Tile
    private lateinit var fieldTwo: Tile
    private lateinit var fieldThree: Tile
    private lateinit var fieldFour: Tile

    @BeforeEach
    fun setUp() {
        createMap()
        fieldOne.actionsNeeded.add(ActionType.WEEDING)
        fieldTwo.actionsNeeded.add(ActionType.WEEDING)
        fieldThree.actionsNeeded.add(ActionType.WEEDING)
        fieldFour.actionsNeeded.add(ActionType.WEEDING)

        machine = Machine(
            0, "#0", 3, farmstead, listOf(ActionType.WEEDING),
            listOf(PlantType.POTATO, PlantType.WHEAT, PlantType.OAT, PlantType.PUMPKIN), farmstead
        )

        handler = WeedingHandler(simulationMap, PlantData())

        farm = Farm(
            0, "Farm", listOf(farmstead),
            mutableListOf(fieldOne, fieldTwo, fieldThree, fieldFour), mutableListOf(),
            listOf(machine), mutableMapOf()
        )
        machine.farmID = farm.getId()
        fieldOne.farmID = farm.getId()
        fieldTwo.farmID = farm.getId()
        fieldThree.farmID = farm.getId()
        fieldFour.farmID = farm.getId()
        farmstead.farmID = farm.getId()
    }

    private fun createMap() {
        fieldOne = Tile(
            0, Coordinate(0, 0),
            TileType.FIELD, TileShape.OCTAGONAL
        )
        fieldOne.currentCrop = PlantType.POTATO
        fieldOne.plant = Plant.createPlant(PlantType.POTATO)

        fieldTwo = Tile(
            1, Coordinate(2, 0),
            TileType.FIELD, TileShape.OCTAGONAL
        )
        fieldTwo.currentCrop = PlantType.WHEAT
        fieldTwo.plant = Plant.createPlant(PlantType.WHEAT)

        fieldThree = Tile(
            2, Coordinate(4, 0),
            TileType.FIELD, TileShape.OCTAGONAL
        )
        fieldThree.currentCrop = PlantType.OAT
        fieldThree.plant = Plant.createPlant(PlantType.OAT)

        fieldFour = Tile(
            3, Coordinate(6, 0),
            TileType.FIELD, TileShape.OCTAGONAL
        )
        fieldFour.currentCrop = PlantType.PUMPKIN
        fieldFour.plant = Plant.createPlant(PlantType.PUMPKIN)

        farmstead = Tile(
            4, Coordinate(3, 1),
            TileType.FARMSTEAD, TileShape.SQUARE
        )
        val tiles = mutableMapOf<Coordinate, Tile>()
        tiles[fieldOne.location] = fieldOne
        tiles[fieldTwo.location] = fieldTwo
        tiles[fieldThree.location] = fieldThree
        tiles[fieldFour.location] = fieldFour
        tiles[farmstead.location] = farmstead
        simulationMap = SimulationMap(tiles)
    }

    @Test
    fun testWeeding() {
        handler.startPhase(farm, machine)
        assertFalse(fieldOne.actionsNeeded.contains(ActionType.WEEDING))
        assertFalse(fieldTwo.actionsNeeded.contains(ActionType.WEEDING))
        assertFalse(fieldThree.actionsNeeded.contains(ActionType.WEEDING))
        assertFalse(fieldFour.actionsNeeded.contains(ActionType.WEEDING))
    }

    @Test
    fun testMachineCannotWeed() {
        val nonWeeder = Machine(
            machine.id,
            machine.name,
            machine.duration,
            machine.currentTile,
            listOf(ActionType.CUTTING),
            machine.plants,
            machine.homeShed
        )
        handler.startPhase(farm, nonWeeder)
        assert(fieldOne.actionsNeeded.contains(ActionType.WEEDING))
        assert(fieldTwo.actionsNeeded.contains(ActionType.WEEDING))
        assert(fieldThree.actionsNeeded.contains(ActionType.WEEDING))
        assert(fieldFour.actionsNeeded.contains(ActionType.WEEDING))
    }

    @Test
    fun testMachineAlreadyUsed() {
        farm.machineHashMap.add(machine.id)
        handler.startPhase(farm, machine)
        assert(fieldOne.actionsNeeded.contains(ActionType.WEEDING))
        assert(fieldTwo.actionsNeeded.contains(ActionType.WEEDING))
        assert(fieldThree.actionsNeeded.contains(ActionType.WEEDING))
        assert(fieldFour.actionsNeeded.contains(ActionType.WEEDING))
    }

    @Test
    fun testMachineCantContinue() {
        machine.elapsedTime = machine.duration * 3
        handler.startPhase(farm, machine)
        assert(machine.currentTile == machine.homeShed)
    }

    @Test
    fun testTileAlreadyHandled() {
        farm.tileHashMap.add(fieldOne.id)
        handler.startPhase(farm, machine)
        assert(fieldOne.actionsNeeded.contains(ActionType.WEEDING))
        assertFalse(fieldTwo.actionsNeeded.contains(ActionType.WEEDING))
        assertFalse(fieldThree.actionsNeeded.contains(ActionType.WEEDING))
        assertFalse(fieldFour.actionsNeeded.contains(ActionType.WEEDING))
    }

    @Test
    fun testNoOperableTiles() {
        fieldOne.actionsNeeded.clear()
        fieldTwo.actionsNeeded.clear()
        fieldThree.actionsNeeded.clear()
        fieldFour.actionsNeeded.clear()
        handler.startPhase(farm, machine)
        assert(fieldOne.actionsNeeded.isEmpty())
        assert(fieldTwo.actionsNeeded.isEmpty())
        assert(fieldThree.actionsNeeded.isEmpty())
        assert(fieldFour.actionsNeeded.isEmpty())
    }

    @Test
    fun testUnimplementedFunctions() {
        handler.getOperableTiles(farm, PlantType.POTATO, 0)
        handler.startPhase(farm, 0, 0)
        handler.performAction(machine, fieldOne, 0)
        handler.startPhase(farm, machine, 0)
    }

    @Test
    fun testGetOperableTilesReturnsWeedingTiles() {
        val operableTiles = handler.getOperableTiles(farm)
        assert(operableTiles.isEmpty())
    }
}
