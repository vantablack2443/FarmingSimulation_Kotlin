package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.actionHandlers.ActionPhaseHandler
import de.unisaarland.cs.se.selab.actionHandlers.CuttingHandler
import de.unisaarland.cs.se.selab.actionHandlers.HarvestingHandler
import de.unisaarland.cs.se.selab.actionHandlers.IncidentHandler
import de.unisaarland.cs.se.selab.actionHandlers.IrrigationHandler
import de.unisaarland.cs.se.selab.actionHandlers.MowingHandler
import de.unisaarland.cs.se.selab.actionHandlers.SowingHandler
import de.unisaarland.cs.se.selab.actionHandlers.WeedingHandler
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.harvestestimatehandler.HarvestEstimateHandler
import de.unisaarland.cs.se.selab.incidents.CityExpansion
import de.unisaarland.cs.se.selab.incidents.CloudCreation
import de.unisaarland.cs.se.selab.log.Logger
import de.unisaarland.cs.se.selab.plant.ALMOND_LATE_HARVEST_PENALTY
import de.unisaarland.cs.se.selab.plant.APPLE_LATE_HARVEST_PENALTY
import de.unisaarland.cs.se.selab.plant.CHERRY_LATE_HARVEST_PENALTY
import de.unisaarland.cs.se.selab.plant.GRAPE_LATE_HARVEST_PENALTY
import de.unisaarland.cs.se.selab.plantdata.ALMOND_HARVEST
import de.unisaarland.cs.se.selab.plantdata.APPLE_HARVEST
import de.unisaarland.cs.se.selab.plantdata.CHERRY_HARVEST
import de.unisaarland.cs.se.selab.plantdata.CHERRY_HARVEST_END
import de.unisaarland.cs.se.selab.plantdata.GRAPE_HARVEST
import de.unisaarland.cs.se.selab.plantdata.GRAPE_HARVEST_START_END
import de.unisaarland.cs.se.selab.tile.Tile
import kotlin.math.floor

const val MOISTURE_WITH_PLANT = 100
const val MOISTURE_WITHOUT_PLANT = 70
const val MAX_YEAR_TICK = 24

/**
 * simulation class
 */
class Simulation(var data: SimulationData, var maxTicks: Int, var currentYearTick: Int) {
    var currentTick: Int = 0
    var map = data.map
    private val cloudHandler = CloudHandler(map)
    init {
        val clouds = data.getClouds().sortedBy { it.id }
        if (clouds.isEmpty()) {
            cloudHandler.setMaxCloudID(
                -1
            )
        } else { cloudHandler.setMaxCloudID(clouds.maxOf { it.id }) }
        for (cloud in clouds) {
            cloudHandler.addCloud(cloud)
        }
    }
    private val incidentHandler = IncidentHandler(this.map)
    init {
        val incidents = data.getIncidents()
        val cloudCreations = incidents.filter { it.type == IncidentType.CLOUD_CREATION }
        val cityExpansions = incidents.filter { it.type == IncidentType.CITY_EXPANSION }
        for (element in cloudCreations) {
            if (element is CloudCreation) element.cloudHandler = this.cloudHandler
        }
        for (element in cityExpansions) {
            if (element is CityExpansion) element.cloudHandler = this.cloudHandler
        }
        incidentHandler.incidents = incidents
    }
    private val harvestEstimator = HarvestEstimateHandler(this.map)
    private val actionHandler = ActionPhaseHandler(data.getFarms())
    private val harvestPerPlant: MutableMap<PlantType, Int> = mutableMapOf<PlantType, Int>()
    init {
        for (plantType in PlantType.entries) {
            harvestPerPlant[plantType] = 0
        }
    }

    init {
        val plantData = actionHandler.getPlantData()
        val sowingHandler = SowingHandler(this.map, plantData)
        val harvestingHandler = HarvestingHandler(this.map, plantData)
        val mowingHandler = MowingHandler(this.map, plantData)
        val cuttingHandler = CuttingHandler(this.map, plantData)
        val weedingHandler = WeedingHandler(this.map, plantData)
        val irrigationHandler = IrrigationHandler(this.map, plantData)
        actionHandler.setSowingHandler(sowingHandler)
        actionHandler.setHarvestingHandler(harvestingHandler)
        actionHandler.setMowingHandler(mowingHandler)
        actionHandler.setCuttingHandler(cuttingHandler)
        actionHandler.setWeedingHandler(weedingHandler)
        actionHandler.setIrrigationHandler(irrigationHandler)
    }

    /**
     * runs the simulation
     */
    fun run() {
        checkLateHarvestStart()
        while (canStartNextTick()) {
            Logger.logTickStart(currentTick, currentYearTick)
            updatePlantationHarvestEstimate()
            // updateLateHarvestPenalty()
            updateSunlight(this.currentYearTick)
            reduceMoisture()
            cloudHandler.moveClouds()
            actionHandler.farmPhase(currentYearTick, currentTick)
            applyIncidents(this.currentYearTick)
            harvestEstimator.estimateHarvest(currentYearTick, currentTick)
            updateTick()
        }
        terminate()
    }

    /**
     * checks if the next tick can be continued
     */
    private fun canStartNextTick(): Boolean {
        return currentTick + 1 <= this.maxTicks
    }

    /**
     * updates next tick
     */
    private fun updateTick() {
        currentTick++
        if (currentYearTick + 1 > MAX_YEAR_TICK) currentYearTick = 1 else currentYearTick++
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
            // If plantation is damaged (hit by drought), there are no plants growing on it
            val moistureAmount = if (tile.plant == null) MOISTURE_WITHOUT_PLANT else MOISTURE_WITH_PLANT
            tile.decreaseMoistureByAmount(moistureAmount)
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
    private fun applyIncidents(currentYearTick: Int) {
        incidentHandler.updateIncidentsForTick(this.currentTick)
        incidentHandler.executeIncidents(currentYearTick)
    }

    private fun finalStatistics() {
        Logger.logStatistics()
        for (farm in data.getFarms()) {
            val totalHarvest = farm.calculateTotalHarvest()
            Logger.logTotalFarmHarvest(farm.getId(), totalHarvest)
        }
        calculateHarvestPerPlant()
        // Log statistics for all plant types in enum order
        for (plant in PlantType.entries) {
            val amount = harvestPerPlant[plant] ?: 0
            Logger.logHarvestPerPlant(plant, amount)
        }
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
                plant.animalAttack = false
                plant.animalAttackPenalty.clear()
                for (element in plant.cuttingTime) {
                    element.second = false
                }
                for (element in plant.mowingTime) {
                    element.second = false
                }
            }
        }
    }

    /**
     * helper function to apply late harvest penalties at the start of the simulation
     */
    private fun checkLateHarvestStart() {
        val plantations = map.filterByType(TileType.PLANTATION, map.getPlantableTiles())
        // APPLE - harvest in 2nd October tick reduces by half, then lost
        // ALMOND - harvest in 2nd October tick reduces by 10%, then lost
        // CHERRY - harvest in 1st Aug tick reduces by 70%, then lost
        // GRAPE - harvesting from 2nd Sep tick reduces by 5% each tick for 3 ticks

        val applePlantations = plantations.filter { it.currentCrop == PlantType.APPLE }
        val almondPlantations = plantations.filter { it.currentCrop == PlantType.ALMOND }
        val grapePlantations = plantations.filter { it.currentCrop == PlantType.GRAPE }
        val cherryPlantations = plantations.filter { it.currentCrop == PlantType.CHERRY }
        appleAlmondPenalty(applePlantations, almondPlantations)
        cherryPenalty(cherryPlantations)
        grapePenalty(grapePlantations)
    }

    /**
     * helper function for apple and almond plantations late harvest
     */
    private fun appleAlmondPenalty(applePlantations: List<Tile>, almondPlantations: List<Tile>) {
        if (this.currentYearTick == OCT_TICK + 1) {
            applePlantations.forEach {
                it.plant?.harvestEstimate =
                    floor(APPLE_HARVEST * APPLE_LATE_HARVEST_PENALTY).toInt()
            }
            almondPlantations.forEach {
                it.plant?.harvestEstimate =
                    floor(ALMOND_HARVEST * ALMOND_LATE_HARVEST_PENALTY).toInt()
            }
        } else {
            applePlantations.forEach { it.plant?.harvestEstimate = APPLE_HARVEST }
            almondPlantations.forEach { it.plant?.harvestEstimate = ALMOND_HARVEST }
        }
    }

    /**
     * helper function for cherry plantations late harvest
     */
    private fun cherryPenalty(cherryPlantations: List<Tile>) {
        if (this.currentYearTick == CHERRY_HARVEST_END + 1) {
            cherryPlantations.forEach {
                it.plant?.harvestEstimate =
                    floor(CHERRY_HARVEST * CHERRY_LATE_HARVEST_PENALTY).toInt()
            }
        } else if (this.currentYearTick > CHERRY_HARVEST_END + 1 && this.currentYearTick < NOV_TICK) {
            cherryPlantations.forEach { it.plant?.harvestEstimate = 0 }
        } // Just Cherry has harvest estimate 0 between Aug tick and Nov tick
    }

    /**
     * helper function for grape plantations harvest
     */
    private fun grapePenalty(grapePlantations: List<Tile>) {
        val onePenaltyHarvest = floor(GRAPE_HARVEST * GRAPE_LATE_HARVEST_PENALTY).toInt()
        val twoPenaltyHarvest = floor(onePenaltyHarvest * GRAPE_LATE_HARVEST_PENALTY).toInt()
        when (this.currentYearTick) {
            GRAPE_HARVEST_START_END + 1 -> {
                grapePlantations.forEach {
                    it.plant?.harvestEstimate =
                        onePenaltyHarvest
                }
            }
            GRAPE_HARVEST_START_END + 2 -> {
                grapePlantations.forEach {
                    it.plant?.harvestEstimate =
                        twoPenaltyHarvest
                }
            }
            GRAPE_HARVEST_START_END + 3 -> {
                grapePlantations.forEach {
                    it.plant?.harvestEstimate =
                        floor(twoPenaltyHarvest * GRAPE_LATE_HARVEST_PENALTY).toInt()
                }
            }
            else -> {
                grapePlantations.forEach { it.plant?.harvestEstimate = GRAPE_HARVEST }
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
                this.harvestPerPlant[plant] = harvestPerPlant.getOrDefault(plant, 0) + harvest
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
