package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger.logIncident
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
        logIncident(id, IncidentType.CITY_EXPANSION, listOf(tile.id))
    }

    private fun removeFromFarms(tile: Tile) {
        val farmId = tile.farmID ?: return
        for (farm in farms) {
            if (farm.getId() == farmId) {
                removeTileFromFarm(tile, farm)
                setMachinesStuckOnTile(tile, farm)
            }
        }
    }

    private fun removeTileFromFarm(tile: Tile, farm: Farm) {
        val fields = farm.getFields().toMutableList()
        if (fields.contains(tile)) {
            fields.remove(tile)
            farm.setFields(fields)
        }
        val plantations = farm.getPlantation().toMutableList()
        if (plantations.contains(tile)) {
            plantations.remove(tile)
            farm.setPlantation(plantations)
        }
    }

    private fun setMachinesStuckOnTile(tile: Tile, farm: Farm) {
        for (machine in farm.getMachines()) {
            if (machine.currentTile == tile) {
                machine.isStuck = true
            }
        }
    }
}
