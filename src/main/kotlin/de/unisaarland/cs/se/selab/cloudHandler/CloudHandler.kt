package de.unisaarland.cs.se.selab.cloudHandler

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.cloud.Cloud

class CloudHandler(val simulationMap: SimulationMap, val coordinateToCLoud: MutableMap<Coordinate, Cloud>) {
    // Add code that will update maxCloudInt
    var maxCloudInt = 0
}