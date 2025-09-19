package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * cloud creation incident
 */
class CloudCreation(
    id: Int,
    tick: Int,
    type: IncidentType,
    val tile: Tile,
    val radius: Int,
    val duration: Int,
    val amount: Int
) : Incident(
    id,
    tick,
    type
) {
    lateinit var cloudHandler: CloudHandler

    override fun execute(map: SimulationMap, yearTick: Int) {
        TODO("Not yet implemented")
    }
}
