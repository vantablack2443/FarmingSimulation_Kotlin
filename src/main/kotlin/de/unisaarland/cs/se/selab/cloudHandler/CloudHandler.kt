package de.unisaarland.cs.se.selab.cloudHandler

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

const val MAX_SUNLIGHT_REDUCTION = 50
const val MIN_SUNLIGHT_REDUCTION = 30
const val MAX_TRAVERSIBLE_TILES = 10
const val MIN_RAIN_AMOUNT = 5000

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
        logCloudPositions()
    }

    /**
     private fun checkRainAlternate(cloud: Cloud, tile: Tile): Int {
     if (tile.currentMoisture == null) return cloud.amount
     if (cloud.amount >= MIN_RAIN_AMOUNT) {
     return maxOf(tile.maxMoisture!! - tile.currentMoisture!!, 0)
     }
     return 0
     }

     private fun rainAlternate(cloud: Cloud, tile: Tile, amount: Int) {
     cloud.amount -= amount
     tile.currentMoisture = tile.currentMoisture?.plus(amount)
     }
     **/

    private fun checkRain(cloud: Cloud, tile: Tile): Int {
        if (tile.currentMoisture == null) return cloud.amount
        if (cloud.amount >= MIN_RAIN_AMOUNT) {
            val neededMoisture = tile.maxMoisture!! - tile.currentMoisture!!
            return minOf(neededMoisture, cloud.amount)
        }
        return 0
    }

    private fun rain(cloud: Cloud, tile: Tile, amount: Int) {
        cloud.amount -= amount
        Logger.logRain(cloud.id, tile.id, amount)
        if (cloud.amount == 0) {
            dissipate(cloud, tile, false)
        }
    }

    /*
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
    */

    private fun tryRainAndDissipate(tile: Tile, cloud: Cloud): Boolean {
        val rainAmount = checkRain(cloud, tile)
        if (rainAmount > 0) {
            rain(cloud, tile, rainAmount)
            if (checkRainDissipate(cloud)) {
                dissipate(cloud, tile, false)
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
            dissipate(cloud, tile, true)
            return
        }

        // Rain if possible, then dissipate if amount is 0
        if (tryRainAndDissipate(tile, cloud)) {
            return
        }
        // if (cloud.isStuck) return

        handleCloudMovement(cloud)
    }

    private fun handleCloudMovement(cloud: Cloud) {
        var moves = 0
        var mergedOrDissipated = false
        while (moves < cloud.maxTraversibleTiles) {
            // Move the cloud in the airflow direction, or mark it as stuck if it can't move
            val currTile = map.getTileByCoordinate(cloud.location) as Tile
            val direction = currTile.direction
            val nextTile = map.getNeighbor(currTile, direction)
            // If the neighbor tile is null, mark the cloud as stuck and check for dissipation(duration Check)
            if (nextTile == null || direction == null) {
                handleStuckCloud(cloud, currTile)
                return
            }
            // Check for village dissipation
            if (checkVillageDissipate(nextTile)) {
                dissipate(cloud, nextTile, true)
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
            Logger.logCloudMove(cloud.id, cloud.amount, currTile.id, nextTile.id)
            reduceSunlight(MIN_SUNLIGHT_REDUCTION, currTile)
            checkForPlantable(currTile)
            cloud.location = nextTile.location
            moves++
        }

        cloud.duration = maxOf(cloud.duration - 1, 0)
        val currTile = map.getTileByCoordinate(cloud.location) as Tile
        if (checkDurationDissipate(cloud)) {
            dissipate(cloud, currTile, false)
        }

        if (!mergedOrDissipated) {
            handlePostMovement(cloud)
        }
    }

    private fun handleStuckCloud(cloud: Cloud, tile: Tile) {
        cloud.isStuck = true
        if (cloud.duration != -1) {
            cloud.duration = maxOf(cloud.duration - 1, 0)
        }
        if (checkDurationDissipate(cloud)) {
            dissipate(cloud, tile, false)
        }
    }

    private fun handlePostMovement(cloud: Cloud) {
        val currTile = map.getTileByCoordinate(cloud.location) as Tile
        if (simulationMap.nextTileNullButAirflow(currTile)) {
            cloud.isStuck = true
        }
        reduceSunlight(MAX_SUNLIGHT_REDUCTION, currTile)
        cloud.maxTraversibleTiles = MAX_TRAVERSIBLE_TILES
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
        val newCloud = Cloud(
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

    private fun dissipate(cloud: Cloud, tile: Tile, village: Boolean) {
        removedClouds.add(cloud.id)
        coordinateToCloud.remove(cloud.location)
        maxCloudID = coordinateToCloud.values.maxOf { it.id }
        // ADD LOG
        if (village) {
            Logger.logCloudStuck(cloud.id, tile.id)
        } else {
            Logger.logDissipation(cloud.id, tile.id)
        }
    }

    private fun reduceSunlight(amount: Int, tile: Tile) {
        tile.currentSunlight = maxOf(tile.currentSunlight - amount, 0)
    }

    private fun checkForPlantable(tile: Tile) {
        if (tile.category in setOf(TileType.FIELD, TileType.PLANTATION)) {
            Logger.logSunlight(tile.id, tile.currentSunlight)
        }
    }

    private fun logCloudPositions() {
        for (cloud in cloudsList) {
            val tile = map.getTileByCoordinate(cloud.location) ?: return
            Logger.logCloudPosition(cloud.id, tile.id, tile.currentSunlight)
        }
    }
}
