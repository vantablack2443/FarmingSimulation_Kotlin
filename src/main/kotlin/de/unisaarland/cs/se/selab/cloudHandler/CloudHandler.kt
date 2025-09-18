package de.unisaarland.cs.se.selab.cloudHandler

import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.cloud.Cloud

class CloudHandler(val map: Map, val coordinateToCLoud: MutableMap<Coordinate, Cloud>) {
    // Add code that will update maxCloudInt
    var maxCloudInt = 0
}