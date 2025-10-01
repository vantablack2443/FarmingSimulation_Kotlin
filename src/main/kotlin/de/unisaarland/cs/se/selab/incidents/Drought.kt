package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.tile.Tile

const val FALLOW_DURATION = 4

/**
 * drought incident
 */
class Drought(id: Int, tick: Int, type: IncidentType, val tile: Tile, val radius: Int) : Incident(id, tick, type) {
    /**
     * executes the drought incident
     * @param simulationMap the map of the simulation the incidents will be executed on
     * @param yearTick the tick the incident will be simulated on
     */
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        val tmp = simulationMap.getTilesByRadius(tile, radius) + tile
        val incidentTiles: List<Tile> = simulationMap.filterForPlantable(tmp)
        val affectedIds: MutableSet<Int> = mutableSetOf()
        incidentTiles.sortedBy { it.id }
        for (tile in incidentTiles) {
            if (tile.category == TileType.FIELD) {
                // Fallowing and killing the plants will be handled by the estimator using the droughtHit bool
                // tile.plant = null
                // tile.currentCrop = null
                tile.droughtHit = true
                tile.currentMoisture = 0
                tile.fallowDuration = Duration(tick + 1, tick + FALLOW_DURATION)
                affectedIds.add(tile.id)
            } else if (tile.category == TileType.PLANTATION) {
                tile.droughtHit = true
                tile.currentMoisture = 0
                tile.plantationDamaged = true
                // tile.plant = null
                // tile.currentCrop = null
                affectedIds.add(tile.id)
            }
        }
        Logger.logIncident(id, IncidentType.DROUGHT, affectedIds.toList().sortedBy { it })
    }
}
