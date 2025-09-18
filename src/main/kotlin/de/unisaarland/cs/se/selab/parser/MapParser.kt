package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.tile.Tile
import org.json.JSONArray
import org.json.JSONObject

class MapParser (private val simData: SimulationData) {
    private lateinit var simulationData: SimulationData
    private lateinit var tileIDMap: MutableMap<Int, Tile>
    private lateinit var tileCoordinates: MutableMap<Coordinate, Tile>

    fun parse(json: String): Unit {
        // TODO
    }

    private fun parseCreateTiles(tiles: JSONArray): Unit {
        // TODO
    }

    private fun parseTile(tile: JSONObject): Tile {
        // TODO
    }

    private fun validateUniqueAttributes(id: Int, c: Coordinate): Boolean {
        // TODO
    }

    private fun validateTileShape(type: TileType): Boolean {
        // TODO
    }

    private fun validatePlantType(tileType: TileType, plant: PlantType): Boolean {
        // TODO
    }

    private fun validateAdjacentTiles(): Boolean {
        // TODO
    }

    private fun validateNeighbors(t: Tile): Boolean {
        // TODO
    }
}