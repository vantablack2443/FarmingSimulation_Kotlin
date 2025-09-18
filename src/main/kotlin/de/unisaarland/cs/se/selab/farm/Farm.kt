package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan

class Farm (id: Int, name : String, farmstead : List <Tile>, fields : List <Tile>,
            plantation : List<Tile>, machine : List<Machine>, sowingPlans : MutableMap<Int, List<SowingPlan>>,
            val harvestPerPlant: kotlin.collections.Map<PlantType, Int>

) {
}