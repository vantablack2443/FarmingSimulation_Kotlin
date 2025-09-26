package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.map.SimulationMap

/**
 * class for handling incidents
 */
class IncidentHandler(var map: SimulationMap) {
    lateinit var incidents: List<Incident>
    private val activeIncidents = mutableListOf<Incident>()

    /**
     * function for updating the active incidents list per tick
     */
    fun updateIncidentsForTick(simTick: Int) {
        // Incident Handler gets a list of incidents ordered by ID from the simulation (Init)
        // The filtered list here contains all the incidents for a given tick ordered by ID in ascending order
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
}
