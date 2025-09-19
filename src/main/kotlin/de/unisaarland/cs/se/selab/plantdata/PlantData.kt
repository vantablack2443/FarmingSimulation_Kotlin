package de.unisaarland.cs.se.selab.plantdata

import de.unisaarland.cs.se.selab.enumerations.PlantType

/**
 * const for sowing ticks
 */
const val POTATO_SOWING_START = 7
const val POTATO_SOWING_END = 10
const val WHEAT_SOWING_START = 19
const val WHEAT_SOWING_END = 20
const val OAT_SOWING_START = 6
const val OAT_SOWING_END = 6
const val PUMPKIN_SOWING_START = 10
const val PUMPKIN_SOWING_END = 12

/**
 * const for harvesting ticks
 */

/**
 * class to find available actions for given tick
 */
class PlantData(
    val harvestingTimes: Map<PlantType, List<Int>>,
    val plantationHarvestEstimates: Map<PlantType, Int>,
    val fieldHarvestingEstimates: Map<PlantType, Int>,
) {
    /**
     * objects for times
     */
    val sowingTimes: Map<IntRange, PlantType> = mapOf(
        POTATO_SOWING_START..POTATO_SOWING_END to PlantType.POTATO,
        WHEAT_SOWING_START..WHEAT_SOWING_END to PlantType.WHEAT,
        OAT_SOWING_START..OAT_SOWING_END to PlantType.OAT,
        PUMPKIN_SOWING_START..PUMPKIN_SOWING_END to PlantType.PUMPKIN
    )

    /**
     * returns all sowable plant types in the given yeartick
     * @param yeartick the yeartick of the simulation
     */
    fun getSowablePlantTypes(yeartick: Int): List<PlantType> {
        return sowingTimes.filter { (range, _) -> yeartick in range }.map { (_, value) -> value }
    }

    /**
     * get harvestable plant method
     */
    fun getHarvestablePlantTypes(yeartick: Int): List<PlantType> {
        TODO()
    }
}
