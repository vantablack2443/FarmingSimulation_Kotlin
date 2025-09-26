package de.unisaarland.cs.se.selab.log

import de.unisaarland.cs.se.selab.enumerations.IncidentType
import de.unisaarland.cs.se.selab.enumerations.LogType
import de.unisaarland.cs.se.selab.enumerations.PlantType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.test.Test

class LoggerTest {

    private lateinit var buffer: StringWriter
    private fun out(): String = buffer.toString().trim()

    @BeforeEach
    fun setUp() {
        buffer = StringWriter()
        Logger.printer = PrintWriter(buffer, true)
    }

    @AfterEach
    fun tearDown() {
        Logger.printer = PrintWriter(System.out, true)
    }

    @Test
    fun `DEBUG in IMPORTANT log level test`() {
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logSunlight(69, 69)
        val output = out()
        assertEquals("", output, "DEBUG logs should not be printed at IMPORTANT level")
    }

    @Test
    fun `INFO in IMPORTANT log level test`() {
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logCloudMove(69, 69, 69, 69)
        val output = out()
        assertEquals("", output, "INFO logs should not be printed at IMPORTANT level")
    }

    @Test
    fun `IMPORTANT in IMPORTANT log level test`() {
        Logger.setLevel(LogType.IMPORTANT)
        Logger.logFarmStart(69)
        val output = out()
        assertEquals(
            "[IMPORTANT] Farm: Farm 69 starts its actions.",
            output,
            "IMPORTANT logs should be printed at IMPORTANT level"
        )
    }

    @Test
    fun `DEBUG in DEBUG log level test`() {
        Logger.setLevel(LogType.DEBUG)
        Logger.logSunlight(69, 69)
        val output = out()
        assertEquals(
            "[DEBUG] Cloud Movement: On tile 69, the amount of sunlight is 69.",
            output,
            "DEBUG logs should be printed at DEBUG level"
        )
    }

    @Test
    fun `INFO in DEBUG log level test`() {
        Logger.setLevel(LogType.DEBUG)
        Logger.logHarvestEstimate(69, 69, PlantType.WHEAT)
        val output = out()
        assertEquals(
            "[INFO] Harvest Estimate: Harvest estimate on tile 69 changed to 69 g of WHEAT.",
            output,
            "INFO logs should be printed at DEBUG level"
        )
    }

    @Test
    fun `IMPORTANT in DEBUG log level test`() {
        Logger.setLevel(LogType.DEBUG)
        Logger.logIncident(69, IncidentType.DROUGHT, listOf(69, 420))
        val output = out()
        assertEquals(
            "[IMPORTANT] Incident: Incident 69 of type DROUGHT happened and affected tiles 69,420.",
            output,
            "IMPORTANT logs should be printed at DEBUG level"
        )
    }

    @Test
    fun `DEBUG in INFO log level test`() {
        Logger.setLevel(LogType.INFO)
        Logger.logFarmSowingPlan(69, listOf(69, 420))
        val output = out()
        assertEquals("", output, "DEBUG logs should not be printed at INFO level")
    }

    @Test
    fun `INFO in INFO log level test`() {
        Logger.setLevel(LogType.INFO)
        Logger.logCloudStuck(69, 420)
        val output = out()
        assertEquals(
            "[INFO] Cloud Dissipation: Cloud 69 got stuck on tile 420.",
            output,
            "INFO logs should be printed at INFO level"
        )
    }

    @Test
    fun `IMPORTANT in INFO log level test`() {
        Logger.setLevel(LogType.INFO)
        Logger.logMachineReturnFail(69)
        val output = out()
        assertEquals(
            "[IMPORTANT] Farm Machine: Machine 69 is finished but failed to return.",
            output,
            "IMPORTANT logs should be printed at INFO level"
        )
    }
}
