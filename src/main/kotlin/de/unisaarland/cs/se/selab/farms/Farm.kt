package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Map

class Farm (id: Int, name : String, farmstead : List <Tile>, fields : List <Tile>,
            plantation : List<Tile>, machine : List<Machine>, sowingPlans : MutableMap<Int, List<SowingPlan>>,
            harvestPerPlant : Map<PlantType, Int>
) {

}