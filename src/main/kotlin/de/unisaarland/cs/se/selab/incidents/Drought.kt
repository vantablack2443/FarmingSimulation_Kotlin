package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

const val FALLOW_DURATION = 4

/**
 * drought incident
 */
class Drought(id: Int, tick: Int, type: IncidentType, val tile: Tile, val radius: Int) : Incident(id, tick, type) {
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        val incidentTiles: List<Tile> = simulationMap.filterForPlantable(simulationMap.getTilesByRadius(tile, radius))
        for (tile in incidentTiles) {
            if (tile.category == TileType.FIELD) {
                tile.plant = null
                tile.currentCrop = null
                tile.currentMoisture = 0
                tile.fallowDuration = Duration(tick + 1, tick + FALLOW_DURATION)
            } else if (tile.category == TileType.PLANTATION) {
                tile.currentMoisture = 0
                tile.plantationDamaged = true
            }
        }
    }
}
