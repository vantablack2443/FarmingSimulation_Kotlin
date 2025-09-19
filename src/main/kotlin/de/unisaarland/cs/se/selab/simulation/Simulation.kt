package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.coordinate.Coordinate

class Simulation (data: SimulationData, var maxTicks: Int, var currentYearTick: Int) {
    var currentTick: Int = 0
    var map = data.map
    private var cloudHandler = CloudHandler(map)
    init {
        var clouds = data.clouds.values.toList()
        var mapping = mutableMapOf<Coordinate, Cloud>()
        cloudHandler.setMaxCloudID(clouds.maxOf { it.id })
        for (cloud in clouds) {
            mapping[cloud.location] = cloud
        }
    }

}
