package de.unisaarland.cs.se.selab.log

import de.unisaarland.cs.se.selab.enumerations.LogType
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.StringWriter
import kotlin.test.Test

class LoggerTest {

    private var buffer = StringWriter()
    private fun out(): String = buffer.toString().trim()

    @Test
    fun `no debug logs leak`() {
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logSunlight(69, 69)
        val output = out()
        assertEquals("", output, "DEBUG logs should not be printed at IMPORTANT level")
    }
}
