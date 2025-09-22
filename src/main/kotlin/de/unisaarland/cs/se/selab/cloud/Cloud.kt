package de.unisaarland.cs.se.selab.cloud
import de.unisaarland.cs.se.selab.coordinate.Coordinate

class Cloud(val id: Int, var location: Coordinate, var duration: Int, var amount: Int) {
    var maxTraversibleTiles: Int = 10
    var isStuck: Boolean = false

}