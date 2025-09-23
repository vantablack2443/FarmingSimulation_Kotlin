package de.unisaarland.cs.se.selab.machine

import de.unisaarland.cs.se.selab.enumerations.PlantType

/**
 * custom class for a pair of plant type and harvested amount
 */
data class PlantAndHarvest(var plant: PlantType, var harvestAmount: Int)
