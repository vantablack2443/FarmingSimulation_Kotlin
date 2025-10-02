package de.unisaarland.cs.se.selab.actionHandlers
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MowingHandlerTest {
    private lateinit var farm: Farm
    private lateinit var tile1: Tile
    private lateinit var tile2: Tile
    private lateinit var machine: Machine
    private lateinit var plant: Plant

    companion object {
        private val MOWING_ACTION_TYPE = ActionType.MOWING
    }

    @BeforeEach
    fun setUp() {
        farm = mock()
        tile1 = mock()
        tile2 = mock()
        machine = mock()
        plant = mock()

        // real mutable lists for actions
        val actionsNeededTile1 = mutableListOf(MOWING_ACTION_TYPE)
        val actionsNeededTile2 = mutableListOf(MOWING_ACTION_TYPE)

        whenever(tile1.actionsNeeded).thenReturn(actionsNeededTile1)
        whenever(tile2.actionsNeeded).thenReturn(actionsNeededTile2)

        whenever(tile1.plantationDamaged).thenReturn(false) // tile1 is undamaged
        whenever(tile2.plantationDamaged).thenReturn(true) // tile2 is damaged

        whenever(tile1.plant).thenReturn(plant)

        whenever(farm.getPlantation()).thenReturn(listOf(tile1, tile2))

        whenever(machine.plants).thenReturn(listOf(PlantType.CHERRY, PlantType.APPLE))
    }

    @Test
    fun getOperableTilesOnlyGetsUndamaged() {
        val simulationMap = mock<SimulationMap>()
        val plantData = mock<PlantData>()
        val handler = MowingHandler(simulationMap, plantData)

        val result = handler.getOperableTiles(farm)

        // Only tile1 should be returned since tile2 is damaged
        assertEquals(listOf(tile1), result)
    }

    @Test
    fun verifyMowingRemovesActionTypeMowing() {
        val simulationMap = mock<SimulationMap>()
        val plantData = mock<PlantData>()
        val handler = MowingHandler(simulationMap, plantData)

        handler.performAction(machine, tile1, 1)

        // After mowing, ActionType.MOWING should be removed from tile1.actionsNeeded
        assertEquals(false, tile1.actionsNeeded.contains(MOWING_ACTION_TYPE))
    }
}
