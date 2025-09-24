package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.enumerations.TileType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.log.Logger
class HarvestingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int){
        val harvestablePlantTypes : List<PlantType> = this.plantdata.getHarvestablePlantTypes(yearTick)
        if(harvestablePlantTypes.isEmpty()){
            return
        }

//  get OperableTiles takes care of the prioritzation of the tiles-- changed the signature in the diagram
        val operableTiles = getOperableTiles(farm, harvestablePlantTypes)
        for( tile in operableTiles){
            val availableMachine = getAvailableMachine(farm, tile)
            if(availableMachine == null){
                return
            }
            operableTiles.remove(tile)
            doHarvest(farm,availableMachine, tile, yearTick)
            if(availableMachine.canPerform(){
                    continueAction(farm,availableMachine, simTick, tile)
                })
                this.simulationMap.findTargetShed(availableMachine, farm.getShedTiles(), true)
            farm.machineHashMap.remove(availableMachine.id)

        }
    }
    private fun continueAction(farm : Farm,machine : Machine, simTick : Int, tileToBaseOn : Tile){
        if(
            !machine.canPerform()

        )t{
            return
        }
        val toContinueOn =  this.simulationMap.getReachableTiles(machine, 2, true) .filter { it.currentCrop == tileToBaseOn.currentCrop}
        if(toContinueOn.isEmpty()) {
            return
        }
        for(tile in toContinueOn){
            if(this.simulationMap.isReachable(machine,tile)){
                doHarvest(farm,machine,tile)
                operableTiles.remove(tile)
                continueAction(farm, machine, simTick, tile )
            }

        }

    }
    private fun doHarvest(farm : Farm,machine: Machine, tile: Tile, yearTick: Int) : Unit{
        // logger.logFarmAction(machine.id, ActionType.HARVESTING, tile.id)
        machine.currentTile = tile
        machine.updateElapsedTime()
        machine.currentHarvest= (tile.currentCrop, tile.plant.harvestEtimate)
        tile.plant.harvestEstimate= 0
        if(tile.category == TileType.FIELD){
            tile.plant= null
        }
        farm.tileHashMap.add(tile.id)
        // not a normal setter because it takes the yearTick and base off the duration on that
        tile.setFallowDuration(yearTick)


    }

    private fun getAvailableMachine(farm : Farm, tile : Tile) : Machine?{

        val machines = mutableListOf<Machine>
        for (machine in farm.getMachines()){
            if( !machine.isStuck && machine.plants.contains(tile.currentCrop)&& machine.actions.contains(ActionType.HARVESTING))

                if(this.simulationMap.isReachable(machine, tile))
                    machines.add(machine)
        }

        val sortedMachines = machines.sortedWith(compareBy ({ it.duration}, {it.id}))
        return sortedMachines.firstOrNull()
    }

    private fun getOperableTiles(farm: Farm, harvestablePlantTiles : List<PlantType>) : MutableList<Tile>{
        val plantationTiles = farm.getPlantation().filter { tile -> harvestablePlantTiles.contains(tile.currentCrop)}
        val fieldTiles = farm.getFields().filter { tile -> harvestablePlantTiles.contains(tile.currentCrop)}
        return plantationTiles.sortedBy { it.id } + fieldTiles.sortedBy { it.id }
    }

}
