package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.tile.Tile
// import kotlin.math.abs

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
     */
    fun getTilesByRadius(tile: Tile, radius: Int): List<Tile> {
        if (radius == 0) {
            return listOf(tile)
        }
        val cord = tile.location

//        val tiles: MutableList<Tile> = mutableListOf()
//        for (i in (cord.x - (radius * 2))..(cord.x + (radius * 2))) {
//            for (j in (cord.y - (radius * 2))..(cord.y + (radius * 2))) {
//                if (abs(i - cord.x) + abs(j - cord.y) > radius * 2 || (i == cord.x && j == cord.y)) {
//                    continue
//                }
//                val newTile = getTileByCoordinate(Coordinate(i, j))
//                // Here it will also try to get invalid tiles such as (9,4) but getTileByCoordinate will return null
//                if (newTile != null) {
//                    tiles.add(newTile)
//                }
//            }
//        }
//
        val neighbors = cord.getNeighborsInRadius(radius)
        val tiles = mutableListOf<Tile>()
        for (n in neighbors) {
            val newTile = getTileByCoordinate(n)
            if (newTile != null) {
                tiles.add(newTile)
            }
        }
        return tiles.sortedBy { it.id }
    }

    /**
     * gets the neighbouring tile according to its airflow direction
     */
    fun getNeighbor(tile: Tile, direction: Direction?): Tile? {
        if (direction == null) return null
        val neighbor = tile.location.getNeighbor(direction)
        if (neighbor != null) { return tiles[neighbor] }
        return null
        /*
        var neighbour: Tile? = null
        if (direction == null) return null
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
        return neighbour*/
    }

    /**
     * check if the destination tile is reachable from the current machine location
     */
    fun isReachable(machine: Machine, destination: Tile): Boolean {
        val carryingHarvest: Boolean = machine.currentHarvest != null
        val reachable = getReachableTiles(machine, -1, carryingHarvest)
        return reachable.any { it.id == destination.id }
    }

    /**
     * Actual BFS done here. returns all the reachable tiles according to the radius.
     */
    fun getReachableTiles(machine: Machine, radius: Int, carryingHarvest: Boolean): List<Tile> {
        val start: Tile = machine.currentTile
        val tileList: List<Tile> = getAccessibleTiles(machine, radius, carryingHarvest)
        val visited: MutableSet<Tile> = mutableSetOf()
        val queue: ArrayDeque<Tile> = ArrayDeque()

        queue.add(start)
        visited.add(start)

        while (queue.isNotEmpty()) {
            val currTile: Tile = queue.removeFirst()

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
        return visited.sortedBy { it.id }
    }

    /**
     * get accessible tiles of the map for a farm
     * (Helper Function)
     */
    private fun getAccessibleTiles(machine: Machine, radius: Int, carryingHarvest: Boolean): List<Tile> {
        val reach = if (radius == -1) {
            tiles.values.toMutableList()
            // whole map is considered when radius is -1
        } else {
            // Assumes getTilesByRadius returns a sorted List
            getTilesByRadius(machine.currentTile, radius).toMutableList()
            // only tiles in given radius considered otherwise
        }
        val removedTiles = mutableListOf<Tile>()
        for (tile in reach) {
            if (tile.category == TileType.FOREST) {
                removedTiles += tile
                // reach -= tile
                // remove tile from reach if it belongs to other farm or is FOREST
            }
            if (tile.category == TileType.FIELD ||
                tile.category == TileType.PLANTATION ||
                tile.category == TileType.FARMSTEAD
            ) {
                if (tile.farmID != machine.farmID) {
                    removedTiles += tile
                }
            }

            if (carryingHarvest && tile.category == TileType.VILLAGE) {
                removedTiles += tile
                // reach -= tile
                // remove VILLAGE tiles if machine is loaded
            }
        }
        reach.removeAll(removedTiles)
        return reach
    }

    /**
     * Gets next possible tile for continuing action
     * just the lowest id reachable tile for radius 2
     * @param actionTilesFarm - The tiles that the farm can work on filtered out by the respective handler based on the
     * action required in the actionsNeeded lists
     */
    fun tileForContinueAction(machine: Machine, actionTilesFarm: List<Tile>, farm: Farm): Tile? {
        val carryingHarvestBool: Boolean = machine.currentHarvest != null
        val allReachableInRadius = getReachableTiles(machine, 2, carryingHarvestBool)
        val possibleTiles = actionTilesFarm.intersect(allReachableInRadius.toSet()).sortedBy { it.id }
        val possibleTilesNotHashed = possibleTiles.filter { it.id !in farm.tileHashMap }
        val possibleTilesForMachine = possibleTilesNotHashed.filter { machine.plants.contains(it.currentCrop) }
        if (possibleTilesForMachine.isNotEmpty()) return possibleTilesForMachine.minByOrNull { it.id }
        return null
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
        if (!reachableSheds.isEmpty()) return reachableSheds.minByOrNull { it.id }
        return null
    }

    /**
     * return all field and plantation tiles sorted by ID
     */
    fun getPlantableTiles(): List<Tile> {
        return tiles.values.toList()
            .filter { it.category in setOf(TileType.FIELD, TileType.PLANTATION) }
            .sortedBy { it.id }
    }

    /**
     * return all field and plantation tiles (sorted by ID) from the given list of tiles
     */
    fun filterForPlantable(tiles: List<Tile>): List<Tile> {
        return tiles.filter { it.category in setOf(TileType.FIELD, TileType.PLANTATION) }
            .sortedBy { it.id }
    }

    /**
     * returns all tiles of the given type (sorted by ID) from the given list of tiles
     */
    fun filterByType(type: TileType, tiles: List<Tile>): List<Tile> {
        return tiles.filter { it.category == type }.sortedBy { it.id }
    }

//    /**
//     * For move cloud optimization
//     */
//    fun nextTileNullButAirflow(tile: Tile): Boolean {
//        val nextTile = getNeighbor(tile, tile.direction)
//        return nextTile == null
//    }
}
