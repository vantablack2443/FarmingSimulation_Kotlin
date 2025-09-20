package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Farm class
 */
class Farm(
    private val id: Int,
    private val name: String,
    private val farmstead: List<Tile>,
    private val fields: List<Tile>,
    private val plantation: List<Tile>,
    private val machine: List<Machine>,
    private val sowingPlans: MutableMap<Int, List<SowingPlan>>,
    private val harvestPerPlant: kotlin.collections.Map<PlantType, Int>
) {

    val machineHashMap: MutableSet<Int> = mutableSetOf()
    val tileHashMap: MutableSet<Int> = mutableSetOf()

    /**
     * Getter methods for private attributes
     */
    fun getId(): Int {
        return id
    }

    /**
     * Returns name of the farm
     */
    fun getName(): String {
        return name
    }

    /**
     * Returns farmstead tiles of the farm
     */
    fun getFarmstead(): List<Tile> {
        return farmstead
    }

    /**
     * Returns field tiles of the farm
     */
    fun getFields(): List<Tile> {
        return fields
    }

    /**
     * Returns plantation tiles of the farm
     */
    fun getPlantation(): List<Tile> {
        return plantation
    }

    /**
     * Returns the machines of the farm
     */
    fun getMachines(): List<Machine> {
        return machine
    }

    /**
     * Returns sowing plans of the farm
     */
    fun getSowingPlans(): MutableMap<Int, List<SowingPlan>> {
        return sowingPlans
    }

    /**
     * Returns the harvest per plant mapping of the farm
     */
    fun getHarvestPerPlant(): Map<PlantType, Int> {
        return harvestPerPlant
    }

    fun getSowingPlansByTick(simTick: Int): List<SowingPlan> {
        // TODO
    }
}
