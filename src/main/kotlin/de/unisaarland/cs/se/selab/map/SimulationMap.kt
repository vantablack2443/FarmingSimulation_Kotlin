package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.tile.Tile
import kotlin.math.abs

/**
 * Map Class
 */
class SimulationMap(
    var tiles: MutableMap<Coordinate, Tile>
) {
    /**
     * get tile from map by its coordinate
     */
    fun getTileByCoordinate(coordinate: Coordinate): Tile? {
        return tiles[coordinate]
    }

    /**
     * get tile from map by its id
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
     * gets all tiles in the specified radius of given tile
     * (not optimized yet, imo doesn't need optimization)
     */
    fun getTilesByRadius(tile: Tile, radius: Int): List<Tile> {
        val cord = tile.location
        val tiles: MutableList<Tile> = mutableListOf()
        for (i in (cord.x - (radius * 2))..(cord.x + (radius * 2))) {
            for (j in (cord.y - (radius * 2))..(cord.y + (radius * 2))) {
                if (abs(i - cord.x) + abs(j - cord.y) > radius * 2) {
                    continue
                }
                val tile = getTileByCoordinate(Coordinate(i, j))
                // Here it will also try to get invalid tiles such as (9,4) but getTileByCoordinate will return null
                if (tile != null) {
                    tiles.add(tile)
                }
            }
        }
        return tiles
    }

    /**
     * gets the neighbouring tile according to its airflow direction
     */
    fun getNeighbor(tile: Tile, direction: Direction): Tile? {
        var neighbour: Tile? = null
        if (tile.shape == TileShape.SQUARE) {
            neighbour = when (direction) {
                Direction.NORTH -> null
                Direction.NORTH_EAST -> tiles[Coordinate(tile.location.x + 1, tile.location.y - 1)]
                Direction.EAST -> null
                Direction.SOUTH_EAST -> tiles[Coordinate(tile.location.x + 1, tile.location.y + 1)]
                Direction.SOUTH -> null
                Direction.SOUTH_WEST -> tiles[Coordinate(tile.location.x - 1, tile.location.y + 1)]
                Direction.WEST -> null
                Direction.NORTH_WEST -> tiles[Coordinate(tile.location.x - 1, tile.location.y - 1)]
            }
        } else if (tile.shape == TileShape.OCTAGONAL) {
            neighbour = when (direction) {
                Direction.NORTH -> tiles[Coordinate(tile.location.x, tile.location.y - 2)]
                Direction.NORTH_EAST -> tiles[Coordinate(tile.location.x + 1, tile.location.y - 1)]
                Direction.EAST -> tiles[Coordinate(tile.location.x + 2, tile.location.y)]
                Direction.SOUTH_EAST -> tiles[Coordinate(tile.location.x + 1, tile.location.y + 1)]
                Direction.SOUTH -> tiles[Coordinate(tile.location.x, tile.location.y + 2)]
                Direction.SOUTH_WEST -> tiles[Coordinate(tile.location.x - 1, tile.location.y + 1)]
                Direction.WEST -> tiles[Coordinate(tile.location.x - 2, tile.location.y)]
                Direction.NORTH_WEST -> tiles[Coordinate(tile.location.x - 1, tile.location.y - 1)]
            }
        }
        return neighbour
    }

    /**
     * check if the destination tile is reachable from the current machine location
     */
    fun isReachable(machine: Machine, destination: Tile): Boolean {
        val start: Tile = machine.currentTile
        val carryingHarvest: Boolean = machine.currentHarvest != null
        val tileList: List<Tile> = getReachableTiles(machine, -1, carryingHarvest)
        val visited: MutableSet<Tile> = mutableSetOf()
        val queue: ArrayDeque<Tile> = ArrayDeque()

        queue.add(start)
        visited.add(start)

        while (queue.isNotEmpty()) {
            val currTile: Tile = queue.removeFirst()
            // use id if location doesnt work
            if (currTile.location == destination.location) return true

            val neighbors: MutableList<Tile> = mutableListOf()
            for (direction in Direction.entries) {
                val temp: Tile? = getNeighbor(currTile, direction)
                if (temp != null) {
                    neighbors += temp
                }
            }

            for (neighbor in neighbors) {
                if (neighbor in tileList && neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor)
                }
            }
        }
        return false
    }

    /**
     * get reachable tiles for harvesting phase
     */
    fun getReachableTiles(machine: Machine, radius: Int, carryingHarvest: Boolean): List<Tile> {
        val reach: MutableList<Tile> = mutableListOf()
        reach += if (radius == -1) {
            tiles.values.toList()
            // whole map is considered when radius is -1
        } else {
            getTilesByRadius(machine.currentTile, radius)
            // only tiles in given radius considered otherwise
        }
        for (tile in reach) {
            if (tile.farmID != machine.farmID || tile.category == TileType.FOREST) {
                reach -= tile
                // remove tile from reach if it belongs to other farm or is FOREST
            }
            if (carryingHarvest && tile.category == TileType.VILLAGE) {
                reach -= tile
                // remove VILLAGE tiles if machine is loaded
            }
        }
        return reach
    }

    /**
     * find the shed to which the machine returns
     * first priority: Home shed then priority by ID
     * returns null if no shed on the farm is reachable
     */
    fun findTargetShed(machine: Machine, farmSheds: List<Tile>, carryingHarvest: Boolean): Tile? {
        val reach = getReachableTiles(machine, -1, carryingHarvest)
        val reachableSheds = farmSheds.intersect(reach.toSet())
        if (machine.homeShed in reachableSheds) return machine.homeShed
        // assumes farm sheds are ordered by id
        if (!reachableSheds.isEmpty()) return reachableSheds.first()
        return null
    }

    /**
     * return all field and plantation tiles
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
