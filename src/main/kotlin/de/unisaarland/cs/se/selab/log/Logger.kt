package de.unisaarland.cs.se.selab.log

import de.unisaarland.cs.se.selab.enumerations.LogType
import java.io.PrintWriter

/**
 * SimulationLogger logs Simulation details
 */
object Logger {
    var printer: PrintWriter = PrintWriter(System.out)
    private val level: LogType = TODO()

    fun setPrinter(printer: PrintWriter) {
        this.printer = printer
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


}

