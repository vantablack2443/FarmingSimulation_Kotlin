package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.tile.Tile

class Map (
    var tiles: MutableMap<Coordinate, Tile>
) {
    fun getTileByCoordinate(coordinate: Coordinate): Tile? {
        TODO()
    }
    fun getTileByID(id: Int): Tile? {
        TODO()
    }
    fun getTilesByRadius(tile:Tile, radius: Int): List<Tile> {
        TODO()
    }
    fun getNeighbor(tile:Tile, direction: Direction): Tile? {
        TODO()
    }
    fun isReachable(machine: Machine, destination: Tile): Boolean {
        TODO()
    }
    fun getReachableTiles(machine: Machine, radius: Int, carryingHarvest: Boolean): List<Tile> {
        TODO()
    }
    fun findTargetShed(machine: Machine, farmSheds: List<Tile>, carryingHarvest: Boolean): Tile? {
        TODO()
    }
    fun getPlantableTiles(): List<Tile> {
        TODO()
    }
    fun filterForPlantable(tiles: List<Tile>): List<Tile> {
        TODO()
    }
    fun filterByType(type: TileType, tiles: List<Tile>): List<Tile> {
        TODO()
    }
}