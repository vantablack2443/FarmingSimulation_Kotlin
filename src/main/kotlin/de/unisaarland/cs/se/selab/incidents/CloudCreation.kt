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
        val affectedTiles = (simulationMap.getTilesByRadius(tile, radius) + tile)
            .filter { it.category != TileType.VILLAGE }
            .sortedBy { it.id }
        val tileIDs = affectedTiles.map { it.id }
        Logger.logIncident(this.id, IncidentType.CLOUD_CREATION, tileIDs)

        for (tile in affectedTiles) {
            val cloud = createCloud(tile)
            val existingCloud = cloudHandler.coordinateToCloud[tile.location]
            // check for merges
            if (existingCloud != null) {
                val newCloud = cloudHandler.merge(cloud, existingCloud)
                cloudHandler.cloudsList.add(newCloud)
                cloudHandler.cloudsList.remove(existingCloud)
            } else {
                cloudHandler.addCloud(cloud)
            }
        }
    }

    /**
     * creates a new cloud instance and adds it into cloud handler mapping
     */
    private fun createCloud(tile: Tile): Cloud {
        val newID = cloudHandler.getMaxCloudID() + 1
        cloudHandler.setMaxCloudID(newID)
        val newCloud = Cloud(newID, tile.location, this.duration, this.amount)
        return newCloud
    }
}
