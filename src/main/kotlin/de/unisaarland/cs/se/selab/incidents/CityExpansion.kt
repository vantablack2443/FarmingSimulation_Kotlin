package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.map.Map

/**
 * city expansion incident
 */
class CityExpansion(id: Int, tick: Int, type: IncidentType) : Incident(id, tick, type) {
    override fun execute(map: Map, yearTick: Int) {
        TODO("Not yet implemented")
    }
}
