package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * bee happy incident
 */
class BeeHappy(
    id: Int,
    tick: Int,
    type: IncidentType,
    val effect: Int,
    val tile: Tile,
    val radius: Int
) : Incident(id, tick, type) {
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        TODO("Not yet implemented")
    }
}
