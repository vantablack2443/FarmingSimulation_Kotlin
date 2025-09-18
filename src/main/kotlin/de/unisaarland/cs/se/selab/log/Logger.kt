package de.unisaarland.cs.se.selab.log

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.LogType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import de.unisaarland.cs.se.selab.tile.Tile
import java.io.PrintWriter

/**
 * SimulationLogger logs Simulation details
 */
object SimulationLogger {
    var printer: PrintWriter = PrintWriter(System.out)
    private var level: LogType = TODO()

    fun setPrinter(printer: PrintWriter) {
        this.printer = printer
    }
    fun setLevel(logType: LogType) {
        this.level = logType
    }

    fun logParsing(valid: Boolean, filename: String) {}

    fun logSimulationStart(yearTick: Int) {}

    /**
     * [IMPORTANT] Simulation Info: Simulation ended at tick $tick.
     */
    fun logSimulationEnd(tick: Int) {
        printer.println("[IMPORTANT] Simulation Info: Simulation ended at tick $tick.")
        printer.flush()
    }

    /**
     * [INFO] Simulation Info: Tick $tick started at tick $yearTick within the year.
     */
    fun logTickStart(tick: Int, yearTick: Int) {
        printer.println("[INFO] Simulation Info: Tick $tick started at tick $yearTick within the year.")
        printer.flush()
    }

    /**
     * logd the position of a cloud
     */
    fun logCloudPosition(cloudID: Int, tileID: Int, sunight: Int) {
        TODO()
    }

    /**
     * logs the start of a farm action phase
     */
    fun logFarmStart(farmID: Int) {
        TODO()
    }

    /**
     * logs farm sowing plans
     */
    fun logFarmSowingPlan(farmID: Int, sowingPlanIDs: List<Int>) {
        TODO()
    }

    /**
     * logs the actions of a farm
     */
    fun logFarmAction(machineID: Int, actionType: ActionType, tileID: Int, duration: Int) {
        TODO()
    }

    /**
     * logs the sowing action
     */
    fun logSowing(machineID: Int, plant: PlantType, sowingPlanID: Int) {
        TODO()
    }

    /**
     * logs the harvest action
     */
    fun logHarvest(machineID: Int, amount: Int, plant: PlantType) {
        TODO()
    }

    /**
     * logs when a machine gets stuck
     */
    fun logMachineReturnFail(machineID: Int) {
        TODO()
    }

    /**
     * logs the end of the use of a machine
     */
    fun logMachineFinish(machineID: Int, tileID: Int) {
        TODO()
    }

    /**
     * logs the harvest in the machine that is returned
     */
    fun logUnload(machineID: Int, amount: Int, plant: PlantType) {
        TODO()
    }

    /**
     * logs the end of a farm action phase
     */
    fun logFarmEnd(farmID: Int) {
        TODO()
    }

    /**
     * logs incidents
     */
    fun logIncident(id: Int, type: IncidentType, affectedTiles: List<Tile>) {
        TODO()
    }

    /**
     * logs missed actions
     */
    fun logMissedActions(tile: Int, actions: List<ActionType>) {
        TODO()
    }

    /**
     * logs the harvests estimate
     */
    fun logHarvestEstimate(tileId: Int, amount: Int, plant: PlantType) {
        TODO()
    }

    fun end() {
        printer.println("Simulation Info: Simulation statistics are calculated.")
    }
}

object EnvironmentLogger
