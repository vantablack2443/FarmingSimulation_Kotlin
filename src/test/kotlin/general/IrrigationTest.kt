package general
/*
import de.unisaarland.cs.se.selab.actionHandlers.IrrigationHandler
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IrrigationTest {
    private lateinit var farmstead: Tile
    private lateinit var plantationOne: Tile
    private lateinit var plantationTwo: Tile
    private lateinit var plantationThree: Tile
    private lateinit var field: Tile
    private lateinit var machine: Machine
    private lateinit var map: SimulationMap
    private lateinit var handler: IrrigationHandler
    private lateinit var farm: Farm

    @BeforeEach
    fun setUp() {
        createMap()
        plantationOne.actionsNeeded.add(ActionType.IRRIGATING)
        plantationTwo.actionsNeeded.add(ActionType.IRRIGATING)
        plantationThree.actionsNeeded.add(ActionType.IRRIGATING)
        field.actionsNeeded.add(ActionType.IRRIGATING)
        machine = Machine(
            1, "machine", 3, farmstead,
            listOf(ActionType.IRRIGATING), listOf(PlantType.POTATO, PlantType.GRAPE), farmstead
        )
        handler = IrrigationHandler(map, PlantData())
        farm = Farm(
            1, "farm", listOf(farmstead),
            mutableListOf(field), mutableListOf(plantationOne, plantationTwo, plantationThree),
            listOf(machine), mutableMapOf()
        )
        machine.farmID = farm.getId()
        plantationOne.farmID = farm.getId()
        plantationTwo.farmID = farm.getId()
        plantationThree.farmID = farm.getId()
        field.farmID = farm.getId()
    }

    private fun createMap() {
        plantationOne = Tile(
            3, Coordinate(8, 2),
            TileType.PLANTATION, TileShape.OCTAGONAL
        )
        plantationOne.maxMoisture = 200
        plantationOne.plant = Plant.createPlant(PlantType.CHERRY)
        plantationOne.currentMoisture = 80
        plantationOne.currentCrop = PlantType.APPLE
        plantationOne.plantationDamaged = false
        plantationTwo = Tile(
            6,
            Coordinate(10, 4),
            TileType.PLANTATION,
            TileShape.OCTAGONAL
        )
        plantationTwo.maxMoisture = 150
        plantationTwo.plant = Plant.createPlant(PlantType.APPLE)
        plantationTwo.currentCrop = PlantType.APPLE
        plantationTwo.currentMoisture = 50
        plantationTwo.plantationDamaged = false
        farmstead = Tile(
            1,
            Coordinate(5, 1),
            TileType.FARMSTEAD,
            TileShape.SQUARE
        )
        plantationThree = Tile(
            4, Coordinate(6, 4),
            TileType.PLANTATION, TileShape.OCTAGONAL
        )
        plantationThree.plantationDamaged = true
        plantationThree.maxMoisture = 100
        plantationThree.currentMoisture = 0
        field = Tile(2, Coordinate(6, 2), TileType.FIELD, TileShape.OCTAGONAL)
        field.plant = Plant.createPlant(PlantType.OAT)
        field.maxMoisture = 300
        field.currentMoisture = 150
        field.currentCrop = PlantType.OAT
        val road =
            Tile(5, Coordinate(8, 4), TileType.ROAD, TileShape.OCTAGONAL)
        val tiles = mutableMapOf<Coordinate, Tile>()
        tiles[plantationOne.location] = plantationOne
        tiles[plantationTwo.location] = plantationTwo
        tiles[plantationThree.location] = plantationThree
        tiles[field.location] = field
        tiles[road.location] = road
        tiles[farmstead.location] = farmstead
        this.map = SimulationMap(tiles)
    }

    @Test
    fun testIrrigation() {
        handler.startPhase(farm, machine, TileType.FIELD)
        assertEquals(300, field.currentMoisture)
        assertEquals(200, plantationOne.currentMoisture)
        assertEquals(150, plantationTwo.currentMoisture)
        assertEquals(0, plantationThree.currentMoisture)
    }
}

 */
