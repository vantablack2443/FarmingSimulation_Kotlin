package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.actionHandlers.ActionPhaseHandler
import de.unisaarland.cs.se.selab.actionHandlers.CuttingHandler
import de.unisaarland.cs.se.selab.actionHandlers.HarvestingHandler
import de.unisaarland.cs.se.selab.actionHandlers.IncidentHandler
import de.unisaarland.cs.se.selab.actionHandlers.MowingHandler
import de.unisaarland.cs.se.selab.actionHandlers.SowingHandler
import de.unisaarland.cs.se.selab.actionHandlers.WeedingHandler
import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.harvestestimatehandler.HarvestEstimateHandler
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.tile.Tile

const val MOISTURE_WITH_PLANT = 100
const val MOISTURE_WITHOUT_PLANT = 70
const val MAX_YEAR_TICK = 24


/**
 * simulation class
 */
class Simulation(var data: SimulationData, var maxTicks: Int, var currentYearTick: Int) {
    var currentTick: Int = 0
    var map = data.map
    private var cloudHandler = CloudHandler(map)
    init {
        var clouds = data.getClouds()
        var mapping = mutableMapOf<Coordinate, Cloud>()
        cloudHandler.setMaxCloudID(clouds.maxOf { it.id })
        for (cloud in clouds) {
            mapping[cloud.location] = cloud
        }
    }
    private var incidentHandler = IncidentHandler(this.map)
    init {
        var incidents = data.getIncidents()
        incidentHandler.setIncidents(incidents)
    }
    private var harvestEstimator = HarvestEstimateHandler(this.map)
    private var actionHandler = ActionPhaseHandler(data.getFarms())
    private var harvestPerPlant: MutableMap<PlantType, Int> = mutableMapOf()

    init {
        var plantData = actionHandler.getPlantData()
        var sowingHandler = SowingHandler(this.map, plantData)
        var harvestingHandler = HarvestingHandler(this.map, plantData)
        var mowingHandler = MowingHandler(this.map, plantData)
        var cuttingHandler = CuttingHandler(this.map, plantData)
        var weedingHandler = WeedingHandler(this.map, plantData)
        actionHandler.setSowingHandler(sowingHandler)
        actionHandler.setHarvestingHandler(harvestingHandler)
        actionHandler.setMowingHandler(mowingHandler)
        actionHandler.setCuttingHandler(cuttingHandler)
        actionHandler.setWeedingHandler(weedingHandler)
    }

    /**
     * runs the simulation
     */
    fun run() {
        while (startNextTick()) {
            updatePlantationHarvestEstimate()
            updateSunlight(this.currentYearTick)
            reduceMoisture()
            actionHandler.farmPhase(currentYearTick, currentTick)
            applyIncidents(this.currentYearTick)
            harvestEstimator.estimateHarvest()
        }
        terminate()
    }

    /**
     * checks if the next tick can be continued and if so, updates the ticks
     */
    private fun startNextTick(): Boolean {
        if (currentTick + 1 > maxTicks) {
            return false
        }
        if (currentYearTick + 1 > MAX_YEAR_TICK) {
            currentYearTick = 1
        } else {
            currentYearTick++
        }
        currentTick++
        Logger.logTickStart(currentTick, currentYearTick)
        return true
    }

    /**
     * update the sunlight amount ouf the tiles based on the current tick
     */
    private fun updateSunlight(currentYear: Int) {
        val sunlight = data.getSunlightAmount(currentYear)
        for (tile in map.getPlantableTiles()) {
            tile.setSunlight(sunlight)
        }
    }

    /**
     * reduces the moisture of plantable tiles at the beginning of tick
     */
    private fun reduceMoisture() {
        val plantables = map.getPlantableTiles()
        val fieldTiles = map.filterByType(TileType.FIELD, plantables)
        val plantations = map.filterByType(TileType.PLANTATION, plantables)
        val plantationCount = reduceMoisturePlantations(plantations)
        val fieldCount = reduceMoistureFields(fieldTiles)
        Logger.logMoistureReduction(fieldCount, plantationCount)
    }

    /**
     * helper function that updates the moisture in plantations
     * returns the amount of plantations that have moisture below threshold of the plants needed moisture
     */
    private fun reduceMoisturePlantations(plantations: List<Tile>): Int {
        var countPlantations = 0
        // update the moisture in plantations and check if it goes below the threshold for the plant
        for (tile in plantations) {
            tile.decreaseMoistureByAmount(MOISTURE_WITH_PLANT)
            val currentMoisture = tile.currentMoisture ?: continue
            val neededMoisture = tile.plant?.neededMoisture ?: continue
            if (currentMoisture < neededMoisture) countPlantations++
        }
        return countPlantations
    }

    private fun reduceMoistureFields(fields: List<Tile>): Int {
        var countFields = 0

        // update moisture in fields and check for threshold
        for (tile in fields) {
            val plant = tile.plant
            val moistureAmount = if (plant == null) MOISTURE_WITHOUT_PLANT else MOISTURE_WITH_PLANT
            tile.decreaseMoistureByAmount(moistureAmount)
            if (plant != null) {
                val currentMoisture = tile.currentMoisture ?: continue
                val neededMoisture = tile.plant?.neededMoisture ?: continue
                if (currentMoisture < neededMoisture) countFields++
            }
        }
        return countFields
    }

    /**
     * executes the incidents for the tick
     */
    private fun applyIncidents(yearTick: Int) {
        incidentHandler.updateIncidentsForTick(this.currentTick)
        incidentHandler.executeIncidents(yearTick)
    }

    private fun finalStatistics() {
        Logger.logStatistics()
        for (farm in data.getFarms()) {
            val totalHarvest = farm.calculateTotalHarvest()
            Logger.logTotalFarmHarvest(farm.getId(), totalHarvest)
        }
        calculateHarvestPerPlant()
        harvestPerPlant.forEach { (plant, amount) -> Logger.logHarvestPerPlant(plant, amount) }
        val remainingHarvest = calculateRemainingHarvest()
        Logger.logRemainingHarvest(remainingHarvest)
    }

    private fun terminate() {
        Logger.logSimulationEnd(this.currentTick)
        finalStatistics()
    }

    /**
     * resets the harvest estimate for plantations in early november
     */
    private fun updatePlantationHarvestEstimate() {
        if (this.currentYearTick == NOV_TICK) {
            val plantations = map.filterByType(TileType.PLANTATION, map.getPlantableTiles())
            for (tile in plantations) {
                val plant = tile.plant ?: continue
                plant.resetHarvestEstimate()
            }
        }
    }

    /**
     * updates the harvestPerPlant mapping at the end of the simulation
     */
    private fun calculateHarvestPerPlant() {
        for (farm in data.getFarms()) {
            for (plant in PlantType.entries) {
                val harvest = farm.calculatePlantHarvest(plant)
                this.harvestPerPlant[plant]?.plus(harvest)
            }
        }
    }

    /**
     * calculates the total harvest remaining after the simulation end
     */
    private fun calculateRemainingHarvest(): Int {
        var harvest = 0
        for (tile in map.getPlantableTiles()) {
            val plant = tile.plant ?: continue
            val amount = plant.harvestEstimate
            harvest += amount
        }
        return harvest
    }
}
