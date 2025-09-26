package de.unisaarland.cs.se.selab.log

import de.unisaarland.cs.se.selab.enumerations.ActionType
import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.LogType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import java.io.PrintWriter

/**
 * SimulationLogger logs Simulation details
 */
object Logger {
    var printer: PrintWriter = PrintWriter(System.out)
    private var level: LogType = LogType.INFO

    /**
     * Sets the output printer for the logger.
     * @param printer The PrintWriter to use for logging output.
     */
//    fun setPrinter(printer: PrintWriter) {
//        this.printer = printer
//    }

    /**
     * Sets the logging level.
     * @param logType The minimum LogType to log.
     */
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

    /**
     * Logs the successful result of parsing a file.
     * @param filename The name of the file parsed.
     */
    fun logParsing(filename: String) {
        val message = "Initialization Info: $filename successfully parsed and validated."
        logs(LogType.INFO, message)
    }

    /**
     *Logs if the file read is invalid
     */
    fun logInvalidFile(filename: String) {
        val message = "Initialization Info: $filename is invalid."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the start of the simulation.
     * @param yearTick The tick within the year when the simulation starts.
     */
    fun logSimulationStart(yearTick: Int) {
        val message = "Simulation Info: Simulation started at tick $yearTick within the year."
        logs(LogType.INFO, message)
    }

    /**
     * Logs the end of the simulation.
     * @param tick The tick at which the simulation ended.
     */
    fun logSimulationEnd(tick: Int) {
        val message = "Simulation Info: Simulation ended at tick $tick."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the start of a simulation tick.
     * @param tick The global tick number.
     * @param yearTick The tick within the year.
     */
    fun logTickStart(tick: Int, yearTick: Int) {
        val message = "Simulation Info: Tick $tick started at tick $yearTick within the year."
        logs(LogType.INFO, message)
    }

    /**
     * Logs a reduction in soil moisture.
     * @param amountField Number of FIELD tiles below threshold.
     * @param amountPlantation Number of PLANTATION tiles below threshold.
     */
    fun logMoistureReduction(amountField: Int, amountPlantation: Int) {
        val message = "Soil Moisture: The soil moisture is below threshold in" +
            " $amountField FIELD and $amountPlantation PLANTATION tiles."
        logs(LogType.INFO, message)
    }

    /**
     * Logs a rain event from a cloud.
     * @param cloudID The ID of the cloud.
     * @param tileID The ID of the tile.
     * @param amount The amount of rain in liters.
     */
    fun logRain(cloudID: Int, tileID: Int, amount: Int) {
        val message = "Cloud Rain: Cloud $cloudID on tile $tileID rained down" +
            " $amount L water."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the movement of a cloud.
     * @param cloudID The ID of the cloud.
     * @param amount The amount of water in the cloud.
     * @param startTile The starting tile ID.
     * @param endTile The ending tile ID.
     */
    fun logCloudMove(cloudID: Int, amount: Int, startTile: Int, endTile: Int) {
        val message = "Cloud Movement: Cloud $cloudID with $amount L water moved" +
            " from tile $startTile to tile $endTile."
        logs(LogType.INFO, message)
    }

    /**
     * Logs the amount of sunlight on a tile.
     * @param tileID The ID of the tile.
     * @param sunlight The amount of sunlight.
     */
    fun logSunlight(tileID: Int, sunlight: Int) {
        val message = "Cloud Movement: On tile $tileID, the amount of sunlight is" +
            " $sunlight."
        logs(LogType.DEBUG, message)
    }

    /**
     * Logs the merging of two clouds.
     * @param startCloudID The ID of the first cloud.
     * @param endCloudID The ID of the second cloud.
     * @param newID The ID of the new merged cloud.
     * @param amount The amount of water in the new cloud.
     * @param duration The duration of the new cloud.
     * @param tileID The tile where the merge occurred.
     */
    fun logCloudMerge(startCloudID: Int, endCloudID: Int, newID: Int, amount: Int, duration: Int, tileID: Int) {
        val message = "Cloud Union: Clouds $startCloudID and $endCloudID united to cloud $newID with" +
            " $amount L water and duration $duration on tile $tileID."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs when a cloud gets stuck on a tile.
     * @param cloudID The ID of the cloud.
     * @param tileID The tile where the cloud is stuck.
     */
    fun logCloudStuck(cloudID: Int, tileID: Int) {
        val message = "Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID."
        logs(LogType.INFO, message)
    }

    /**
     * Logs the dissipation of a cloud.
     * @param cloudID The ID of the cloud.
     * @param tileID The tile where the cloud dissipated.
     */
    fun logDissipation(cloudID: Int, tileID: Int) {
        val message = "Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID."
        logs(LogType.INFO, message)
    }

    /**
     * Logs the position and sunlight of a cloud on a tile.
     * @param cloudID The ID of the cloud.
     * @param tileID The tile ID.
     * @param sunlight The amount of sunlight.
     */
    fun logCloudPosition(cloudID: Int, tileID: Int, sunlight: Int) {
        val message = "Cloud Position: Cloud $cloudID is on tile $tileID, where the" +
            " amount of sunlight is $sunlight."
        logs(LogType.DEBUG, message)
    }

    /**
     * Logs the start of a farm's actions.
     * @param farmID The ID of the farm.
     */
    fun logFarmStart(farmID: Int) {
        val message = "Farm: Farm $farmID starts its actions."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the sowing plans a farm intends to pursue.
     * @param farmID The ID of the farm.
     * @param sowingPlanIDs The list of sowing plan IDs.
     */
    fun logFarmSowingPlan(farmID: Int, sowingPlanIDs: List<Int>) {
        val stringPlans = sowingPlanIDs.sorted().joinToString(separator = ",")

        val message = "Farm: Farm $farmID has the following active sowing plans it" +
            " intends to pursue in this tick: $stringPlans."
        logs(LogType.DEBUG, message)
    }

    /**
     * Logs a farm action performed by a machine.
     * @param machineID The ID of the machine.
     * @param actionType The type of action performed.
     * @param tileID The tile on which the action is performed.
     * @param duration The duration of the action.
     */
    fun logFarmAction(machineID: Int, actionType: ActionType, tileID: Int, duration: Int) {
        val message = "Farm Action: Machine $machineID performs $actionType on" +
            " tile $tileID for $duration days."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs a sowing event by a machine.
     * @param machineID The ID of the machine.
     * @param plant The type of plant sowed.
     * @param sowingPlanID The ID of the sowing plan.
     */
    fun logSowing(machineID: Int, plant: PlantType, sowingPlanID: Int) {
        val message = "Farm Sowing: Machine $machineID has sowed $plant according" +
            " to sowing plan $sowingPlanID."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs a harvest event by a machine.
     * @param machineID The ID of the machine.
     * @param amount The amount harvested in grams.
     * @param plant The type of plant harvested.
     */
    fun logHarvest(machineID: Int, amount: Int, plant: PlantType) {
        val message = "Farm Harvest: Machine $machineID has collected $amount g of" +
            " $plant harvest."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs when a machine fails to return to the shed.
     * @param machineID The ID of the machine.
     */
    fun logMachineReturnFail(machineID: Int) {
        val message = "Farm Machine: Machine $machineID is finished but failed to return."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs when a machine finishes and returns to the shed.
     */
    fun logMachineFinish(machineID: Int, tileID: Int) {
        val message = "Farm Machine: Machine $machineID is finished and returns to" +
            " the shed at $tileID."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs when a machine unloads harvest in the shed.
     * @param machineID The ID of the machine.
     * @param amount The amount unloaded in grams.
     * @param plant The type of plant unloaded.
     */
    fun logUnload(machineID: Int, amount: Int, plant: PlantType) {
        val message = "Farm Machine: Machine $machineID unloads $amount g of" +
            " $plant harvest in the shed."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the end of a farm's actions.
     * @param farmID The ID of the farm.
     */
    fun logFarmEnd(farmID: Int) {
        val message = "Farm: Farm $farmID finished its actions."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs an incident affecting tiles.
     * @param incidentID The ID of the incident.
     * @param incidentType The type of the incident.
     * @param tileIDs The list of affected tiles.
     */
    fun logIncident(incidentID: Int, incidentType: IncidentType, tileIDs: List<Int>) {
        val stringTiles = tileIDs.sorted().joinToString(separator = ",")

        val message = "Incident: Incident $incidentID of type $incidentType happened and affected tiles $stringTiles."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs missed actions on a tile.
     * @param tileID The ID of the tile.
     * @param actions The list of missed actions.
     */
    fun logMissedActions(tileID: Int, actions: List<ActionType>) {
        val actionsString = actions.sortedBy { it.name }.joinToString(separator = ",")

        val message = "Harvest Estimate: Required actions on tile $tileID were not performed: $actionsString."
        logs(LogType.DEBUG, message)
    }

    /**
     * Logs a harvest estimate for a tile.
     * @param tileID The ID of the tile.
     * @param amount The estimated amount in grams.
     * @param plant The type of plant.
     */
    fun logHarvestEstimate(tileID: Int, amount: Int, plant: PlantType) {
        val message = "Harvest Estimate: Harvest estimate on tile $tileID changed to $amount g of $plant."
        logs(LogType.INFO, message)
    }

    /**
     * Logs the end of the simulation and statistics calculation. THIS IS STILL INCOMPLETE, NEED
     * TO ADD STATISTICS. 3 LOGS WITHIN
     */
    fun logStatistics() {
        val message = "Simulation Info: Simulation statistics are calculated."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the total harvest of the farm
     */
    fun logTotalFarmHarvest(farmID: Int, amount: Int) {
        val message = "Simulation Statistics: Farm $farmID collected $amount g of harvest."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the total harvest of the given plant
     */
    fun logHarvestPerPlant(plant: PlantType, amount: Int) {
        val message = "Simulation Statistics: Total amount of $plant harvested: $amount g."
        logs(LogType.IMPORTANT, message)
    }

    /**
     * Logs the total harvest remaining after simulation end
     */
    fun logRemainingHarvest(amount: Int) {
        val message = "Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g."
        logs(LogType.IMPORTANT, message)
    }
}
