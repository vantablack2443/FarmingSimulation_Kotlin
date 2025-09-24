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
    /**
     * executes the broken machine
     * @param simulationMap the map the incident will be executed on
     * @param yearTick the tick the incident will be executed on
     */
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        val currentBrokenFor = machine.brokenFor
        if (currentBrokenFor == null) {
            machine.brokenFor = duration
        } else {
            var endTick = currentBrokenFor.endTick
            endTick = if (duration.endTick > endTick) duration.endTick else endTick
            machine.brokenFor = Duration(tick, endTick)
        }
    }
}
