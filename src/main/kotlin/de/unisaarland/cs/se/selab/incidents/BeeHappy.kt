package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.map.SimulationMap

/**
 * bee happy incident
 */
class BeeHappy(id: Int, tick: Int, type: IncidentType) : Incident(id, tick, type) {
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        TODO("Not yet implemented")
    }
}
