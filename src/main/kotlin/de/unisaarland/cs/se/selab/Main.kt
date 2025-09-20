package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.enumerations.LogType
import de.unisaarland.cs.se.selab.parser.CommandLineParser
import de.unisaarland.cs.se.selab.parser.Parser
import de.unisaarland.cs.se.selab.parser.ParsingException
import de.unisaarland.cs.se.selab.parser.ValidationException
import de.unisaarland.cs.se.selab.simulation.Simulation
import java.io.PrintWriter
import de.unisaarland.cs.se.selab.log.Logger as Logger

const val MAX_TICKS = 1000
const val MAX_YEAR_TICK = 24

/**
 Main Function
 **/
fun main(args: Array<String>) {
    val parser = CommandLineParser()
        .required("map")
        .required("farms")
        .required("scenario")
        .optional("start_year_tick")
        .required("max_ticks")
        .required("log_level")
        .optional("out")
        .optional("help", false)

    val result = parser.parse(args)

    val out = result["out"]
    if (out != null) {
        Logger.printer = PrintWriter(out)
    }

    if (result["help"] == "") {
        Logger.printer.println("help needed")
    }

    val maxTicks = validateMaxTicks(result)
    val startYearTick = validateYearTick(result)
    validateLogLevel(result)

    try {
        val parser = Parser()
        val simData = parser.parse(
            listOf(
                result["map"] ?: throw ParsingException(),
                result["farms"] ?: throw ParsingException(),
                result["scenario"] ?: throw ParsingException()
            )
        )
        val simulation = Simulation(simData, maxTicks, startYearTick)
        simulation.run()
    } catch (exception: ParsingException) {
        Logger.logInvalidFile(exception.filePath ?: "null file")
        Logger.printer.flush()
        return
    } catch (exception: ValidationException) {
        Logger.logParsing(false, exception.filePath ?: "null file")
        return
    }
    Logger.printer.flush()
}

/**
 * helper function to validate the parsed max ticks value
 */
private fun validateMaxTicks(result: CommandLineParser.ParsingResult): Int {
    val maxTicksRes = result["max_ticks"] ?: throw ParsingException()
    val maxTicks = maxTicksRes.toIntOrNull() ?: throw ParsingException("Max ticks must be an integer.")
    if (maxTicks > MAX_TICKS) throw ParsingException("Max ticks should not exceed 1000")
    return maxTicks
}

/**
 * helper function to validate the parsed year tick
 */
private fun validateYearTick(result: CommandLineParser.ParsingResult): Int {
    val startYearTickRes = result["start_year_tick"] ?: "1"
    val startYearTick = if (startYearTickRes.isNotEmpty()) startYearTickRes.toInt() else 1
    if (startYearTick > MAX_YEAR_TICK) throw ParsingException("Start year tick should not exceed 24")
    return startYearTick
}

/**
 * helper function to validate the parsed log level
 */
private fun validateLogLevel(result: CommandLineParser.ParsingResult) {
    val logType = result["log_level"] ?: throw ParsingException()
    if (logType.uppercase() !in setOf("DEBUG", "INFO", "IMPORTANT")) {
        throw ParsingException("Log level should be either DEBUG, INFO or IMPORTANT")
    }
    val logLevel = LogType.valueOf(logType)
    Logger.setLevel(logLevel)
}
