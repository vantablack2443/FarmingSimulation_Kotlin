package de.unisaarland.cs.se.selab.coordinate

import de.unisaarland.cs.se.selab.enumerations.Direction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CoordinateTest {
    @Test
    fun getNeighbor() {
        val c1 = Coordinate(-1, -1)
        val c2 = Coordinate(0, 0)
        val c3 = Coordinate(1, 1)

        val c1NE = c1.getNeighbor(Direction.NORTH_EAST)
        val c1SE = c1.getNeighbor(Direction.SOUTH_EAST)
        val c1SW = c1.getNeighbor(Direction.SOUTH_WEST)
        val c1NW = c1.getNeighbor(Direction.NORTH_WEST)

        val c3NE = c3.getNeighbor(Direction.NORTH_EAST)
        val c3SE = c3.getNeighbor(Direction.SOUTH_EAST)
        val c3SW = c3.getNeighbor(Direction.SOUTH_WEST)
        val c3NW = c3.getNeighbor(Direction.NORTH_WEST)

        val c2North = c2.getNeighbor(Direction.NORTH)
        val c2NorthEast = c2.getNeighbor(Direction.NORTH_EAST)
        val c2East = c2.getNeighbor(Direction.EAST)
        val c2SouthEast = c2.getNeighbor(Direction.SOUTH_EAST)
        val c2South = c2.getNeighbor(Direction.SOUTH)
        val c2SouthWest = c2.getNeighbor(Direction.SOUTH_WEST)
        val c2West = c2.getNeighbor(Direction.WEST)
        val c2NorthWest = c2.getNeighbor(Direction.NORTH_WEST)

        assertEquals(0, c1NE?.x ?: 1)
        assertEquals(0, c1SE?.x ?: 1)
        assertEquals(-2, c1SW?.x ?: -3)
        assertEquals(-2, c1NW?.x ?: -3)

        assertEquals(2, c3NE?.x ?: 3)
        assertEquals(2, c3SE?.x ?: 3)
        assertEquals(0, c3SW?.x ?: -1)
        assertEquals(0, c3NW?.x ?: -1)

        assertEquals(0, c2North?.x ?: 0)
        assertEquals(1, c2NorthEast?.x ?: 1)
        assertEquals(2, c2East?.x ?: 0)
        assertEquals(1, c2SouthEast?.x ?: 1)
        assertEquals(0, c2South?.x ?: 0)
        assertEquals(-1, c2SouthWest?.x ?: -1)
        assertEquals(-2, c2West?.x ?: 0)
        assertEquals(-1, c2NorthWest?.x ?: -1)

    }

    @Test
    fun getImmediateNeighbors() {
        // TODO
    }

    @Test
    fun getNeighborsInRadius() {
        // TODO
    }
}
