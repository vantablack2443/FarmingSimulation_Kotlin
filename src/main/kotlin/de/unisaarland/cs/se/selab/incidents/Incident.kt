package de.unisaarland.cs.se.selab.incidents
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.map.SimulationMap

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
    abstract fun execute(simulationMap: SimulationMap, simTick: Int)
}
