package de.unisaarland.cs.se.selab.plantdata

import de.unisaarland.cs.se.selab.enumerations.PlantType

/**
 * class to store harvesting and sowing times of plants
 */
class PlantData(
    val sowingTimes: Map<PlantType, List<Int>>,
    val harvestingTimes: Map<PlantType, List<Int>>,
    val plantationHarvestEstimates: Map<PlantType, Int>,
    val fieldHarvestingEstimates: Map<PlantType, Int>,
) {
    /**
     * get sowable plant method
     */
    fun getSowablePlantTypes(yeartick: Int): List<PlantType> {
        TODO()
    }

    /**
     * get harvestable plant method
     */
    fun getHarvestablePlantTypes(yeartick: Int): List<PlantType> {
        TODO()
    }
}
