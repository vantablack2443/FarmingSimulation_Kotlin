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
class CloudHandlerAlt(val simulationMap: SimulationMap) {
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
        for (cloud in cloudsList) {
            val tile = map.getTileByCoordinate(cloud.location) ?: return
            if (tile.category in setOf(TileType.FIELD, TileType.PLANTATION)) {
                Logger.logCloudPosition(cloud.id, tile.id, tile.currentSunlight)
            }
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
        if (tile.category != TileType.FIELD && tile.category != TileType.PLANTATION) {
            return 0
        } else if (cloud.amount >= MIN_RAIN_AMOUNT) {
            if (tile.currentMoisture == null) return cloud.amount
            val neededMoisture = tile.maxMoisture?.minus(tile.currentMoisture ?: 0) ?: 0
            return minOf(neededMoisture, cloud.amount)
        }
        return 0
    }

    /**
     * tries to rain returns true if the cloud dissipates false if not
     */
    private fun tryRain(c: Cloud): Boolean {
        val tile = simulationMap.getTileByCoordinate(c.location)!!
        val rainAmount = checkRain(c, tile)
        if (rainAmount > 0) {
            c.amount = maxOf(0, c.amount - rainAmount)
            tile.increaseMoistureByAmount(rainAmount)
            Logger.logRain(c.id, tile.id, rainAmount)
        }
        if (c.amount == 0) {
            Logger.logDissipation(c.id, tile.id)
            removedClouds.add(c)
            coordinateToCloud.remove(c.location)
            return true
        }
        return false
    }

    /**
     * creates a new instance of the merged cloud
     */
    private fun createMergedCloud(cloud: Cloud, targetCloud: Cloud): Cloud {
        val newDuration: Int = if (targetCloud.duration != -1 && cloud.duration != -1) {
            minOf(targetCloud.duration, cloud.duration)
        } else {
            maxOf(targetCloud.duration, cloud.duration)
        }
        setMaxCloudID(maxCloudID + 1)
        val newCloud = Cloud(
            id = maxCloudID,
            location = targetCloud.location,
            duration = newDuration,
            amount = targetCloud.amount + cloud.amount
        )
        newCloud.maxTraversibleTiles = maxOf(cloud.maxTraversibleTiles, targetCloud.maxTraversibleTiles)
        maxCloudID += 1
        return newCloud
    }

    /**
     * handles all the merging logic (removing and adding new cloud)
     */
    private fun merge(originC: Cloud, destinationC: Cloud) {
        reduceSunlight(MIN_SUNLIGHT_REDUCTION, simulationMap.getTileByCoordinate(originC.location)!!)
        val newCloud = createMergedCloud(originC, destinationC)
        addCloud(newCloud)
        removedClouds.add(originC)
        removedClouds.add(destinationC)
        Logger.logCloudMerge(
            originC.id,
            destinationC.id,
            newCloud.id,
            newCloud.amount,
            newCloud.duration,
            simulationMap.getTileByCoordinate(newCloud.location)!!.id
        )
    }

    /**
     * originally the moveCloudHandler
     */
    private fun moveCloud(c: Cloud) {
        var moves = 0
        while (moves <= c.maxTraversibleTiles && !c.isStuck) {
            // instead of returning throwing errors should be a good idea
            val currTile = simulationMap.getTileByCoordinate(c.location) ?: return
            val nextTile = simulationMap.getNeighbor(currTile, currTile.direction)
            val nextCloud = coordinateToCloud[nextTile?.location]

            // check for merges
            if (nextCloud != null) {
                merge(c, nextCloud)
                return
            }

            if (nextTile == null) {
                c.isStuck = true
                /* missing try rain here because of needing return statement in case of dissipating (too complex) */
                break
            }

            if (currTile.category == TileType.PLANTATION || currTile.category == TileType.FIELD) {
                reduceSunlight(MIN_SUNLIGHT_REDUCTION, currTile)
                Logger.logSunlight(currTile.id, currTile.currentSunlight)
            }
            c.location = nextTile.location
            Logger.logCloudMove(c.id, c.amount, currTile.id, nextTile.id)

            // check for village after moving
            if (villageCheck(c)) {
                villageDissipate(c)
                return
            }

            // check for rain after moving
            if (tryRain(c)) {
                return
            }
            moves++
        }
        reduceSunlight(MAX_SUNLIGHT_REDUCTION, simulationMap.getTileByCoordinate(c.location)!!)
        c.duration--
    }

    /**
     * move clouds phase
     */
    fun moveClouds() {
        val cloudIterator = cloudsList.iterator()
        while (cloudIterator.hasNext()) {
            val cloud = cloudIterator.next()
            // check if cloud should be removed and for dissipation combined because detekt cant have to many continue
            if (cloud in removedClouds || tryRain(cloud)) {
                continue
            }

            // check if cloud is on VILLAGE in case of CityExtension incident
            if (villageCheck(cloud)) {
                villageDissipate(cloud)
                continue
            }

            /* might be missing is stuck check + duration? might be in the moveCloud function */

            moveCloud(cloud)
        }

        for (c in removedClouds) {
            cloudsList.remove(c)
        }
        logCloudPositions()
    }
}
