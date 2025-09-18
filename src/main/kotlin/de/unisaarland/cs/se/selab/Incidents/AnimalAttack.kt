package de.unisaarland.cs.se.selab.Incidents
import de.unisaarland.cs.se.selab.Map

class AnimalAttack(id: Int, tick: Int, type: IncidentType):Incident(id, tick, type) {
    override fun execute(map: Map, yearTick: Int) {
        TODO("Not yet implemented")
    }
}