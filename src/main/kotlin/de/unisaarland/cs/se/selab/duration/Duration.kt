package de.unisaarland.cs.se.selab.duration

/**
 * class for duration with start and end tick
 */
class Duration(val startTick: Int, val endTick: Int) {

    /**
     * checks if a tick is in the range of the duration
     */
    fun inRange(tick: Int): Boolean {
        return tick in startTick..endTick
    }
}
