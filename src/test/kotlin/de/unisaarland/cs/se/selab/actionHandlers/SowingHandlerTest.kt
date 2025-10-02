package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile
import kotlin.test.Test
import kotlin.test.assertEquals

class SowingHandlerTest {
    private lateinit var sowingHandler: SowingHandler
    private lateinit var simMap: SimulationMap
    private lateinit var sowPlan: SowingPlan
    private lateinit var machine: Machine
    private lateinit var farm: Farm
    private lateinit var fieldOne: Tile
    private lateinit var fieldTwo: Tile
    private lateinit var fieldThree: Tile
    private lateinit var roadOne: Tile
    private lateinit var roadTwo: Tile

    private var farmstead: Tile = Tile(10, Coordinate(1, 1), TileType.FARMSTEAD, TileShape.SQUARE)

    /**
     * set up for simple sow can sow
     * farm id = 1
     */
    private fun simpleSowCanSowSetup() {
        // field one setup
        fieldOne = Tile(1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldOne.farmID = 1
        fieldOne.maxMoisture = 1000
        fieldOne.possiblePlants = listOf(PlantType.WHEAT)
        fieldOne.currentMoisture = 1000

        // machine setup
        machine = Machine(
            1, "Tractor", 1, farmstead,
            listOf(ActionType.SOWING), listOf(PlantType.WHEAT), farmstead
        )
        machine.farmID = 1

        // farmstead setup
        farmstead = Tile(10, Coordinate(1, 1), TileType.FARMSTEAD, TileShape.SQUARE)
        farmstead.shed = true

        // sow plan setup
        sowPlan = SowingPlan(1, PlantType.WHEAT, 0, listOf(fieldOne))

        // map setup
        simMap = SimulationMap(
            mutableMapOf(
                Pair(fieldOne.location, fieldOne),
                Pair(farmstead.location, farmstead)
            )
        )

        // farm setup
        farm = Farm(
            1, "Farm", listOf(farmstead), mutableListOf(fieldOne),
            mutableListOf(), listOf(machine), mutableMapOf(Pair(0, mutableListOf(sowPlan))),
            mutableMapOf()
        )
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()

        // action handler setup
        sowingHandler = SowingHandler(simMap, PlantData())
    }

    @Test
    fun `simple sowing can sow`() {
        simpleSowCanSowSetup()
        sowingHandler.startPhase(farm, 19, 0)
        assertEquals(PlantType.WHEAT, fieldOne.currentCrop)
        assertEquals(1500000, fieldOne.plant?.harvestEstimate)
    }

    @Test
    fun `simple sowing can't sow broken machine`() {
        simpleSowCanSowSetup()
        machine.brokenFor = Duration(0, 1)
        assertEquals(null, fieldOne.currentCrop)
        assertEquals(null, fieldOne.plant)
    }

    @Test
    fun `simple sowing can't sow tile is fallow`() {
        simpleSowCanSowSetup()
        fieldOne.fallowDuration = Duration(0, 1)
        assertEquals(null, fieldOne.currentCrop)
        assertEquals(null, fieldOne.plant)
    }

    /**
     * set up for simple sow can sow
     * farm id = 1
     */
    private fun simpleSowCantWrongMonthSetup() {
        // field one setup
        fieldOne = Tile(1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldOne.farmID = 1
        fieldOne.maxMoisture = 1000
        fieldOne.possiblePlants = listOf(PlantType.PUMPKIN)
        fieldOne.currentMoisture = 1000

        // machine setup
        machine = Machine(
            1, "Tractor", 1, farmstead,
            listOf(ActionType.SOWING), listOf(PlantType.PUMPKIN), farmstead
        )
        machine.farmID = 1

        // farmstead setup
        farmstead = Tile(10, Coordinate(1, 1), TileType.FARMSTEAD, TileShape.SQUARE)
        farmstead.shed = true

        // sow plan setup
        sowPlan = SowingPlan(1, PlantType.PUMPKIN, 0, listOf(fieldOne))

        // map setup
        simMap = SimulationMap(
            mutableMapOf(
                Pair(fieldOne.location, fieldOne),
                Pair(farmstead.location, farmstead)
            )
        )

        // farm setup
        farm = Farm(
            1, "Farm", listOf(farmstead), mutableListOf(fieldOne),
            mutableListOf(), listOf(machine), mutableMapOf(Pair(0, mutableListOf(sowPlan))),
            mutableMapOf()
        )
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()

        // action handler setup
        sowingHandler = SowingHandler(simMap, PlantData())
    }

    @Test
    fun `simple sowing can't sow on wrong month`() {
        simpleSowCantWrongMonthSetup()
        sowingHandler.startPhase(farm, 19, 0)
        assertEquals(null, fieldOne.currentCrop)
        assertEquals(null, fieldOne.plant)
    }

    private fun simpleSowContinuationSetup() {
        // field setups
        fieldOne = Tile(1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldOne.farmID = 1
        fieldOne.maxMoisture = 1000
        fieldOne.possiblePlants = listOf(PlantType.WHEAT)
        fieldOne.currentMoisture = 1000
        fieldTwo = Tile(2, Coordinate(2, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldTwo.farmID = 1
        fieldTwo.maxMoisture = 1000
        fieldTwo.possiblePlants = listOf(PlantType.WHEAT)
        fieldTwo.currentMoisture = 1000
        fieldThree = Tile(3, Coordinate(4, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldThree.farmID = 1
        fieldThree.maxMoisture = 1000
        fieldThree.possiblePlants = listOf(PlantType.WHEAT)
        fieldThree.currentMoisture = 1000

        // machine setup
        machine = Machine(
            1, "Tractor", 7, farmstead,
            listOf(ActionType.SOWING), listOf(PlantType.WHEAT), farmstead
        )
        machine.farmID = 1

        // farmstead setup
        farmstead = Tile(10, Coordinate(1, 1), TileType.FARMSTEAD, TileShape.SQUARE)
        farmstead.shed = true

        // sow plan setup
        sowPlan = SowingPlan(1, PlantType.WHEAT, 0, listOf(fieldOne, fieldTwo, fieldThree))

        // map setup
        simMap = SimulationMap(
            mutableMapOf(
                Pair(fieldOne.location, fieldOne),
                Pair(fieldTwo.location, fieldTwo),
                Pair(fieldThree.location, fieldThree),
                Pair(farmstead.location, farmstead)
            )
        )

        // farm setup
        farm = Farm(
            1, "Farm", listOf(farmstead), mutableListOf(fieldOne, fieldTwo, fieldThree),
            mutableListOf(), listOf(machine), mutableMapOf(Pair(0, mutableListOf(sowPlan))),
            mutableMapOf()
        )
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()

        // action handler setup
        sowingHandler = SowingHandler(simMap, PlantData())
    }

    @Test
    fun `simple sowing continuation test`() {
        simpleSowContinuationSetup()
        sowingHandler.startPhase(farm, 19, 0)
        assertEquals(PlantType.WHEAT, fieldOne.currentCrop)
        assertEquals(1500000, fieldOne.plant?.harvestEstimate)
        assertEquals(PlantType.WHEAT, fieldTwo.currentCrop)
        assertEquals(1500000, fieldTwo.plant?.harvestEstimate)
        assertEquals(null, fieldThree.currentCrop)
        assertEquals(null, fieldThree.plant)
    }

    private fun simpleSowContinuationNotOnPlan() {
        // field setups
        fieldOne = Tile(1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldOne.farmID = 1
        fieldOne.maxMoisture = 1000
        fieldOne.possiblePlants = listOf(PlantType.WHEAT)
        fieldOne.currentMoisture = 1000
        fieldTwo = Tile(2, Coordinate(2, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldTwo.farmID = 1
        fieldTwo.maxMoisture = 1000
        fieldTwo.possiblePlants = listOf(PlantType.WHEAT)
        fieldTwo.currentMoisture = 1000
        fieldThree = Tile(3, Coordinate(4, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldThree.farmID = 1
        fieldThree.maxMoisture = 1000
        fieldThree.possiblePlants = listOf(PlantType.WHEAT)
        fieldThree.currentMoisture = 1000

        // machine setup
        machine = Machine(
            1, "Tractor", 3, farmstead,
            listOf(ActionType.SOWING), listOf(PlantType.WHEAT), farmstead
        )
        machine.farmID = 1

        // farmstead setup
        farmstead = Tile(10, Coordinate(1, 1), TileType.FARMSTEAD, TileShape.SQUARE)
        farmstead.shed = true

        // sow plan setup
        sowPlan = SowingPlan(1, PlantType.WHEAT, 0, listOf(fieldOne, fieldThree))

        // map setup
        simMap = SimulationMap(
            mutableMapOf(
                Pair(fieldOne.location, fieldOne),
                Pair(fieldTwo.location, fieldTwo),
                Pair(fieldThree.location, fieldThree),
                Pair(farmstead.location, farmstead)
            )
        )

        // farm setup
        farm = Farm(
            1, "Farm", listOf(farmstead), mutableListOf(fieldOne, fieldTwo, fieldThree),
            mutableListOf(), listOf(machine), mutableMapOf(Pair(0, mutableListOf(sowPlan))),
            mutableMapOf()
        )
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()

        // action handler setup
        sowingHandler = SowingHandler(simMap, PlantData())
    }

    @Test
    fun `tile no the sowing plan continuation test`() {
        simpleSowContinuationNotOnPlan()
        sowingHandler.startPhase(farm, 19, 0)
        assertEquals(PlantType.WHEAT, fieldOne.currentCrop)
        assertEquals(1500000, fieldOne.plant?.harvestEstimate)
        assertEquals(null, fieldTwo.currentCrop)
        assertEquals(null, fieldTwo.plant)
        assertEquals(PlantType.WHEAT, fieldThree.currentCrop)
        assertEquals(1500000, fieldThree.plant?.harvestEstimate)
    }

    private fun simpleSowContinuationNotReachable() {
        // field setups
        fieldOne = Tile(1, Coordinate(0, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldOne.farmID = 1
        fieldOne.maxMoisture = 1000
        fieldOne.possiblePlants = listOf(PlantType.WHEAT)
        fieldOne.currentMoisture = 1000
        fieldTwo = Tile(2, Coordinate(2, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldTwo.farmID = 1
        fieldTwo.maxMoisture = 1000
        fieldTwo.possiblePlants = listOf(PlantType.WHEAT)
        fieldTwo.currentMoisture = 1000
        fieldThree = Tile(3, Coordinate(8, 0), TileType.FIELD, TileShape.OCTAGONAL)
        fieldThree.farmID = 1
        fieldThree.maxMoisture = 1000
        fieldThree.possiblePlants = listOf(PlantType.WHEAT)
        fieldThree.currentMoisture = 1000

        // road setups
        roadOne = Tile(4, Coordinate(4, 0), TileType.ROAD, TileShape.OCTAGONAL)
        roadTwo = Tile(5, Coordinate(6, 0), TileType.ROAD, TileShape.OCTAGONAL)

        // machine setup
        machine = Machine(
            1, "Tractor", 3, farmstead,
            listOf(ActionType.SOWING), listOf(PlantType.WHEAT), farmstead
        )
        machine.farmID = 1

        // farmstead setup
        farmstead = Tile(10, Coordinate(1, 1), TileType.FARMSTEAD, TileShape.SQUARE)
        farmstead.shed = true

        // sow plan setup
        sowPlan = SowingPlan(1, PlantType.WHEAT, 0, listOf(fieldOne, fieldTwo, fieldThree))

        // map setup
        simMap = SimulationMap(
            mutableMapOf(
                Pair(fieldOne.location, fieldOne),
                Pair(fieldTwo.location, fieldTwo),
                Pair(fieldThree.location, fieldThree),
                Pair(farmstead.location, farmstead),
                Pair(roadOne.location, roadOne),
                Pair(roadTwo.location, roadTwo)
            )
        )

        // farm setup
        farm = Farm(
            1, "Farm", listOf(farmstead), mutableListOf(fieldOne, fieldTwo, fieldThree),
            mutableListOf(), listOf(machine), mutableMapOf(Pair(0, mutableListOf(sowPlan))),
            mutableMapOf()
        )
        farm.machineHashMap.clear()
        farm.tileHashMap.clear()

        // action handler setup
        sowingHandler = SowingHandler(simMap, PlantData())
    }

    @Test
    fun `sowing continuation unreachable test`() {
        simpleSowContinuationNotReachable()
        sowingHandler.startPhase(farm, 19, 0)
        assertEquals(PlantType.WHEAT, fieldOne.currentCrop)
        assertEquals(1500000, fieldOne.plant?.harvestEstimate)
        assertEquals(PlantType.WHEAT, fieldTwo.currentCrop)
        assertEquals(1500000, fieldTwo.plant?.harvestEstimate)
        assertEquals(null, fieldThree.currentCrop)
        assertEquals(null, fieldThree.plant)
    }
}
