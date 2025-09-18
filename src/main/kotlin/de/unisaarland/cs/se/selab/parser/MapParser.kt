package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.plant.Almond
import de.unisaarland.cs.se.selab.plant.Apple
import de.unisaarland.cs.se.selab.plant.Cherry
import de.unisaarland.cs.se.selab.plant.Grape
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.simulation.SimulationData
import de.unisaarland.cs.se.selab.tile.Tile
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

/**
 * custom exception
 */
class ValidationException : Exception("Validation Exception, missing or invalid fields")

/**
 * Map parser class
 */
class MapParser(private val simData: SimulationData) {
    private lateinit var tileIDMap: MutableMap<Int, Tile>
    private lateinit var tileCoordinates: MutableMap<Coordinate, Tile>

    /**
     * parses the given map config file
     */
    fun parse(json: String) {
        val file = File(json)
        val jsonString = file.readText()
        val tiles = Json.parseToJsonElement(jsonString).jsonObject["tiles"]?.jsonArray ?: throw ValidationException()
        parseCreateTiles(tiles)
        simData.tiles = tileIDMap
        simData.map = Map(tileCoordinates)
        TODO("add logger")
    }

    /**
     * iterates over the tiles array extracted from the json file and parses them one by one
     */
    private fun parseCreateTiles(tiles: JsonArray) {
        for (tile in tiles) {
            val parsedTile = parseTile(tile as JsonObject)
            validateTileShape(parsedTile)
        }

        for (tile in tileIDMap.values) {
            validateNeighbors(tile)
        }
    }

    /**
     * parses a single tile object
     */
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
        validateUniqueAttributes(id, location)
        tileIDMap[id] = parsedTile
        tileCoordinates[location] = parsedTile
        return parsedTile
    }

    /**
     * parse the airflow and direction for a tile; throw exception if type village
     */
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
     * validate the uniqueness of each new ID and coordinate
     */
    private fun validateUniqueAttributes(id: Int, location: Coordinate) {
        if (tileIDMap.containsKey(id) || tileCoordinates.containsKey(location)) throw ValidationException()
    }

    /**
     * parses the farmID if required by the tile type and updates it in the tile object
     */
    private fun parseFarmID(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.FARMSTEAD || tile.category == TileType.PLANTATION ||
            tile.category == TileType.FIELD
        ) {
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
            validatePlantType(TileType.PLANTATION, plantType)
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
                validatePlantType(TileType.FIELD, element)
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

    /**
     * check the shape of the tile based on its type
     */
    private fun validateTileShape(tile: Tile) {
        when (tile.category) {
            TileType.FARMSTEAD, TileType.MEADOW -> {
                if (tile.shape != TileShape.SQUARE) throw ValidationException()
            }
            TileType.FIELD, TileType.PLANTATION -> {
                if (tile.shape != TileShape.OCTAGONAL) throw ValidationException()
            }
            else -> { return }
        }
    }

    /**
     * validate the correctness of plant types on plantable fields
     */
    private fun validatePlantType(tileType: TileType, plant: String) {
        when (tileType) {
            TileType.PLANTATION -> { if (!PlantType.isPlantationPlant(plant)) throw ValidationException() }
            TileType.FIELD -> { if (!PlantType.isFieldPlant(plant)) throw ValidationException() }
            else -> { return }
        }
    }

    /**
     * validate tha=e adjoining tiles based on the tile category
     */
    private fun validateNeighbors(tile: Tile) {
        val neighboringTiles = mutableListOf<Tile>()
        for (direction in Direction.entries) {
            val adjacent = tile.location.getNeighbor(direction)
            val neighborTile = tileCoordinates.getOrDefault(adjacent, null)
            if (neighborTile != null) {
                neighboringTiles.add(neighborTile)
            }
        }
        when (tile.category) {
            TileType.FARMSTEAD, TileType.MEADOW -> validateFarmsteadOrMeadowNeighbors(neighboringTiles)
            TileType.FOREST -> validateForestNeighbors(neighboringTiles)
            TileType.VILLAGE -> validateVillageNeighbors(neighboringTiles)
            else -> return
        }
    }

    /**
     * helper function to validate adjoining tiles of a farmstead or a meadow
     */
    private fun validateFarmsteadOrMeadowNeighbors(neighbors: List<Tile>) {
        for (element in neighbors) {
            if (element.category == TileType.FARMSTEAD || element.category == TileType.MEADOW) {
                throw ValidationException()
            }
        }
    }

    /**
     * helper function to validate adjoining tiles of a forest
     */
    private fun validateForestNeighbors(neighbors: List<Tile>) {
        for (element in neighbors) {
            if (element.category == TileType.VILLAGE) {
                throw ValidationException()
            }
        }
    }

    /**
     * helper function to validate adjoining tiles of a vilage
     */
    private fun validateVillageNeighbors(neighbors: List<Tile>) {
        for (element in neighbors) {
            if (element.category == TileType.FOREST) throw ValidationException()
        }
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
