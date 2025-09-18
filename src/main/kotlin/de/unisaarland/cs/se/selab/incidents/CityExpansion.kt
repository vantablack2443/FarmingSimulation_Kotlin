package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * city expansion incident
 */
class CityExpansion(
    id: Int,
    tick: Int,
    type: IncidentType,
    val tile: Tile,
    val farms: List<Farm>
) : Incident(id, tick, type) {
    override fun execute(map: Map, yearTick: Int) {
        TODO("Not yet implemented")
    }
}
