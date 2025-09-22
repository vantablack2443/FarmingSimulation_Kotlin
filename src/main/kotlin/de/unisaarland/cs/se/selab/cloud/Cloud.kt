package de.unisaarland.cs.se.selab.cloud
import de.unisaarland.cs.se.selab.coordinate.Coordinate

const val TEN = 10

/**
 * Cloud data class
 */

class Cloud(val id: Int, var location: Coordinate, var duration: Int, var amount: Int) {
    var maxTraversibleTiles: Int = TEN
    var isStuck: Boolean = false
}
