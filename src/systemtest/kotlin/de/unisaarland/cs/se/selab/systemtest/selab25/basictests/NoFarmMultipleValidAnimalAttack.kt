package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * No farm phase and multiple animal attacks are valid, tesing if the tile would only be affected once
 */
class NoFarmMultipleValidAnimalAttack : ExampleSystemTestExtension() {
    override val name = "NoFarmMultipleValidAnimalAttack"
    override val description = "No farm phase and multiple animal attacks are valid," +
        " tesing if the tile would only be affected once"

    override val farms = "NoFarmMultipleValidAnimalAttack/farm.json"
    override val scenario = "NoFarmMultipleValidAnimalAttack/scenario.json"
    override val map = "NoFarmMultipleValidAnimalAttack/map.json"
    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val lines = result().lines()
        var afterMarker = false
        for (line in lines) {
            assertNextLine(line)
            if (line == "[IMPORTANT] Farm: Farm 1 finished its actions.") {
                afterMarker = true
            }
        }
    }

    private fun result(): String {
        return """

        """.trimIndent()
    }
}
