package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.plant.Almond
import de.unisaarland.cs.se.selab.plant.Apple
import de.unisaarland.cs.se.selab.plant.Cherry
import de.unisaarland.cs.se.selab.plant.Grape
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.tile.Tile
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray

/**
 * custom exception
 */
class ValidationException : Exception("Validation Exception, missing or invalid fields")


class MapParser (private val simData: SimulationData) {
    private lateinit var simulationData: SimulationData
    private lateinit var tileIDMap: MutableMap<Int, Tile>
    private lateinit var tileCoordinates: MutableMap<Coordinate, Tile>


    fun parse(json: String): Unit {
        // TODO
    }


    private fun parseCreateTiles(tiles: JsonArray): Unit {
        // TODO
    }


    private fun parseTile(tile: JsonObject): Tile {
        val id = tile["id"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (id < 0) throw ValidationException()
        val type = tile["category"] ?.jsonPrimitive?.content ?: throw ValidationException()
        if (type !in TileType.entries.toString()) throw ValidationException()
        val category = TileType.valueOf(type.uppercase())
        val coordinates = tile["location"] ?.jsonPrimitive?.content ?: throw ValidationException()
        val (x, y) = coordinates.removeSurrounding("(", ")").split(',').map { it.toInt() }
        val location = Coordinate(x, y)
        val shape = getShapeByCoordinate(location)
        val parsedTile = Tile(id, location, category, shape)


        val (airflow, direction) = parseAirflow(tile, category)
        parsedTile.airflow = airflow
        parsedTile.direction = direction
        parseFarmID(tile, parsedTile)
        parseFarmstead(tile, parsedTile)
        parsePlantableTiles(tile, parsedTile)
        return parsedTile
    }


    private fun parseAirflow(tile: JsonObject, category: TileType): Pair<Boolean, Direction> {
        if (category != TileType.VILLAGE) {
            val airflow = tile["airflow"]?.jsonPrimitive?.boolean ?: throw ValidationException()
            val angle = tile["direction"]?.jsonPrimitive?.int ?: throw ValidationException()
            val direction = Direction.getDirectionByAngle(angle)
            return Pair(airflow, direction)
        }
        throw ValidationException()
    }




    /**
     * parses the farmID if required by the tile type and updates it in the tile object
     */
    private fun parseFarmID(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.FARMSTEAD
            || tile.category == TileType.PLANTATION
            || tile.category == TileType.FIELD) {
            val farmID = jsonObject["farm"]?.jsonPrimitive?.int ?: throw ValidationException()
            if (farmID < 0) throw ValidationException()
            tile.farmID = farmID
        }
    }


    /**
     * parses the shed if required by the tile type and updates it in the tile object
     */
    private fun parseFarmstead(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.FARMSTEAD) {
            val shed = jsonObject["shed"]?.jsonPrimitive?.boolean ?: throw ValidationException()
            tile.shed = shed
        }
    }


    /**
     * parses the plants of the plantation/field tile and updates it in the tile object
     */
    private fun parsePlantableTiles(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.PLANTATION) {
            val plantType = jsonObject["plant"]?.jsonPrimitive?.content ?: throw ValidationException()
            if (!PlantType.isPlantationPlant(plantType)) throw ValidationException()
            var plant: Plant? = null
            when (plantType.uppercase()) {
                "APPLE" -> plant = Apple()
                "ALMOND" -> plant = Almond()
                "CHERRY" -> plant = Cherry()
                "GRAPE" -> plant = Grape()
            }
            tile.plant = plant
            tile.currentCrop = PlantType.valueOf(plantType.uppercase())
        }


        if (tile.category == TileType.FIELD) {
            val plants = jsonObject["possiblePlants"]?.jsonArray ?: throw ValidationException()
            val fieldPlants = plants.map { it.jsonPrimitive.content }
            for (element in fieldPlants) {
                if (!PlantType.isFieldPlant(element)) throw ValidationException()
            }
            tile.possiblePlants = fieldPlants.map { PlantType.valueOf(it.uppercase()) }
            parseCapacity(jsonObject, tile)
        }


    }


    /**
     * parses the moisture capacity (tile type check should be done before calling the function)
     * and updates it in the tile object
     */
    private fun parseCapacity(jsonObject: JsonObject, tile: Tile) {
        val capacity = jsonObject["capacity"]?.jsonPrimitive?.int ?: throw ValidationException()
        if (capacity < 0) throw ValidationException()
        tile.maxMoisture = capacity
        tile.currentMoisture = capacity
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


    /**
     * returns the shape of the tile based on its coordinate
     */
    private fun getShapeByCoordinate(coordinate: Coordinate): TileShape {
        val x = coordinate.x
        val y = coordinate.y
        if (x % 2 == 0 && y % 2 == 0) return TileShape.OCTAGONAL
        if (x % 2 == 1 && y % 2 == 1) return TileShape.SQUARE
        throw ValidationException()
    }
}
