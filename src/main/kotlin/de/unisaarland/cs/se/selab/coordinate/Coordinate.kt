package de.unisaarland.cs.se.selab.coordinate

import de.unisaarland.cs.se.selab.enumerations.Direction
import kotlin.math.abs

/**
 * coordinate class to model 2d-points
 */
data class Coordinate(
    var x: Int,
    var y: Int
) {
    /**
     * helper function for get neighbor
     */
    private fun isSquare(): Boolean {
        return abs(this.x) % 2 == 1 && abs(this.y) % 2 == 1
    }

    /**
     * function that returns a neighboring coordinate based on the direction
     * if the coordinate corresponds to a square tile, only the four defined directions are considered
     */
    fun getNeighbor(d: Direction): Coordinate? {
        if (this.isSquare() && d in setOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
            return null
        }
        return when (d) {
            Direction.NORTH -> Coordinate(this.x, this.y - 2)
            Direction.NORTH_EAST -> Coordinate(this.x + 1, this.y - 1)
            Direction.EAST -> Coordinate(this.x + 2, this.y)
            Direction.SOUTH_EAST -> Coordinate(this.x + 1, this.y + 1)
            Direction.SOUTH -> Coordinate(this.x, this.y + 2)
            Direction.SOUTH_WEST -> Coordinate(this.x - 1, this.y + 1)
            Direction.WEST -> Coordinate(this.x - 2, this.y)
            Direction.NORTH_WEST -> Coordinate(this.x - 1, this.y - 1)
        }
    }

    /**
     * gets the immediate neighbors of the tile;
     * it checks for the corresponding shape; if it is a square,
     * it only gives back the neighbors in four defined directions
     */
    fun getImmediateNeighbors(): Set<Coordinate> {
        val neighbors = mutableSetOf<Coordinate>()
        for (
        direction in setOf(
            Direction.NORTH_EAST,
            Direction.SOUTH_EAST,
            Direction.SOUTH_WEST,
            Direction.NORTH_WEST
        )
        ) {
            getNeighbor(direction)?.let { neighbors.add(it) }
        }
        if (!isSquare()) {
            for (direction in setOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
                getNeighbor(direction)?.let { neighbors.add(it) }
            }
        }
        return neighbors
    }

    /**
     * returns the neighboring tile coordinates in the given radius
     */
    fun getNeighborsInRadius(radius: Int): Set<Coordinate> {
        val neighbors = mutableSetOf<Coordinate>()
        neighbors.addAll(getImmediateNeighbors())
        repeat(radius - 1) {
            val newNeighbors = mutableSetOf<Coordinate>()
            for (neighbor in neighbors) {
                newNeighbors.addAll(neighbor.getImmediateNeighbors())
            }
            neighbors.addAll(newNeighbors)
        }
        neighbors.removeIf { it.x == this.x && it.y == this.y }
        return neighbors
    }
}
