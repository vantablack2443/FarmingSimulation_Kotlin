package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.Direction
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileShape
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.map.SimulationMap
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
class ValidationException : Exception {
    var filePath: String? = null
    constructor(cause: Throwable, filePath: String) : super(cause) {
        this.filePath = filePath
    }

    // constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
    constructor() : super()
}

/**
 * Map parser class
 */
class MapParser(private val simData: SimulationData) {
    private val tileIDMap: MutableMap<Int, Tile> = mutableMapOf()
    private val tileCoordinates: MutableMap<Coordinate, Tile> = mutableMapOf()

    /**
     * parses the given map config file
     */
    fun parse(json: String) {
        try {
            val file = File(json)
            val jsonString = file.readText()
            val tiles = Json.parseToJsonElement(jsonString)
                .jsonObject["tiles"]?.jsonArray ?: throw ValidationException()
            parseCreateTiles(tiles)
            simData.map = SimulationMap(tileCoordinates)
            Logger.logParsing(true, file.name)
        } catch (exception: ValidationException) {
            throw ValidationException(exception, json)
        }
    }

    /**
     * iterates over the tiles array extracted from the json file and parses them one by one
     */
    private fun parseCreateTiles(tiles: JsonArray) {
        for (tile in tiles) {
            val parsedTile = parseTile(tile as JsonObject)
            validateTileShape(parsedTile)
            simData.addTileToMapping(parsedTile)
        }

        for (tile in tileIDMap.values) {
            validateNeighbors(tile)
        }
    }

    private fun getTileID(json: JsonObject): Int {
        return json["id"]?.jsonPrimitive?.int ?: throw ValidationException("Tile ID missing")
    }

    private fun getTileCategory(json: JsonObject): String {
        return json["category"]?.jsonPrimitive?.content ?: throw ValidationException("Missing tile type")
    }

    private fun getTileCoordinates(json: JsonObject): JsonObject {
        return json["coordinates"]?.jsonObject ?: throw ValidationException("Missing tile coordinates")
    }

    /**
     * parses a single tile object
     */
    private fun parseTile(tile: JsonObject): Tile {
        val id = getTileID(tile)
        if (id < 0) throw ValidationException("Tile ID negative")

        val type = getTileCategory(tile)
        if (type !in TileType.entries.toString()) throw ValidationException("invalid tile type")
        val category = TileType.valueOf(type.uppercase())

        val coordinates = getTileCoordinates(tile)
        val x = coordinates["x"]?.jsonPrimitive?.int
        val y = coordinates["y"]?.jsonPrimitive?.int
        if (x == null || y == null) throw ValidationException("Missing x or y coordinate")

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
        if (category == TileType.VILLAGE) {
            if (tile["airflow"] != null) {
                throw ValidationException("Village tile cannot have airflow")
            }
            return Pair(false, Direction.entries.first())
        }

        val airflow = tile["airflow"]?.jsonPrimitive?.boolean
            ?: throw ValidationException("Missing tile airflow")
        if (!airflow) {
            return Pair(false, Direction.entries.first())
        }
        val angle = tile["direction"]?.jsonPrimitive?.content
            ?: throw ValidationException("Missing tile direction while airflow is true")
        val direction = Direction.getDirectionByAngle(angle)
        return Pair(true, direction)
    }

    /**
     * validate the uniqueness of each new ID and coordinate
     */
    private fun validateUniqueAttributes(id: Int, location: Coordinate) {
        if (tileIDMap.containsKey(id) || tileCoordinates.containsKey(location)) {
            throw ValidationException("ID or location is not unique")
        }
    }

    /**
     * parses the farmID if required by the tile type and updates it in the tile object
     */
    private fun parseFarmID(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.FARMSTEAD || tile.category == TileType.PLANTATION ||
            tile.category == TileType.FIELD
        ) {
            val farmID = jsonObject["farm"]?.jsonPrimitive?.int ?: throw ValidationException("Missing farmID")
            if (farmID < 0) throw ValidationException("Negative farm ID")
            tile.farmID = farmID
        }
    }

    /**
     * parses the shed if required by the tile type and updates it in the tile object
     */
    private fun parseFarmstead(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.FARMSTEAD) {
            val shed = jsonObject["shed"]?.jsonPrimitive?.boolean
                ?: throw ValidationException("Missing shed on a farmstead")
            tile.shed = shed
        }
    }

    /**
     * parses the plants of the plantation/field tile and updates it in the tile object
     */
    private fun parsePlantableTiles(jsonObject: JsonObject, tile: Tile) {
        if (tile.category == TileType.PLANTATION) {
            val plantType = jsonObject["plant"]?.jsonPrimitive?.content
                ?: throw ValidationException("Missing plant type")
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
            val plants = jsonObject["possiblePlants"]?.jsonArray
                ?: throw ValidationException("Possible plants missing")
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
        val capacity = jsonObject["capacity"]?.jsonPrimitive?.int
            ?: throw ValidationException("missing capacity")
        if (capacity < 0) throw ValidationException("capacity must be positive")
        tile.maxMoisture = capacity
        tile.currentMoisture = capacity
    }

    /**
     * check the shape of the tile based on its type
     */
    private fun validateTileShape(tile: Tile) {
        when (tile.category) {
            TileType.FARMSTEAD, TileType.MEADOW -> {
                if (tile.shape != TileShape.SQUARE) {
                    throw ValidationException("Mismatched shape for tile ${tile.id}")
                }
            }
            TileType.FIELD, TileType.PLANTATION -> {
                if (tile.shape != TileShape.OCTAGONAL) {
                    throw ValidationException("Mismatched shape for tile ${tile.id}")
                }
            }
            else -> { return }
        }
    }

    /**
     * validate the correctness of plant types on plantable fields
     */
    private fun validatePlantType(tileType: TileType, plant: String) {
        when (tileType) {
            TileType.PLANTATION -> {
                if (!PlantType.isPlantationPlant(plant)) {
                    throw ValidationException("mismatched plant type")
                }
            }
            TileType.FIELD -> {
                if (!PlantType.isFieldPlant(plant)) {
                    throw ValidationException("mismatched plant type")
                }
            }
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
                throw ValidationException("forest can not adjoin village")
            }
        }
    }

    /**
     * helper function to validate adjoining tiles of a village
     */
    private fun validateVillageNeighbors(neighbors: List<Tile>) {
        for (element in neighbors) {
            if (element.category == TileType.FOREST) {
                throw ValidationException("village can not adjoin forest")
            }
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
