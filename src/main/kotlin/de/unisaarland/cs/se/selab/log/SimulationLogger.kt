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

    /**
     * intializes the printer
     */
    fun setPrinter(printer: PrintWriter) {
        this.printer = printer
    }

    /**
     * sets the log level
     */
    fun setLevel(logType: LogType) {
        this.level = logType
    }

    /**
     * logs if parsing was successful or not
     */
    fun logParsing(valid: Boolean, filename: String) {
        printer.println(
            "[INFO] Initialization Info: $filename successfully parsed and" +
                " validated. $valid"
            // not correct case not valid not yet added
        )
    }

    /**
     * logs the start of the simulation
     */
    fun logSimulationStart(yearTick: Int) {
        printer.println(
            "[INFO] Simulation Info: Simulation started at tick $yearTick within the" +
                " year."
        )
    }

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
     * logs the reduction of moisture
     */
    fun logMoistureReduction(amountField: Int, amountPlantation: Int) {
        printer.println(
            "[INFO] Soil Moisture: The soil moisture is below threshold in" +
                " $amountField FIELD and $amountPlantation PLANTATION tiles."
        )
    }

    /**
     * logs the rain
     */
    fun logRain(cloudID: Int, tileID: Int, amount: Int) {
        printer.println(
            "[IMPORTANT] Cloud Rain: Cloud $cloudID on tile $tileID rained down" +
                " $amount L water."
        )
    }

    /**
     * logs the cloud movement
     */
    fun logCloudMove(cloudID: Int, amount: Int, startTile: Int, endTile: Int) {
        // for cloud not originating from field and plantation
        printer.println(
            "[INFO] Cloud Movement: Cloud $cloudID with $amount L water moved" +
                " from tile $startTile to tile $endTile. "
        )
    }

    /**
     * logs the sunlight
     */
    fun logSunlight(tileID: Int, sunlight: Int) {
        printer.println(
            "[DEBUG] Cloud Movement: On tile $tileID, the amount of sunlight is" +
                " $sunlight."
        )
    }

    /**
     * logs when clouds merge
     */
    fun logCloudMerge(startCloudID: Int, endCloudID: Int, newID: Int, amount: Int, duration: Int, tileID: Int) {
        printer.println(
            "[IMPORTANT] Cloud Union: Clouds $startCloudID and" +
                "$endCloudID united to cloud $newID with $amount L water\n" +
                "and duration $duration on tile $tileID. "
        )
    }

    /**
     * logs the dissipation of a cloud
     */
    fun logDissipation(cloudID: Int, tileID: Int) {
        printer.println(
            "[INFO] Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID."
        )
    }

    /**
     * logs if the cloud got stuck
     */
    fun logCloudStuck(cloudID: Int, tileID: Int) {
        printer.println(
            "[INFO] Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID"
        )
    }

    /**
     * logd the position of a cloud
     */
    fun logCloudPosition(cloudID: Int, tileID: Int, sunlight: Int) {
        printer.println(
            "[DEBUG] Cloud Position: Cloud $cloudID is on tile $tileID, where the" +
                " amount of sunlight is $sunlight."
        )
    }

    /**
     * logs the start of a farm action phase
     */
    fun logFarmStart(farmID: Int) {
        printer.println("[IMPORTANT] Farm: Farm $farmID starts its actions.")
    }

    /**
     * logs farm sowing plans
     */
    fun logFarmSowingPlan(farmID: Int, sowingPlanIDs: List<Int>) {
        printer.println(
            "[DEBUG] Farm: Farm $farmID has the following active sowing plans it" +
                " intends to pursue in this tick: $sowingPlanIDs."
        )
    }

    /**
     * logs the actions of a farm
     */
    fun logFarmAction(machineID: Int, actionType: ActionType, tileID: Int, duration: Int) {
        printer.println(
            "[IMPORTANT] Farm Action: Machine $machineID performs $actionType on" +
                " tile $tileID for $duration days."
        )
    }

    /**
     * logs the sowing action
     */
    fun logSowing(machineID: Int, plant: PlantType, sowingPlanID: Int) {
        printer.println(
            "[IMPORTANT] Farm Sowing: Machine $machineID has sowed $plant according" +
                " to sowing plan $sowingPlanID."
        )
    }

    /**
     * logs the harvest action
     */
    fun logHarvest(machineID: Int, amount: Int, plant: PlantType) {
        printer.println(
            "[IMPORTANT] Farm Harvest: Machine $machineID has collected $amount g of" +
                " $plant harvest."
        )
    }

    /**
     * logs when a machine gets stuck
     */
    fun logMachineReturnFail(machineID: Int) {
        printer.println(
            "[IMPORTANT] Farm Machine: Machine $machineID is finished but failed to" +
                " return."
        )
    }

    /**
     * logs the end of the use of a machine
     */
    fun logMachineFinish(machineID: Int, tileID: Int) {
        printer.println(
            "[IMPORTANT] Farm Machine: Machine $machineID is finished and returns to" +
                " the shed at $tileID."
        )
    }

    /**
     * logs the harvest in the machine that is returned
     */
    fun logUnload(machineID: Int, amount: Int, plant: PlantType) {
        printer.println(
            "[IMPORTANT] Farm Machine: Machine $machineID unloads $amount g of" +
                " $plant harvest in the shed."
        )
    }

    /**
     * logs the end of a farm action phase
     */
    fun logFarmEnd(farmID: Int) {
        printer.println(
            "[IMPORTANT] Farm: Farm $farmID finished its actions."
        )
    }

    /**
     * logs incidents
     */
    fun logIncident(incidentID: Int, incidentType: IncidentType, tileIDs: List<Tile>) {
        printer.println(
            "[IMPORTANT] Incident: Incident $incidentID of type $incidentType" +
                " happened and affected tiles $tileIDs."
        )
    }

    /**
     * logs missed actions
     */
    fun logMissedActions(tileID: Int, actions: List<ActionType>) {
        printer.println(
            "[DEBUG] Harvest Estimate: Required actions on tile $tileID were not" +
                " performed: $actions."
        )
    }

    /**
     * logs the harvests estimate
     */
    fun logHarvestEstimate(tileID: Int, amount: Int, plant: PlantType) {
        printer.println(
            "[INFO] Harvest Estimate: Harvest estimate on tile $tileID changed to" +
                " $amount g of $plant."
        )
    }

    /**
     * logs farm statistics, plant statistics, remaining statistics
     */
    fun end() {
        printer.println("Simulation Info: Simulation statistics are calculated.")
    }
}
