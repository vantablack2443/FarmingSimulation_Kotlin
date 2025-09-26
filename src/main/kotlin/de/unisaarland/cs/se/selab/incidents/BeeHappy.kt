package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

const val HUNDRED = 100

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
        val reach = simulationMap.getTilesByRadius(tile, radius)
        val meadows = simulationMap.filterByType(TileType.MEADOW, reach)
        val affectedTiles = mutableSetOf<Tile>()
        for (meadow in meadows) {
            affectedTiles += simulationMap.filterForPlantable(simulationMap.getTilesByRadius(meadow, 2))
        }
        for (tile in affectedTiles) {
            val tilePlant = tile.plant ?: continue
            if (tile.category == TileType.FIELD && tilePlant.isBlooming(tick)) {
                tilePlant.doBeeHappy((HUNDRED + effect).toDouble() / HUNDRED)
            }
            if (tile.category == TileType.PLANTATION && tilePlant.isBlooming(yearTick)) {
                tilePlant.doBeeHappy((HUNDRED + effect).toDouble() / HUNDRED)
            }
        }
    }
}
