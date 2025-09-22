package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.map.SimulationMap
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
    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        this.tile.category = TileType.VILLAGE
        removeFromFarms(tile)
        this.tile.farmID = null
    }

    private fun removeFromFarms(tile: Tile) {
        val farmid = tile.farmID ?: return

        for (farm in farms) {
            if (farm.getId() == farmid) {
                val fields: MutableList<Tile> = farm.getFields().toMutableList()
                if (fields.contains(tile)) {
                    fields.remove(tile)
                    farm.setFields(fields)
                }
                if (farm.getPlantation().contains(tile)) {
                    val plantations: MutableList<Tile> = farm.getPlantation().toMutableList()
                    plantations.remove(tile)
                    farm.setPlantation(plantations)
                }
            }
        }
    }
}
