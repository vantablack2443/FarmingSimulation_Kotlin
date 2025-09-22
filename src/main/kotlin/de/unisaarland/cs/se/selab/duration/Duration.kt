package de.unisaarland.cs.se.selab.duration

class Duration(val startTick: Int, val endTick: Int) {
    fun inRange(tick: Int): Boolean {
        return tick in startTick..endTick
    }
}
