package de.unisaarland.cs.se.selab.machine

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Machine class
 */
class Machine(
    val id: Int,
    val farmID: Int,
    val name: String,
    val duration: Int,
    var currentTile: Tile,
    val actions: List<ActionType>,
    val plants: List<PlantType>,
    var homeShed: Tile
) {
    var elapsedTime: Int = 0
    var isStuck: Boolean = false
    var brokenFor: Duration? = null
    var currentHarvest: PlantAndHarvest? = null
}
