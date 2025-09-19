package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.duration.Duration
import de.unisaarland.cs.se.selab.enumerations.ActionType

abstract class PlantationPlant(harvestEstimate: Int, neededMoisture: Int, harvestingTime: Duration,
                               bloomingTime: Duration?, pollination: Int, animalAttack: Boolean,
                               actionsNeeded: MutableList<ActionType>, lateActions: MutableList<ActionType>
) : Plant(harvestEstimate, neededMoisture,
    harvestingTime, bloomingTime, pollination, animalAttack, actionsNeeded, lateActions
) {
}