package de.unisaarland.cs.se.selab.plantdata

import de.unisaarland.cs.se.selab.enumerations.PlantType

/**
 * const for sowing ticks
 */
const val POTATO_SOWING_START = 7
const val POTATO_SOWING_END = 10
const val POTATO_SOWING_END_PLUS_LATE = 12

const val WHEAT_SOWING_START = 19
const val WHEAT_SOWING_END = 20
const val WHEAT_SOWING_END_PLUS_LATE = 22

const val OAT_SOWING_START = 6
const val OAT_SOWING_END = 6
const val OAT_SOWING_END_PLUS_LATE = 8

const val PUMPKIN_SOWING_START = 10
const val PUMPKIN_SOWING_END = 12
const val PUMPKIN_SOWING_END_PLUS_LATE = 14

/**
 * const for harvesting ticks
 */
// POTATO loses all harvest after 20
const val POTATO_HARVEST_START = 17
const val POTATO_HARVEST_END = 20
// const val POTATO_HARVEST_END_PLUS_LATE = 20
// late harvest not possible for potato

// WHEAT loses 20% harvest per tick in tick 14, 15
const val WHEAT_HARVEST_START = 11
const val WHEAT_HARVEST_END = 13
const val WHEAT_HARVEST_END_PLUS_LATE = 15

// OAT loses 20% harvest per tick in tick 17, 18
const val OAT_HARVEST_START = 13
const val OAT_HARVEST_END = 16
const val OAT_HARVEST_END_PLUS_LATE = 18

// PUMPKIN loses all harvest after 20
const val PUMPKIN_HARVEST_START = 17
const val PUMPKIN_HARVEST_END = 20
// const val PUMPKIN_HARVEST_END_PLUS_LATE = 20
// late harvest not possible for pumpkin

// APPLE loses 50% harvest in tick 19
const val APPLE_HARVEST_START = 17
const val APPLE_HARVEST_END = 19
const val APPLE_HARVEST_END_PLUS_LATE = 20

// ALMOND loses 10% harvest in tick 20
const val ALMOND_HARVEST_START = 16
const val ALMOND_HARVEST_END = 19
const val ALMOND_HARVEST_END_PLUS_LATE = 20

// CHERRY loses 30% harvest in tick 15
const val CHERRY_HARVEST_START = 13
const val CHERRY_HARVEST_END = 14
const val CHERRY_HARVEST_END_PLUS_LATE = 15

// GRAPE loses 5% harvest per tick in tick 18, 19, 20
const val GRAPE_HARVEST_START_END = 17
const val GRAPE_HARVEST_START_END_PLUS_LATE = 20

// harvest estimates
const val ALMOND_HARVEST = 800000
const val APPLE_HARVEST = 1700000
const val CHERRY_HARVEST = 1200000
const val GRAPE_HARVEST = 1200000
const val OAT_HARVEST = 1200000
const val POTATO_HARVEST = 1000000
const val PUMPKIN_HARVEST = 500000
const val WHEAT_HARVEST = 1500000

/**
 * class to find available actions for given tick
 */
class PlantData {
    val plantationHarvestEstimates: Map<PlantType, Int> = mapOf(
        PlantType.ALMOND to ALMOND_HARVEST,
        PlantType.APPLE to APPLE_HARVEST,
        PlantType.GRAPE to GRAPE_HARVEST,
        PlantType.CHERRY to CHERRY_HARVEST
    )
    val fieldHarvestingEstimates: Map<PlantType, Int> = mapOf(
        PlantType.OAT to OAT_HARVEST,
        PlantType.PUMPKIN to PUMPKIN_HARVEST,
        PlantType.POTATO to POTATO_HARVEST,
        PlantType.WHEAT to WHEAT_HARVEST,
    )
    val sowingTimes: Map<IntRange, PlantType> = mapOf(
        POTATO_SOWING_START..POTATO_SOWING_END_PLUS_LATE to PlantType.POTATO,
        WHEAT_SOWING_START..WHEAT_SOWING_END_PLUS_LATE to PlantType.WHEAT,
        OAT_SOWING_START..OAT_SOWING_END_PLUS_LATE to PlantType.OAT,
        PUMPKIN_SOWING_START..PUMPKIN_SOWING_END_PLUS_LATE to PlantType.PUMPKIN
    )

    val harvestingTimes: Map<IntRange, PlantType> = mapOf(
        POTATO_HARVEST_START..POTATO_HARVEST_END to PlantType.POTATO,
        WHEAT_HARVEST_START..WHEAT_HARVEST_END_PLUS_LATE to PlantType.WHEAT,
        OAT_HARVEST_START..OAT_HARVEST_END_PLUS_LATE to PlantType.OAT,
        PUMPKIN_HARVEST_START..PUMPKIN_HARVEST_END to PlantType.PUMPKIN,
        APPLE_HARVEST_START..APPLE_HARVEST_END_PLUS_LATE to PlantType.APPLE,
        ALMOND_HARVEST_START..ALMOND_HARVEST_END_PLUS_LATE to PlantType.ALMOND,
        CHERRY_HARVEST_START..CHERRY_HARVEST_END_PLUS_LATE to PlantType.CHERRY,
        GRAPE_HARVEST_START_END..GRAPE_HARVEST_START_END_PLUS_LATE to PlantType.GRAPE
    )

    /**
     * returns all sowable plant types in the given yeartick
     * @param yeartick the yeartick of the simulation
     */
    fun getSowablePlantTypes(yeartick: Int): List<PlantType> {
        return sowingTimes.filter { (range, _) -> yeartick in range }.map { (_, value) -> value }
    }

    /**
     * returns all harvestabble plant types in the given yeartick
     * @param yeartick the yeartick of the simulation
     */
    fun getHarvestablePlantTypes(yeartick: Int): List<PlantType> {
        return harvestingTimes.filter { (range, _) -> yeartick in range }.map { (_, value) -> value }
    }
}
