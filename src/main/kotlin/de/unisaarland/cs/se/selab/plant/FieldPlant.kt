package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.duration.Duration

/**
 * abstract class for field plants
 */
abstract class FieldPlant : Plant() {
    open var sowingTime: Duration = Duration(-1, -1)
    open var sownTick: Int = -1
}