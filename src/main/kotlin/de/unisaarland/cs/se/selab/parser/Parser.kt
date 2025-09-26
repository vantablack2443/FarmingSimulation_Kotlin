package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.simulation.SimulationData

/**
 * custom exception
 */
class ValidationException : Exception {
    var filePath: String? = null
    constructor(cause: Throwable, filePath: String) : super(cause) {
        this.filePath = filePath
    }

    // constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
    constructor() : super()
}

/**
 * custom parsing exception for when CLI commands are invalid
 */
class ParsingException : Exception {
    // constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
    constructor() : super()
}

/**
 * parser class; will be called from main to parse the three config files
 */
class Parser {
    private lateinit var simData: SimulationData

    /**
     * parse function that takes the config files as an argument
     * and parses them by calling the concrete parsers
     */
    fun parse(filenames: List<String>): SimulationData {
        val simData = SimulationData()
        this.simData = simData
        parseMap(filenames[0])
        parseFarm(filenames[1])
        parseScenario(filenames[2])
        return simData
    }
    private fun parseMap(mapFile: String) {
        val mapParser = MapParser(this.simData)
        mapParser.parse(mapFile)
    }
    private fun parseFarm(farmFile: String) {
        val farmParser = FarmParser(this.simData)
        farmParser.parse(farmFile)
    }

    private fun parseScenario(scenarioFile: String) {
        val scenarioParser = ScenarioParser(this.simData)
        scenarioParser.parse(scenarioFile)
    }
}
