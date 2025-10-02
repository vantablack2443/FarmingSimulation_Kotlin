package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.machine.PlantAndHarvest
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * tests basic functions in the tile class
 */
class FarmTest {
    private lateinit var fields: MutableList<Tile>
    private lateinit var plantations: MutableList<Tile>
    private lateinit var farmsteads: MutableList<Tile>
    private lateinit var farm: Farm
    private lateinit var machines: MutableList<Machine>

    @BeforeEach
    fun setUp() {
        fields = mutableListOf(
            Tile(1, Coordinate(6, 2), TileType.FIELD, TileShape.OCTAGONAL),
            Tile(2, Coordinate(4, 2), TileType.FIELD, TileShape.OCTAGONAL),
            Tile(3, Coordinate(4, 4), TileType.FIELD, TileShape.OCTAGONAL)
        )
        fields[0].currentCrop = PlantType.POTATO
        fields[0].plant = Plant.createPlant(PlantType.POTATO)
        fields[0].currentMoisture = 150
        fields[0].maxMoisture = 300
        fields[0].currentSunlight = 168
        plantations = mutableListOf(
            Tile(4, Coordinate(8, 2), TileType.PLANTATION, TileShape.OCTAGONAL),
            Tile(5, Coordinate(8, 4), TileType.PLANTATION, TileShape.OCTAGONAL)
        )
        plantations[1].currentCrop = PlantType.APPLE
        plantations[1].plant = Plant.createPlant(PlantType.APPLE)
        plantations[1].plant?.setSowingTime(0, 9)
        plantations[1].currentMoisture = 400
        plantations[1].maxMoisture = 400
        plantations[1].currentSunlight = 168
        plantations[0].droughtHit = true
        farmsteads = mutableListOf(
            Tile(6, Coordinate(7, 1), TileType.FARMSTEAD, TileShape.SQUARE)
        )
        machines = mutableListOf(
            Machine(
                1, "First Machine", 3, farmsteads[0],
                listOf(ActionType.WEEDING, ActionType.MOWING),
                listOf(PlantType.APPLE, PlantType.POTATO), farmsteads[0]
            ),
            Machine(
                2, "Second Machine", 4, farmsteads[0],
                listOf(ActionType.HARVESTING), listOf(PlantType.ALMOND), farmsteads[0]
            )
        )
        farm = Farm(
            0, "Test Farm", farmsteads.toList(), fields, plantations,
            machines.toList(),
            mutableMapOf(
                0 to mutableListOf(
                    SowingPlan(0, PlantType.POTATO, 0, mutableListOf(fields[0])),
                    SowingPlan(1, PlantType.OAT, 1, mutableListOf(fields[1]))
                )
            )
        )
    }

    @Test
    fun testFarmFunctions() {
        farm.updateNeededActions(11, 0)
        assertEquals(1, farm.getFields().filter { it.actionsNeeded.isNotEmpty() }.size)
        assertEquals(1, farm.getPlantation().filter { it.actionsNeeded.isNotEmpty() }.size)
        farm.addHarvestPerPlant(PlantType.POTATO, 48000)
        farm.addHarvestPerPlant(PlantType.APPLE, 40000)
        machines[0].currentHarvest = PlantAndHarvest(PlantType.APPLE, 45000)
        machines[1].currentHarvest = PlantAndHarvest(PlantType.APPLE, 5000)
        val appleHarvest = farm.calculatePlantHarvest(PlantType.APPLE)
        assertEquals(90000, appleHarvest)
        val potatoHarvest = farm.calculatePlantHarvest(PlantType.POTATO)
        assertEquals(48000, potatoHarvest)
        val total = farm.calculateTotalHarvest()
        assertEquals(138000, total)
    }
}
