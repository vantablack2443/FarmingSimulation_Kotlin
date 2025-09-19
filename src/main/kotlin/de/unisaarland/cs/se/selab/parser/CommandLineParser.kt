package de.unisaarland.cs.se.selab.parser

/**
 * custom class for parsing the CLI commands
 */
class CommandLineParser {
    private data class Command(val name: String, val expected: Boolean, val required: Boolean)

    /**
     * Holds parsed results for each command
     */
    class ParsingResult {
        private val results = mutableMapOf<String, String>()

        /**
         * Gets the argument of the given command.
         * @param name name of the command
         * @return parsed argument of this command
         */
        operator fun get(name: String): String? {
            return results[name]
        }

        /**
         * Sets the argument of the given command.
         * @param name name of the command
         * @param argument value of the command
         */
        operator fun set(name: String, argument: String) {
            results[name] = argument
        }
    }

    private val commands: MutableList<Command> = mutableListOf()

    /**
     * Adds the compulsory command.
     * @param command command
     */
    fun required(command: String): CommandLineParser {
        commands.add(Command(command, expected = true, required = true))
        return this
    }

    /**
     * Adds the optional command.
     * @param command command
     * @param expected if a value for this argument should be expected
     */
    fun optional(command: String, expected: Boolean = true): CommandLineParser {
        commands.add(Command(command, expected, false))
        return this
    }

    /**
     * Parses the given array of arguments.
     * @throws ParsingException if there is an invalid usage of commands.
     */
    fun parse(args: Array<String>): ParsingResult {
        val result = ParsingResult()
        for ((i, arg) in args.withIndex()) {
            if (!arg.startsWith("--")) continue
            val name = arg.removePrefix("--")
            val command = commands.find { it.name == name }
                ?: throw ParsingException("Invalid command in command line")
            if (command.expected) {
                if (i + 1 >= args.size) {
                    throw ParsingException("Command $name needs a value")
                }
                result[name] = args[i + 1]
            } else {
                result[name] = ""
            }
        }
        val missingRequiredCommands = commands.filter { it.required && result[it.name] == null }
        if (missingRequiredCommands.isNotEmpty()) {
            val missingNames = missingRequiredCommands.joinToString { it.name }
            throw ParsingException("Missing required commands $missingNames")
        }
        return result
    }
}

/**
 * custom parsing exception for when CLI commands are invalid
 */
class ParsingException : Exception {
    var filePath: String? = null
    constructor(cause: Throwable, filePath: String) : super(cause) {
        this.filePath = filePath
    }

    // constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
    constructor() : super()
}
