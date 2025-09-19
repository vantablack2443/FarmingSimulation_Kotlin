package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.coordinate.Coordinate
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.map.SimulationMap

class SimulationData {
    lateinit var map: SimulationMap // take a look at this later
    var tiles: MutableMap<Int, Tile> = mutableMapOf()
    var farms: MutableMap<Int, Farm> = mutableMapOf() // ID to Farm
    var machines: MutableMap<Int, Machine> = mutableMapOf()
    var incidents: MutableMap<Int, Incident> = mutableMapOf()
    var clouds: MutableMap<Int, Cloud> = mutableMapOf()
    var sowingPlans: MutableMap<Int, List<SowingPlan>> = mutableMapOf() // mapping tick
    var sunlightData: = mapOf<Int, Int> ()// tick to average value

    fun setMap(m: MutableMap<Coordinate, Tile>) {
        map = m
    }

    fun setTiles(tiles: MutableMap<Int, Tile>) {
        this.tiles = tiles
    }

    fun setFarms(farms: MutableMap<Int, Farm>) {
        this.farms = farms
    }

    fun setMachines(machines: MutableMap<Int, Machine>) {
        this.machines = machines
    }

    fun setIncidents(incidents: MutableMap<Int, Incident>) {
        this.incidents = incidents
    }

    fun setClouds(clouds: MutableMap<Int, Cloud>) {
        this.clouds = clouds
    }
    fun setSowingPlans(plans:  MutableMap<Int, List<SowingPlan>>) {
        this.sowingPlans = plans
    }
}

