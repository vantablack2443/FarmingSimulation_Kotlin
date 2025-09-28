
package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test for basic cloud movement
 */
class CloudSystemTest : ExampleSystemTestExtension() {
    override val name = "ExampleTest"
    override val description = "Tests statistics after 0 ticks."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "cloud_basic/farms.json"
    override val scenario = "cloud_basic/scenario.json"
    override val map = "cloud_basic/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        return
    }
}
