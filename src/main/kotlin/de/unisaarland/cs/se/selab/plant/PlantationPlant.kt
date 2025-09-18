package de.unisaarland.cs.se.selab.plant
import de.unisaarland.cs.se.selab.duration.Duration

/**
 * abstract class for plantation plants
 */
abstract class PlantationPlant : Plant() {
    open var cuttingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
    open var mowingTime: MutableList<Pair<Duration, Boolean>> = mutableListOf()
}
