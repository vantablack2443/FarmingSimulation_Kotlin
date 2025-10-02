package de.unisaarland.cs.se.selab.machine

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MachineTest {
    private lateinit var farmstead: Tile
    private lateinit var actionType: ActionType
    private lateinit var machine: Machine
    private lateinit var plantType: PlantType

    @BeforeEach
    fun setUp() {
        createMap()

        actionType = ActionType.IRRIGATING
        plantType = PlantType.CHERRY

        val machine = Machine(
            0,
            "LORRY",
            7,
            farmstead,
            listOf(actionType),
            listOf(plantType),
            farmstead
        )
        this.machine = machine
    }
    private fun createMap() {
        farmstead = Tile(
            0,
            Coordinate(-1, -1),
            TileType.FARMSTEAD,
            TileShape.SQUARE
        )
        val fieldTile = Tile(
            1,
            Coordinate(0, 0),
            TileType.FIELD,
            TileShape.OCTAGONAL
        )
        val villageTile = Tile(
            2,
            Coordinate(1, 1),
            TileType.VILLAGE,
            TileShape.SQUARE
        )
        val plantationTile = Tile(
            3,
            Coordinate(0, 2),
            TileType.PLANTATION,
            TileShape.OCTAGONAL,
        )

        val tiles = listOf(
            farmstead,
            fieldTile,
            villageTile,
            plantationTile
        )
        val mapping = mutableMapOf<Coordinate, Tile>()
        tiles.forEach { mapping[it.location] = it }
        tiles.forEach { it.airflow = false }
    }

    @Test
    fun testUpdateTime() {
        assertTrue { machine.canPerform() }
        machine.updateElapsedTime()
        assertEquals(7, machine.elapsedTime)

        var bool = machine.canPerform()
        assertTrue(bool)

        machine.updateElapsedTime()
        bool = machine.canPerform()
        assertFalse(bool)

        assertEquals(14, machine.elapsedTime)

        machine.updateElapsedTime()
        bool = machine.canPerform()
        assertFalse(bool)

        machine.resetElapsedTime()
        assertEquals(0, machine.elapsedTime)
        bool = machine.canPerform()
        assertTrue(bool)
    }
}
