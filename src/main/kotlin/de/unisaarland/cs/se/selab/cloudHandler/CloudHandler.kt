package de.unisaarland.cs.se.selab.cloudHandler

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.map.SimulationMap

/**
 * handler class for cloud movement
 */
class CloudHandler(val simulationMap: SimulationMap) {
    // Add code that will update maxCloudInt
    private lateinit var coordinateToCloud: MutableMap<Coordinate, Cloud>
    private var maxCloudID = 0
    private var map = simulationMap

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
}
