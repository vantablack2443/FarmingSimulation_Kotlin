package de.unisaarland.cs.se.selab.actionHandlers

import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.plantdata.PlantData
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * This handler handles the harvesting part of the farm phase.
 */
class HarvestingHandler(simulationMap: SimulationMap, plantdata: PlantData) : ActionHandler(simulationMap, plantdata) {

    override fun startPhase(farm: Farm, yearTick: Int, simTick: Int) {
        // TODO
        return
    }

    override fun startPhase(
        farm: Farm,
        machine: Machine
    ) {
        // TODO
        return
    }

    override fun startPhase(
        farm: Farm,
        machine: Machine,
        yearTick: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun performAction(
        machine: Machine,
        tile: Tile
    ) {
        // TODO
        return
    }

    override fun performAction(
        machine: Machine,
        tile: Tile,
        yearTick: Int
    ) {
        TODO("Not yet implemented")
    }

    /** why was this deleted in actionhandler?
     * override fun getOperableTiles(
     *         farm: Farm,
     *         plant: PlantType
     *     ): List<Tile> {
     *         // TODO
     *         return listOf()
     *     }
     */

    override fun getOperableTiles(farm: Farm): List<Tile> {
        // TODO
        return emptyList()
    }

    override fun getOperableTiles(
        farm: Farm,
        plant: PlantType,
        tick: Int
    ): List<Tile> {
        // TODO
        return emptyList()
    }
}
