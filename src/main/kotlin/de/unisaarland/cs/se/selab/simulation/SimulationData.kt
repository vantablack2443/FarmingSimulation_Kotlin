package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.machine.Machine
import de.unisaarland.cs.se.selab.map.SimulationMap
import de.unisaarland.cs.se.selab.sowingplan.SowingPlan
import de.unisaarland.cs.se.selab.tile.Tile

const val SUN_JAN_NOV = 98
const val SUN_FEB_OCT = 112
const val SUN_MARCH_SEP = 126
const val SUN_APR = 140
const val SUN_MAY_JUN_JUL = 168
const val SUN_AUG = 154
const val SUN_DEC = 84

const val FEB_TICK = 3
const val MARCH_TICK = 5
const val APR_TICK = 7
const val MAY_TICK = 9
const val JUN_TICK = 11
const val JUL_TICK = 13
const val AUG_TICK = 15
const val SEP_TICK = 17
const val OCT_TICK = 19
const val NOV_TICK = 21
const val DEC_TICK = 23

/**
 * simulation data class stores the necessary mappings
 */
class SimulationData {
    lateinit var map: SimulationMap // take a look at this later
    private var tiles: MutableMap<Int, Tile> = mutableMapOf()
    private var farms: MutableMap<Int, Farm> = mutableMapOf() // ID to Farm
    private var machines: MutableMap<Int, Machine> = mutableMapOf()
    private var incidents: MutableMap<Int, Incident> = mutableMapOf()
    private var clouds: MutableMap<Int, Cloud> = mutableMapOf()
    private var sowingPlans: MutableMap<Int, List<SowingPlan>> = mutableMapOf() // mapping tick
    private var sunlightData: MutableMap<Int, Int> = mutableMapOf() // tick to average value
    init {
        sunlightData[1] = SUN_JAN_NOV
        sunlightData[2] = SUN_JAN_NOV
        sunlightData[FEB_TICK] = SUN_FEB_OCT
        sunlightData[FEB_TICK + 1] = SUN_FEB_OCT
        sunlightData[MARCH_TICK] = SUN_MARCH_SEP
        sunlightData[MARCH_TICK + 1 ] = SUN_MARCH_SEP
        sunlightData[APR_TICK] = SUN_APR
        sunlightData[APR_TICK + 1] = SUN_APR
        sunlightData[MAY_TICK] = SUN_MAY_JUN_JUL
        sunlightData[MAY_TICK + 1] = SUN_MAY_JUN_JUL
        sunlightData[JUN_TICK] = SUN_MAY_JUN_JUL
        sunlightData[JUN_TICK + 1] = SUN_MAY_JUN_JUL
        sunlightData[JUL_TICK] = SUN_MAY_JUN_JUL
        sunlightData[JUL_TICK + 1] = SUN_MAY_JUN_JUL
        sunlightData[AUG_TICK] = SUN_AUG
        sunlightData[AUG_TICK + 1] = SUN_AUG
        sunlightData[SEP_TICK] = SUN_MARCH_SEP
        sunlightData[SEP_TICK + 1] = SUN_MARCH_SEP
        sunlightData[OCT_TICK] = SUN_FEB_OCT
        sunlightData[OCT_TICK + 1] = SUN_FEB_OCT
        sunlightData[NOV_TICK] = SUN_JAN_NOV
        sunlightData[NOV_TICK + 1] = SUN_JAN_NOV
        sunlightData[DEC_TICK] = SUN_DEC
        sunlightData[DEC_TICK + 1] = SUN_DEC
    }

    /**
     * setter for the simulation mapping
     */
    fun setMap(m: SimulationMap) {
        map = m
    }

    /**
     * setter for the ID to tile mapping
     */
    fun setTiles(tiles: MutableMap<Int, Tile>) {
        this.tiles = tiles
    }

    /**
     * setter for the ID to farm mapping
     */
    fun setFarms(farms: MutableMap<Int, Farm>) {
        this.farms = farms
    }

    /**
     * setter for the ID to machine mapping
     */
    fun setMachines(machines: MutableMap<Int, Machine>) {
        this.machines = machines
    }

    /**
     * setter for the TICK to Incident mapping
     */
    fun setIncidents(incidents: MutableMap<Int, Incident>) {
        this.incidents = incidents
    }

    /**
     * setter for ID to cloud mapping
     */
    fun setClouds(clouds: MutableMap<Int, Cloud>) {
        this.clouds = clouds
    }

    /**
     * setter for TICK to sowing plan mapping
     */
    fun setSowingPlans(plans: MutableMap<Int, List<SowingPlan>>) {
        this.sowingPlans = plans
    }

    /**
     * get the sunlight amount for the given tick
     */
    fun getSunlightAmount(currentYear: Int): Int {
        return sunlightData.getOrDefault(currentYear, 0)
    }

    /**
     * returns all the tiles sorted by the ascending ID
     */
    fun getTiles(): List<Tile> {
        return tiles.values.toList().sortedBy { it.id }
    }

    /**
     * returns the incidents sorted by ascending ID
     */
    fun getIncidents(): List<Incident> {
        return incidents.values.toList().sortedBy { it.id }
    }

    /**
     * returns the clouds listed by ascending ID
     */
    fun getClouds(): List<Cloud> {
        return clouds.values.toList().sortedBy { it.id }
    }

    /**
     * returns farms sorted by ID
     */
    fun getFarms(): List<Farm> {
        return this.farms.values.toList().sortedBy { it.getId() }
    }

    /**
     * returns sowing plans
     */
    fun getSowingPlans(): Map<Int, List<SowingPlan>> {
        return this.sowingPlans
    }

    /**
     * get tile by ID
     */
    fun getTileById(id: Int): Tile? {
        return tiles[id]
    }

    /**
     * get machine by ID
     */
    fun getMachineById(id: Int): Machine? {
        return machines[id]
    }
}
