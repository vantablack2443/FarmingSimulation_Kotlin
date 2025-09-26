package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Animal attack incident
 */
class AnimalAttack(
    id: Int,
    tick: Int,
    type: IncidentType,
    val tile: Tile,
    val radius: Int
) : Incident(id, tick, type) {
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        val reach = simulationMap.getTilesByRadius(tile, radius)
        val forests = simulationMap.filterByType(TileType.FOREST, reach)
        val affectedTiles = mutableSetOf<Tile>()
        for (forest in forests) {
            affectedTiles += simulationMap.filterForPlantable(simulationMap.getTilesByRadius(forest, 1))
        }
        for (tile in affectedTiles) {
            val tilePlant = tile.plant ?: continue
            tilePlant.doAnimalAttack()
            tilePlant.resetMowingTime(yearTick)
        }
    }
}
