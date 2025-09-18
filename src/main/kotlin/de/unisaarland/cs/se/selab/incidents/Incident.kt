package de.unisaarland.cs.se.selab.incidents
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.enumerations.IncidentType

/**
 * abstract incident class
 */
abstract class Incident(
    val id: Int,
    val tick: Int,
    val type: IncidentType
) {
    /**
     * execute method, will be overridden in concrete classes
     */
    abstract fun execute(map: Map, yearTick: Int)
}