package de.unisaarland.cs.se.selab.log

import de.unisaarland.cs.se.selab.enumerations.LogType
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.StringWriter
import kotlin.test.Test

class LoggerTest {

    private var buffer = StringWriter()
    private fun out(): String = buffer.toString().trim()

    @Test
    fun `IMPORTANT log level tests`() {
        // no leaks for debug logs
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logSunlight(69, 69)
        var output = out()
        assertEquals("", output, "DEBUG logs should not be printed at IMPORTANT level")

        // no leaks for info logs
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logCloudMove(69, 69, 69, 69)
        output = out()
        assertEquals("", output, "INFO logs should not be printed at IMPORTANT level")

        // important logs printed
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logFarmStart(69)
        output = out()
        assertEquals(
            "[IMPORTANT] Farm: Farm 69 starts its actions.",
            output,
            "IMPORTANT logs should be printed at IMPORTANT level"
        )
    }

    @Test
    fun temp() {
        Logger.setLevel(LogType.DEBUG)
        Logger.logSunlight(69, 69)
        var output = out()
        assertEquals(
            "[DEBUG] Cloud Movement: On tile 69, the amount of sunlight is 69.",
            output,
            "DEBUG logs should not be printed at IMPORTANT level"
        )
    }
}
