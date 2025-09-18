package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Keeps track of the tiles of the simulation
 */
class Map(
    var tiles: MutableMap<Coordinate, Tile>
) {
    /**
     * get tile from map according to its coordinate
     */
    fun getTileByCoordinate(coordinate: Coordinate): Tile? {
        return tiles.get(coordinate)
    }

    /**
     * get tiile from map according to its id
     */
    fun getTileByID(id: Int): Tile? {
        for (tile in tiles.values) {
            if (tile.id == id) {
                return tile
            }
        }
        return null
    }

    /**
     * get all reachable tiles in radius moves
     */
    fun getTilesByRadius(tile: Tile, radius: Int): List<Tile> {
        TODO()
    }

    /**
     * gets the neighbouring tile according to its airflow direction
     */
    fun getNeighbor(tile: Tile, direction: Direction): Tile? {
        var neighbour: Tile? = null
        if (tile.shape == TileShape.SQUARE) {
            when (direction) {
                Direction.NORTH -> neighbour = null
                Direction.NORTH_EAST -> neighbour = tiles.get(Coordinate(tile.location.x + 1, tile.location.y - 1))
                Direction.EAST -> neighbour = null
                Direction.SOUTH_EAST -> neighbour = tiles.get(Coordinate(tile.location.x + 1, tile.location.y + 1))
                Direction.SOUTH -> neighbour = null
                Direction.SOUTH_WEST -> neighbour = tiles.get(Coordinate(tile.location.x - 1, tile.location.y + 1))
                Direction.WEST -> neighbour = null
                Direction.NORTH_WEST -> neighbour = tiles.get(Coordinate(tile.location.x - 1, tile.location.y - 1))
            }
        } else if (tile.shape == TileShape.OCTAGONAL) {
            when (direction) {
                Direction.NORTH -> neighbour = tiles.get(Coordinate(tile.location.x, tile.location.y - 2))
                Direction.NORTH_EAST -> neighbour = tiles.get(Coordinate(tile.location.x + 1, tile.location.y - 1))
                Direction.EAST -> neighbour = tiles.get(Coordinate(tile.location.x + 2, tile.location.y))
                Direction.SOUTH_EAST -> neighbour = tiles.get(Coordinate(tile.location.x + 1, tile.location.y + 1))
                Direction.SOUTH -> neighbour = tiles.get(Coordinate(tile.location.x, tile.location.y + 2))
                Direction.SOUTH_WEST -> neighbour = tiles.get(Coordinate(tile.location.x - 1, tile.location.y + 1))
                Direction.WEST -> neighbour = tiles.get(Coordinate(tile.location.x - 2, tile.location.y))
                Direction.NORTH_WEST -> neighbour = tiles.get(Coordinate(tile.location.x - 1, tile.location.y - 1))
            }
        }
        return neighbour
    }

    /**
     * check if the destination tile is reachable from the current machine location
     */
    fun isReachable(machine: Machine, destination: Tile): Boolean {
        TODO()
    }

    /**
     * get reachable tiles for harvesting phase
     */
    fun getReachableTiles(machine: Machine, radius: Int, carryingHarvest: Boolean): List<Tile> {
        TODO()
    }

    /**
     * find all reachable sheds by the machine
     */
    fun findTargetShed(machine: Machine, farmSheds: List<Tile>, carryingHarvest: Boolean): Tile? {
        TODO()
    }

    /**
     * return all field and platation tiles
     */
    fun getPlantableTiles(): List<Tile> {
        val plantables: MutableList<Tile> = mutableListOf()
        for (tile in tiles.values) {
            if (tile.category == TileType.FIELD) {
                plantables.add(tile)
            }
            if (tile.category == TileType.PLANTATION) {
                plantables.add(tile)
            }
        }
        return plantables
    }

    /**
     * return all field and plantation tiles from the given list of tiles
     */
    fun filterForPlantable(tiles: List<Tile>): List<Tile> {
        val plantables: MutableList<Tile> = mutableListOf()
        for (tile in tiles) {
            if (tile.category == TileType.FIELD) {
                plantables.add(tile)
            }
            if (tile.category == TileType.PLANTATION) {
                plantables.add(tile)
            }
        }
        return plantables
    }

    /**
     * returns all tiles of the given type from the given list of tiles
     */
    fun filterByType(type: TileType, tiles: List<Tile>): List<Tile> {
        val filtered: MutableList<Tile> = mutableListOf()
        for (tile in tiles) {
            if (tile.category == type) {
                filtered.add(tile)
            }
        }
        return filtered
    }
}
