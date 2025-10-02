package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.log.Logger
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
    lateinit var cloudHandler: CloudHandler

    override fun execute(simulationMap: SimulationMap, yearTick: Int) {
        this.tile.category = TileType.VILLAGE

        this.tile.currentCrop = null
        this.tile.airflow = null
        this.tile.direction = null
        this.tile.shed = null
        this.tile.possiblePlants = null
        this.tile.maxMoisture = null
        this.tile.currentMoisture = null
        this.tile.currentSunlight = 0
        this.tile.plant = null
        this.tile.plantationDamaged = null
        this.tile.fallowDuration = null

        /*
        * Change Farm and Machine
        * 1. Find the farm that owns the tile (using farmID)
        * 2. Remove the tile from the farm's fields or plantation list
        * 3. Check if any machines are currently on that tile and set them to stuck
         */
        changeFarmAndMachine(tile)
        this.tile.farmID = null
        val cloud = cloudHandler.getCloudByCoordinate(tile.location)
        logIncident(id, IncidentType.CITY_EXPANSION, listOf(tile.id))
        if (cloud != null) {
            Logger.logCloudStuck(cloud.id, tile.id)
            cloudHandler.coordinateToCloud.remove(cloud.location)
            cloudHandler.cloudsList.removeIf { it.id == cloud.id }
        }
    }

    private fun changeFarmAndMachine(tile: Tile) {
        val farmId = tile.farmID ?: return
        for (farm in farms) {
            if (farm.getId() == farmId) {
                removeTileFromFarm(tile, farm)
//                setMachinesStuckOnTile(tile, farm)
            }
        }
    }

    private fun removeTileFromFarm(tile: Tile, farm: Farm) {
        val fields = farm.getFields().toMutableList()
        if (fields.contains(tile)) {
            fields.remove(tile)
            farm.setFields(fields)
        }
//        val plantations = farm.getPlantation().toMutableList()
//        if (plantations.contains(tile)) {
//            plantations.remove(tile)
//            farm.setPlantation(plantations)
//        }
    }
}
//    private fun setMachinesStuckOnTile(tile: Tile, farm: Farm) {
//        for (machine in farm.getMachines()) {
//            if (machine.currentTile == tile) {
//                machine.isStuck = true
//            }
//        }
