package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * cloud creation incident
 */
class CloudCreation(
    id: Int,
    tick: Int,
    type: IncidentType,
    val tile: Tile,
    val radius: Int,
    val duration: Int,
    val amount: Int
) : Incident(
    id,
    tick,
    type
) {
    lateinit var cloudHandler: CloudHandler

    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        val affectedTiles = simulationMap.getTilesByRadius(tile, radius)
            .filter { it.category != TileType.VILLAGE }
            .sortedBy { it.id }
        val tileIDs = affectedTiles.map { it.id }
        Logger.logIncident(this.id, IncidentType.CLOUD_CREATION, tileIDs)

        for (tile in affectedTiles) {
            val cloud = createCloud(tile)
            cloudHandler.addCloud(cloud)
            if (cloudHandler.checkMerge(tile)) {
                val targetCloud = cloudHandler.getCloudByCoordinate(tile.location) ?: continue
                val newCloud = cloudHandler.merge(cloud, targetCloud)
                Logger.logCloudMerge(
                    targetCloud.id,
                    cloud.id,
                    newCloud.id,
                    this.amount,
                    this.duration,
                    tile.id
                )
            }
        }
    }

    /**
     * setter for cloud handler
     */
    fun setCloudHandler(cloudHandler: CloudHandler) {
        this.cloudHandler = cloudHandler
    }

    /**
     * creates a new cloud instance and adds it into cloud handler mapping
     */
    private fun createCloud(tile: Tile): Cloud {
        val newID = cloudHandler.getMaxCloudID()
        cloudHandler.setMaxCloudID(newID + 1)
        val newCloud = Cloud(newID, tile.location, this.duration, this.amount)
        cloudHandler.addCloud(newCloud)
        return newCloud
    }
}
