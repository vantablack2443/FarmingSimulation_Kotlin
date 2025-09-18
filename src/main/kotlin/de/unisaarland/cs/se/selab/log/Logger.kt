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
object Logger {
    var printer: PrintWriter = PrintWriter(System.out)
    private var level: LogType = LogType.INFO

    fun setPrinter(printer: PrintWriter) {
        this.printer = printer
    }

    fun setLevel(logType: LogType) {
        this.level = logType
    }

    private fun logs(logType: LogType, message: String) {
        val shouldLog = when (logType) {
            LogType.IMPORTANT -> true
            LogType.INFO -> level != LogType.IMPORTANT
            LogType.DEBUG -> level == LogType.DEBUG
        }

        if (shouldLog) {
            printer.println("[${logType.name}] $message")
            printer.flush()
        }
    }

    fun logParsing(valid: Boolean, filename: String) {
        val message = "Initialization Info: $filename successfully parsed and" +
            " validated. $valid"
        logs(LogType.INFO, message)
    }

    fun logSimulationStart(yearTick: Int) {
        val message = "Simulation Info: Simulation started at tick $yearTick within the" +
            " year."
        logs(LogType.INFO, message)
    }

    fun logSimulationEnd(tick: Int) {
        val message = "Simulation Info: Simulation ended at tick $tick."
        logs(LogType.IMPORTANT, message)
    }

    fun logTickStart(tick: Int, yearTick: Int) {
        val message = "Simulation Info: Tick $tick started at tick $yearTick within the year."
        logs(LogType.INFO, message)
    }

    fun logMoistureReduction(amountField: Int, amountPlantation: Int) {
        val message = "Soil Moisture: The soil moisture is below threshold in" +
            " $amountField FIELD and $amountPlantation PLANTATION tiles."
        logs(LogType.INFO, message)
    }

    fun logRain(cloudID: Int, tileID: Int, amount: Int) {
        val message = "Cloud Rain: Cloud $cloudID on tile $tileID rained down" +
            " $amount L water."
        logs(LogType.IMPORTANT, message)
    }

    fun logCloudMove(cloudID: Int, amount: Int, startTile: Int, endTile: Int) {
        val message = "Cloud Movement: Cloud $cloudID with $amount L water moved" +
            " from tile $startTile to tile $endTile."
        logs(LogType.INFO, message)
    }

    fun logSunlight(tileID: Int, sunlight: Int) {
        val message = "Cloud Movement: On tile $tileID, the amount of sunlight is" +
            " $sunlight."
        logs(LogType.DEBUG, message)
    }

    fun logCloudMerge(startCloudID: Int, endCloudID: Int, newID: Int, amount: Int, duration: Int, tileID: Int) {
        val message = "Cloud Union: Clouds $startCloudID and $endCloudID united to cloud $newID with" +
            " $amount L water and duration $duration on tile $tileID."
        logs(LogType.IMPORTANT, message)
    }

    fun logDissipation(cloudID: Int, tileID: Int) {
        val message = "Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID."
        logs(LogType.INFO, message)
    }

    fun logCloudStuck(cloudID: Int, tileID: Int) {
        val message = "Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID."
        logs(LogType.INFO, message)
    }

    fun logCloudPosition(cloudID: Int, tileID: Int, sunlight: Int) {
        val message = "Cloud Position: Cloud $cloudID is on tile $tileID, where the" +
            " amount of sunlight is $sunlight."
        logs(LogType.DEBUG, message)
    }

    fun logFarmStart(farmID: Int) {
        val message = "Farm: Farm $farmID starts its actions."
        logs(LogType.IMPORTANT, message)
    }

    fun logFarmSowingPlan(farmID: Int, sowingPlanIDs: List<Int>) {
        val message = "Farm: Farm $farmID has the following active sowing plans it" +
            " intends to pursue in this tick: $sowingPlanIDs."
        logs(LogType.DEBUG, message)
    }

    fun logFarmAction(machineID: Int, actionType: ActionType, tileID: Int, duration: Int) {
        val message = "Farm Action: Machine $machineID performs $actionType on" +
            " tile $tileID for $duration days."
        logs(LogType.IMPORTANT, message)
    }

    fun logSowing(machineID: Int, plant: PlantType, sowingPlanID: Int) {
        val message = "Farm Sowing: Machine $machineID has sowed $plant according" +
            " to sowing plan $sowingPlanID."
        logs(LogType.IMPORTANT, message)
    }

    fun logHarvest(machineID: Int, amount: Int, plant: PlantType) {
        val message = "Farm Harvest: Machine $machineID has collected $amount g of" +
            " $plant harvest."
        logs(LogType.IMPORTANT, message)
    }

    fun logMachineReturnFail(machineID: Int) {
        val message = "Farm Machine: Machine $machineID is finished but failed to return."
        logs(LogType.IMPORTANT, message)
    }

    fun logMachineFinish(machineID: Int, tileID: Int) {
        val message = "Farm Machine: Machine $machineID is finished and returns to" +
            " the shed at $tileID."
        logs(LogType.IMPORTANT, message)
    }

    fun logUnload(machineID: Int, amount: Int, plant: PlantType) {
        val message = "Farm Machine: Machine $machineID unloads $amount g of" +
            " $plant harvest in the shed."
        logs(LogType.IMPORTANT, message)
    }

    fun logFarmEnd(farmID: Int) {
        val message = "Farm: Farm $farmID finished its actions."
        logs(LogType.IMPORTANT, message)
    }

    fun logIncident(incidentID: Int, incidentType: IncidentType, tileIDs: List<Tile>) {
        val message = "Incident: Incident $incidentID of type $incidentType happened and affected tiles $tileIDs."
        logs(LogType.IMPORTANT, message)
    }

    fun logMissedActions(tileID: Int, actions: List<ActionType>) {
        val message = "Harvest Estimate: Required actions on tile $tileID were not performed: $actions."
        logs(LogType.DEBUG, message)
    }

    fun logHarvestEstimate(tileID: Int, amount: Int, plant: PlantType) {
        val message = "Harvest Estimate: Harvest estimate on tile $tileID changed to $amount g of $plant."
        logs(LogType.INFO, message)
    }

    fun end() {
        val message = "Simulation Info: Simulation statistics are calculated."
        logs(LogType.IMPORTANT, message)
    }
}
