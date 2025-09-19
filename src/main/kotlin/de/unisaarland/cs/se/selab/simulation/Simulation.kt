package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.actionHandlers.ActionPhaseHandler
import de.unisaarland.cs.se.selab.actionHandlers.IncidentHandler
import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloudHandler.CloudHandler
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.harvestestimatehandler.HarvestEstimateHandler

class Simulation (data: SimulationData, var maxTicks: Int, var currentYearTick: Int) {
    var currentTick: Int = 0
    var map = data.map
    private var cloudHandler = CloudHandler(map)
    init {
        var clouds = data.clouds.values.toList()
        var mapping = mutableMapOf<Coordinate, Cloud>()
        cloudHandler.setMaxCloudID(clouds.maxOf { it.id })
        for (cloud in clouds) {
            mapping[cloud.location] = cloud
        }
    }
    private var incidentHandler = IncidentHandler(data, this.map)
    init {
        var incidents = data.incidents.values.toList()
        incidentHandler.setIncidents(incidents)
    }
    private var harvestEstimator = HarvestEstimateHandler(this.map)
    private var actionHandler = ActionPhaseHandler(data.farms.values.toList())

    fun run() {
        TODO()
    }

    private fun startNextTick(simTick: Int): Boolean {
        TODO()
    }

    private fun updateSunlight(currentYear: Int) {
        TODO()
    }

    private fun reduceMoisture() {
        TODO()
    }

    private fun applyIncidents(yearTick: Int) {
        TODO()
    }

    private fun finalStatistics() {
        TODO()
    }

    private fun terminate() {
        TODO()
    }

    private fun updatePlantationHarvestEstimate() {
        TODO()
    }



}
