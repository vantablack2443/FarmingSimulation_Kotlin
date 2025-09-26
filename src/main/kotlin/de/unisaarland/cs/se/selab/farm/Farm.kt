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
    private val farmsteads: List<Tile>,
    private val fields: MutableList<Tile>,
    private val plantation: MutableList<Tile>,
    private val machines: List<Machine>,
    private val sowingPlans: MutableMap<Int, MutableList<SowingPlan>>,
    private val harvestPerPlant: MutableMap<PlantType, Int>
) {

    val machineHashMap: HashSet<Int> = hashSetOf()
    val tileHashMap: HashSet<Int> = hashSetOf()

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
        return farmsteads
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
        return machines
    }

    /**
     * Returns sowing plans of the farm
     */
    fun getSowingPlans(): MutableMap<Int, MutableList<SowingPlan>> {
        return sowingPlans
    }

    /**
     * Returns the harvest per plant mapping of the farm
     */
    fun getHarvestPerPlant(): Map<PlantType, Int> {
        return harvestPerPlant
    }

    /**
     * Gets sowing plan by tick
     */
    fun getSowingPlansByTick(simTick: Int): List<SowingPlan> {
        return sowingPlans[simTick].orEmpty()
    }

    /**
     * removes the executed sowing plans from the farm's list of sowing plans
     */
    fun removeSowingPlans(executedPlans: List<SowingPlan>) {
        // TODO
        executedPlans.isEmpty()
    }

    /**
     * updates the actions needed list of the tiles in the farm for the current year tick
     */
    fun updateNeededActions(yearTick: Int, simTick: Int) {
        for (field in fields) {
            val plant = field.plant ?: continue
            plant.needsWeeding(simTick, field.actionsNeeded)
            plant.needsHarvesting(yearTick, field.actionsNeeded)
            field.needsIrrigation()
        }
        for (plantation in plantation) {
            val plant = plantation.plant ?: continue
            plant.needsCutting(yearTick, plantation.actionsNeeded)
            plant.needsMowing(yearTick, plantation.actionsNeeded)
            plant.needsHarvesting(yearTick, plantation.actionsNeeded)
            plantation.needsIrrigation()
        }
    }

    /**
     * returns the overall sum of the harvest
     */
    fun calculateTotalHarvest(): Int {
        var amount = harvestPerPlant.values.sum()
        for (machine in machines) {
            val currentHarvest = machine.currentHarvest ?: continue
            amount += currentHarvest.harvestAmount
        }
        return amount
    }

    /**
     * returns the harvest amount based on the plant type
     */
    fun calculatePlantHarvest(plant: PlantType): Int {
        var amount = harvestPerPlant.getOrDefault(plant, 0)
        for (machine in machines) {
            val currentHarvest = machine.currentHarvest ?: continue
            if (currentHarvest.plant != plant) continue
            amount += currentHarvest.harvestAmount
        }
        return amount
    }

    /**
     * sets the fields of the farm to the new list of fields
     */
    fun setFields(newFields: MutableList<Tile>) {
        this.fields.clear()
        this.fields.addAll(newFields)
    }

    /**
     * sets the plantation of the farm to the new list of plantations
     */
    fun setPlantation(newPlantation: MutableList<Tile>) {
        this.plantation.clear()
        this.plantation.addAll(newPlantation)
    }

//    /**
//     * returns a list of sheds on a farm
//     */
//    fun getShedTiles(): List<Tile> {
//        val sheds: MutableList<Tile> = mutableListOf()
//        for (farmstead in farmsteads) {
//            if (farmstead.shed == true) {
//                sheds.add(farmstead)
//            }
//        }
//        return sheds
//    }
}
