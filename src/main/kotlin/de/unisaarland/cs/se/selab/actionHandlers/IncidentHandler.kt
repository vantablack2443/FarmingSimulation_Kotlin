package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.map.SimulationMap

/**
 * class for handling incidents
 */
class IncidentHandler(var map: SimulationMap) {
    lateinit var incidents: List<Incident>
    private var activeIncidents = mutableListOf<Incident>()

    /**
     * function for updating the active incidents list per tick
     */
    fun updateIncidentsForTick(simTick: Int) {
        activeIncidents.clear()
        val currentIncidents = incidents.filter { it.tick == simTick }
        activeIncidents.addAll(currentIncidents)
    }

    /**
     * function that executes the active incidents
     */
    fun executeIncidents(yearTick: Int) {
        for (incident in activeIncidents) {
            incident.execute(this.map, yearTick)
        }
    }

    /**
     * sets the incidents list (is used in simulation)
     */
    fun setIncidents(incidents: List<Incident>) {
        this.incidents = incidents.toList()
    }
}
