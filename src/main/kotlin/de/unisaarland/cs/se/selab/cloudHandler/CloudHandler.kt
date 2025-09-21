package de.unisaarland.cs.se.selab.cloudHandler

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

const val MAX_SUNLIGHT_REDUCTION = 50
const val MIN_SUNLIGHT_REDUCTION = 30
const val MAX_TRAVERSIBLE_TILES = 10

/**
 * handler class for cloud movement
 */
class CloudHandler(val simulationMap: SimulationMap) {
    // Add code that will update maxCloudInt
    private lateinit var coordinateToCloud: MutableMap<Coordinate, Cloud>
    private lateinit var cloudsList: MutableList<Cloud>
    private var maxCloudID = 0
    private var map = simulationMap

    // This set is used to track clouds that have merged or dissipated. Will be used to remove clouds from the map
    private var removedClouds: MutableSet<Int> = mutableSetOf()

    // Used to hold the merged clouds temporarily while movement is in progress
    private var mergedList: MutableList<Cloud> = mutableListOf()

    /**
     * setter for max cloud ID
     */
    fun setMaxCloudID(maxCloudID: Int) {
        this.maxCloudID = maxCloudID
    }

    /**
     * setter for coordinate to cloud mapping
     */
    fun setCloudMapping(mapping: MutableMap<Coordinate, Cloud>) {
        coordinateToCloud = mapping
    }

    /**
     * Moves clouds according to the rules specified in the task description.
     * Called from the simulation once per tick
     */
    fun moveClouds() {
        // Move clouds in clouds
        for (cloud in cloudsList) {
            if (cloud.isStuck) continue
            moveCloud(cloud)
        }
        // Move newly merged clouds
        for (cloud in mergedList) {
            moveCloud(cloud)
        }
        // Append the merged clouds to the main cloud list
        cloudsList.addAll(mergedList)
        mergedList.clear()

        // Remove clouds that have dissipated or merged
        cloudsList.removeIf { it.id in removedClouds }
    }

    private fun checkRain(tile: Tile): Boolean {
        if (tile.currentMoisture == null || tile.currentMoisture!! < tile.maxMoisture!!) {
            return true
        }
        return false
    }

    private fun rain(tile: Tile, cloud: Cloud) {
        // If currentMoisture is null, rain down the entire amount
        if (tile.currentMoisture == null) {
            val rainedAmount = cloud.amount
            cloud.amount = 0
            // ADD LOG
        } else {
            val requiredMoisture = tile.maxMoisture!! - tile.currentMoisture!!
            // If the cloud has less or equal amount than required, rain down the entire amount
            if (requiredMoisture >= cloud.amount) {
                val rainedAmount = cloud.amount
                cloud.amount = 0
                // Tile moisture becomes current + rained amount
                tile.currentMoisture = tile.currentMoisture!! + rainedAmount
                // ADD LOG
            } else {
                // Cloud has more than required, rain down only the required amount
                val rainedAmount = requiredMoisture
                cloud.amount -= requiredMoisture
                // Tile moisture becomes maxMoisture
                tile.currentMoisture = tile.maxMoisture
                // ADD LOG
            }
        }
    }

    private fun tryRainAndDissipate(tile: Tile, cloud: Cloud): Boolean {
        if (checkRain(tile)) {
            rain(tile, cloud)
            if (checkRainDissipate(cloud)) {
                dissipate(cloud)
                return true
            }
        }
        return false
    }

    private fun moveCloud(cloud: Cloud) {
        // Clouds are always on a valid tile
        val tile: Tile = map.getTileByCoordinate(cloud.location) as Tile

        // Village Dissipation?
        if (tile.category == TileType.VILLAGE) {
            dissipate(cloud)
            return
        }

        // Rain if possible, then dissipate if amount is 0
        if (tryRainAndDissipate(tile, cloud)) {
            return
        }

        // Move the cloud in the airflow direction, or mark it as stuck if it can't move
        val direction = tile.direction

        var currTile = tile
        var nextTile = map.getNeighbor(tile, direction)
        // If the neighbor tile is null, mark the cloud as stuck and check for dissipation(duration Check)
        if (nextTile == null) {
            cloud.isStuck = true
            if (cloud.duration != -1) {
                cloud.duration -= 1
            }
            if (checkDurationDissipate(cloud)) {
                dissipate(cloud)
            }
            return
        }

        if (cloud.isStuck) return

        var moves = 0
        var mergedOrDissipated = false
        while (nextTile != null || moves < cloud.maxTraversibleTiles) {
            // Check for village dissipation
            if (checkVillageDissipate(nextTile!!)) {
                dissipate(cloud)
                mergedOrDissipated = true
                break
            }

            // Check for merge and merge if necessary
            if (checkMerge(nextTile)) {
                val newCloud = merge(cloud)
                // Check rain for the new cloud
                tryRainAndDissipate(nextTile, newCloud)
                // Dissipate will add the new cloud to removed clouds
                // Will be removed after appending to the two lists if required
                mergedOrDissipated = true
                break
            }

            // Move the cloud to the neighbor tile's location
            reduceSunlight(MIN_SUNLIGHT_REDUCTION, currTile)
            cloud.location = nextTile.location
            currTile = nextTile
            moves++

            nextTile = map.getNeighbor(tile, direction)
        }

        cloud.duration -= 1

        if (!mergedOrDissipated) {
            val currTile = map.getTileByCoordinate(cloud.location) as Tile
            if (simulationMap.nextTileNullButAirflow(currTile)) {
                cloud.isStuck = true
            }
            reduceSunlight(MAX_SUNLIGHT_REDUCTION, currTile)
            cloud.maxTraversibleTiles = MAX_TRAVERSIBLE_TILES
        }
    }

    private fun checkMerge(tile: Tile): Boolean {
        return coordinateToCloud.contains(tile.location)
    }

    private fun merge(cloud: Cloud): Cloud {
        // Can assert that the target cloud exists since we checked for it before calling this function
        val targetCloud = coordinateToCloud[cloud.location]!!
        // Mark both clouds as removed
        removedClouds.add(cloud.id)
        removedClouds.add(targetCloud.id)

        // Remove both clouds from the coordinate to cloud mapping
        coordinateToCloud.remove(cloud.location)
        coordinateToCloud.remove(targetCloud.location)

        // Merge the two clouds into a new cloud
        val newCloud = createMergedCloud(cloud, targetCloud)

        // Add the new cloud to the merged list
        mergedList.add(newCloud)
        // Update the coordinate to cloud mapping with new cloud at the location
        coordinateToCloud[newCloud.location] = newCloud
        // ADD LOG
        return newCloud
    }

    private fun createMergedCloud(cloud: Cloud, targetCloud: Cloud): Cloud {
        val newCloud: Cloud = Cloud(
            id = maxCloudID + 1,
            location = targetCloud.location,
            duration = minOf(targetCloud.duration, cloud.duration),
            amount = targetCloud.amount + cloud.amount
        )
        newCloud.maxTraversibleTiles = maxOf(cloud.maxTraversibleTiles, targetCloud.maxTraversibleTiles)
        maxCloudID += 1
        return newCloud
    }

    private fun checkRainDissipate(cloud: Cloud): Boolean {
        return cloud.amount <= 0
    }

    private fun checkDurationDissipate(cloud: Cloud): Boolean {
        return cloud.duration == 0
    }

    private fun checkVillageDissipate(tile: Tile): Boolean {
        val tile: Tile = map.getTileByCoordinate(tile.location) as Tile
        return tile.category == TileType.VILLAGE
    }

    private fun dissipate(cloud: Cloud) {
        removedClouds.add(cloud.id)
        coordinateToCloud.remove(cloud.location)
        // ADD LOG
    }

    private fun reduceSunlight(amount: Int, tile: Tile) {
        tile.currentSunlight -= amount
        if (tile.currentSunlight < 0) {
            tile.currentSunlight = 0
        }
    }
}
