package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap

/**
 * broken machine incident
 */
class BrokenMachine(id: Int, tick: Int, type: IncidentType, val machine: Machine, val duration: Duration) :
    Incident(
        id,
        tick,
        type
    ) {
    override fun execute(map: SimulationMap, yearTick: Int) {
        TODO("Not yet implemented")
    }
}
