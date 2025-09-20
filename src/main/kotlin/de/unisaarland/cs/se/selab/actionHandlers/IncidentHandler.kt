package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.simulation.SimulationData

/**
 * class for handling incidents
 */
class IncidentHandler(simData: SimulationData, map: SimulationMap) {
     lateinit var incidents: List<Incident>
     private var activeIncidents = mutableListOf<Incident>()

    /**
     * function for updating the active incidents list per tick
     */
    fun updateIncidentsForTick(tick: Int) {
        TODO()
    }

    /**
     * function that executes the active incidents
     */
    fun executeIncidents(yearTick: Int) {
        TODO()
    }

    /**
     * sets the incidents list (is used in simulation)
     */
    fun setIncidents(incidents: List<Incident>) {
        this.incidents = incidents.toList()
    }
}