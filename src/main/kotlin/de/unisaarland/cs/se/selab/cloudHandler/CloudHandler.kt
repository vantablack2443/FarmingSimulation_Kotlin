package de.unisaarland.cs.se.selab.cloudHandler

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.TEN
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

const val MAX_SUNLIGHT_REDUCTION = 50
const val MIN_SUNLIGHT_REDUCTION = 3
const val MIN_RAIN_AMOUNT = 5000

/**
 * handler class for cloud movement
 */
class CloudHandler(val simulationMap: SimulationMap) {
    val coordinateToCloud: MutableMap<Coordinate, Cloud> = mutableMapOf()
    val cloudsList: MutableList<Cloud> = mutableListOf()
    val removedClouds: MutableList<Cloud> = mutableListOf()
    private var maxCloudID = 0
    private val map = simulationMap

    /**
     * setter for max Cloud ID
     * @param maxCloudID the maximum cloud ID
     */
    fun setMaxCloudID(maxCloudID: Int) {
        this.maxCloudID = maxCloudID
    }

    /**
     * getter for max cloud id
     */
    fun getMaxCloudID(): Int {
        return maxCloudID
    }

    /**
     * adds the given cloud instance to the mapping
     * @param cloud cloud that would be added
     */
    fun addCloud(cloud: Cloud) {
        this.coordinateToCloud[cloud.location] = cloud
        this.cloudsList.add(cloud)
    }

    /**
     * reduces sunlight on the given tile by given amount
     */
    private fun reduceSunlight(amount: Int, tile: Tile) {
        tile.currentSunlight = maxOf(tile.currentSunlight - amount, 0)
    }

    /**
     * logs all cloud positions at the end of movement phase
     */
    private fun logCloudPositions() {
        // only for plantable tiles
        val plantables = map.getPlantableTiles().sortedBy { it.id }
        for (tile in plantables) {
            val cloud = this.getCloudByCoordinate(tile.location) ?: continue
            reduceSunlight(
                MAX_SUNLIGHT_REDUCTION,
                tile
            )
            Logger.logCloudPosition(cloud.id, tile.id, tile.currentSunlight)
        }
    }

    /**
     * checks and dissipates if on a village
     */
    private fun villageCheck(c: Cloud): Boolean {
        return simulationMap.getTileByCoordinate(c.location)?.category == TileType.VILLAGE
    }

    /**
     * handles dissipating on villages()
     */
    private fun villageDissipate(c: Cloud) {
        val villageTile = simulationMap.getTileByCoordinate(c.location) ?: return
        Logger.logCloudStuck(c.id, villageTile.id)
        coordinateToCloud.remove(c.location)
        removedClouds.add(c)
    }

    /**
     * checks for rain, return the rain amount
     */
    private fun checkRain(cloud: Cloud, tile: Tile): Int {
        if (cloud.amount >= MIN_RAIN_AMOUNT) {
            if (tile.category in setOf(TileType.FIELD, TileType.PLANTATION)) {
                val neededMoisture = tile.maxMoisture?.minus(tile.currentMoisture ?: 0) ?: 0
                return minOf(neededMoisture, cloud.amount)
            }
            if (tile.currentMoisture == null) return cloud.amount
        }
        return 0
    }

    /**
     * tries to rain returns true if the cloud dissipates false if not
     */
    private fun tryRain(c: Cloud): Boolean {
        val tile = simulationMap.getTileByCoordinate(c.location) ?: return false
        val rainAmount = checkRain(c, tile)
        if (rainAmount > 0) {
            c.amount = maxOf(0, c.amount - rainAmount)
            tile.increaseMoistureByAmount(rainAmount)
            Logger.logRain(c.id, tile.id, rainAmount)
        }
        if (c.amount == 0) {
            dissipate(c)
            return true
        }
        return false
    }

    /**
     * checks if the cloud duration is exhausted
     */
    private fun checkDurationDissipate(cloud: Cloud): Boolean {
        return cloud.duration == 0
    }

    /**
     * creates a new instance of the merged cloud
     */
    private fun createMergedCloud(existingCloud: Cloud, targetCloud: Cloud): Cloud {
        val newDuration: Int = if (targetCloud.duration != -1 && existingCloud.duration != -1) {
            minOf(targetCloud.duration, existingCloud.duration)
        } else {
            maxOf(targetCloud.duration, existingCloud.duration)
        }
        setMaxCloudID(maxCloudID + 1)
        val newCloud = Cloud(
            id = maxCloudID,
            location = existingCloud.location,
            duration = newDuration,
            amount = targetCloud.amount + existingCloud.amount
        )
        newCloud.maxTraversibleTiles = maxOf(existingCloud.maxTraversibleTiles, targetCloud.maxTraversibleTiles)
        return newCloud
    }

    /**
     * handles all the merging logic (removing and adding new cloud)
     */
    fun merge(originC: Cloud, destinationC: Cloud): Cloud {
        val destinationTile = simulationMap.getTileByCoordinate(originC.location)
        val newCloud = createMergedCloud(originC, destinationC)
        coordinateToCloud.entries.removeIf { it.value == originC || it.value == destinationC }
        coordinateToCloud[originC.location] = newCloud
        removedClouds.add(originC)
        removedClouds.add(destinationC)
        Logger.logCloudMerge(
            originC.id,
            destinationC.id,
            newCloud.id,
            newCloud.amount,
            newCloud.duration,
            destinationTile?.id ?: -1
        )
        return newCloud
    }

    /**
     * originally the moveCloudHandler
     */
    private fun moveCloud(c: Cloud) {
        // check if cloud is on VILLAGE in case of CityExtension incident
        if (villageCheck(c)) {
            villageDissipate(c)
            return
        }
        while (c.maxTraversibleTiles > 0 && !c.isStuck) {
            // instead of returning throwing errors should be a good idea
            val currTile = simulationMap.getTileByCoordinate(c.location) ?: continue
            val nextTile = simulationMap.getNeighbor(currTile, currTile.direction)
            val nextCloud = coordinateToCloud[nextTile?.location]
            // check for merges
            if (nextCloud != null) {
                c.maxTraversibleTiles--
                logLocationChange(c, currTile, nextTile ?: currTile)
                val newCloud = merge(nextCloud, c)
                cloudsList.add(newCloud)
                return
            }

            if (nextTile == null) {
                c.isStuck = true
                break
            }
            c.maxTraversibleTiles--
            logLocationChange(c, currTile, nextTile)
            coordinateToCloud.remove(c.location)
            c.location = nextTile.location
            coordinateToCloud[c.location] = c
            // check for village after moving
            if (villageCheck(c)) {
                villageDissipate(c)
                return
            }

            // check for rain after moving
            if (tryRain(c)) {
                return
            }
        }
        if (c.duration > 0) c.duration--
        if (checkDurationDissipate(c)) {
            dissipate(c)
        }
    }

    /**
     * helper function to log cloud movement
     */
    private fun logLocationChange(cloud: Cloud, currTile: Tile, nextTile: Tile) {
        Logger.logCloudMove(cloud.id, cloud.amount, currTile.id, nextTile.id)
        if (currTile.category == TileType.PLANTATION || currTile.category == TileType.FIELD) {
            reduceSunlight(MIN_SUNLIGHT_REDUCTION, currTile)
            Logger.logSunlight(currTile.id, currTile.currentSunlight)
        }
    }

    /**
     * helper function for dissipation
     */
    private fun dissipate(c: Cloud) {
        coordinateToCloud.remove(c.location)
        removedClouds.add(c)
        val tile = map.getTileByCoordinate(c.location) ?: return
        Logger.logDissipation(c.id, tile.id)
    }

    /**
     * move clouds phase
     */
    fun moveClouds() {
        var i = 0
        while (i < cloudsList.size) {
            val cloudToMove = cloudsList.getOrElse(i) { null }
            if (cloudToMove != null) {
                if (cloudToMove in removedClouds || tryRain(cloudToMove)) {
                    i++
                    continue
                }
                if (cloudToMove.isStuck) {
                    handleStuckCloud(cloudToMove)
                    i++
                    continue
                }
                moveCloud(cloudToMove)
                i++
            }
        }

        for (c in removedClouds) {
            // update clouds list
            cloudsList.remove(c)
        }
        // empty removed clouds list
        removedClouds.clear()
        logCloudPositions()
        resetTraversibleTiles()
    }

    /**
     * helper function to reset the traversible tiles each tick
     */
    private fun resetTraversibleTiles() {
        for (cloud in cloudsList) {
            cloud.maxTraversibleTiles = TEN
        }
    }

    /**
     * helper function
     */
    private fun handleStuckCloud(cloud: Cloud) {
        if (cloud.duration > 0) cloud.duration--
        if (checkDurationDissipate(cloud)) dissipate(cloud)
    }

    /**
     * returns the  cloud on the given coordinate
     */
    fun getCloudByCoordinate(coordinate: Coordinate): Cloud? {
        return this.coordinateToCloud[coordinate]
    }

    /**
     * helper function for cloud test; adds cloud to the list
     */
    fun addCloudToCloudsList(c: Cloud) {
        this.cloudsList.add(c)
    }
}
