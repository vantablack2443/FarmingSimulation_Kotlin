package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.enumerations.IncidentType

/**
 * cloud creation incident
 */
class CloudCreation(id: Int, tick: Int, type: IncidentType) : Incident(id, tick, type) {
    override fun execute(map: Map, yearTick: Int) {
        TODO("Not yet implemented")
    }
}