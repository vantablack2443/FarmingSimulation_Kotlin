package de.unisaarland.cs.se.selab.actionHandlers

// import de.unisaarland.cs.se.selab.enumerations.PlantType
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
// import org.mockito.kotlin.mock
// import org.mockito.kotlin.whenever
//
// class MowingHandlerTest {
//    private lateinit var farm: de.unisaarland.cs.se.selab.farm.Farm
//    private lateinit var tile1: de.unisaarland.cs.se.selab.tile.Tile
//    private lateinit var tile2: de.unisaarland.cs.se.selab.tile.Tile
//    private lateinit var machine: de.unisaarland.cs.se.selab.machine.Machine
//    private lateinit var plant: de.unisaarland.cs.se.selab.plant.Plant
//    private lateinit var actionsNeededSpy: MutableList<de.unisaarland.cs.se.selab.enumerations.ActionType>
//    private lateinit var tile3: de.unisaarland.cs.se.selab.tile.Tile
//
//    companion object {
//        private val MOWING_ACTION_TYPE = de.unisaarland.cs.se.selab.enumerations.ActionType.MOWING
//    }
//
//    @BeforeEach
//    fun setUp() {
//        farm = mock()
//        tile1 = mock()
//        tile2 = mock()
//        machine = mock()
//        plant = mock()
//        tile3 = mock()
//        // Set up actionsNeeded on tiles, not plant
//        val actionsNeededTile1 = org.mockito.kotlin.spy(mutableListOf(MOWING_ACTION_TYPE))
//        val actionsNeededTile2 = org.mockito.kotlin.spy(mutableListOf(MOWING_ACTION_TYPE))
//        whenever(tile1.actionsNeeded).thenReturn(actionsNeededTile1)
//        whenever(tile2.actionsNeeded).thenReturn(actionsNeededTile2)
//        whenever(tile1.plantationDamaged).thenReturn(false) // tile1 is undamaged
//        whenever(tile2.plantationDamaged).thenReturn(true) // tile2 is damaged
//        whenever(tile1.plant).thenReturn(plant)
//        whenever(farm.getPlantation()).thenReturn(listOf(tile1, tile2))
//        whenever(machine.plants).thenReturn(listOf(PlantType.CHERRY, PlantType.APPLE))
//        actionsNeededSpy = actionsNeededTile1 // For verification in test
//    }
//
//    @Test
//    fun getOperableTilesOnlyGetsUndamaged() {
//        val simulationMap = mock<de.unisaarland.cs.se.selab.map.SimulationMap>()
//        val plantData = mock<de.unisaarland.cs.se.selab.plantdata.PlantData>()
//        val handler = MowingHandler(simulationMap, plantData)
//        val result = handler.getOperableTiles(farm)
//        assertEquals(listOf(tile1), result)
//    }
//
//    @Test
//    fun verifyMowingRemovesActionTypeMowing() {
//        val simulationMap = mock<de.unisaarland.cs.se.selab.map.SimulationMap>()
//        val plantData = mock<de.unisaarland.cs.se.selab.plantdata.PlantData>()
//        val handler = MowingHandler(simulationMap, plantData)
//        handler.performAction(machine, tile1, 1)
//        org.mockito.kotlin.verify(actionsNeededSpy).remove(MOWING_ACTION_TYPE)
//    }
// }
