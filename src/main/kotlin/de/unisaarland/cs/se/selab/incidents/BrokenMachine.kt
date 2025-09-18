package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.Map

class BrokenMachine(id: Int, tick: Int, type: Incident.IncidentType): Incident(id, tick, type) {
    override fun execute(map: Map, yearTick: Int) {
        TODO("Not yet implemented")
    }
}