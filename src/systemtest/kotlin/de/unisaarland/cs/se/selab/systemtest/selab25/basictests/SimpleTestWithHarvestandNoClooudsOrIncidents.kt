package de.unisaarland.cs.se.selab.systemtest.selab25.basictests
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
// class SimpleTestWithHarvestandNoCloudsOrIncidents : ExampleSystemTestExtension (){
//    override val name = "SimpleTestWithHarvestandNoCloudsOrIncidents"
//    override val description = "Scenario with only sowing in farm phase
//    and no clouds or incidents, short duration, i.e., 4,  for the machine so it should sow all three tiles "
//    override val farms = "SimpleHarvestNoCloudsNorIncidents/farm.json"
//
//    override val scenario = "SimpleHarvestNoCloudsNorIncidents/scenario.json"
//    override val map = "SimpleHarvestNoCloudsNorIncidents/map.json"
//
//    override val logLevel = "DEBUG"
//    override val maxTicks = 1
//    override val startYearTick = 19
//    override suspend fun run() {
//        val lineIterator = result().lines().iterator()
//        while (lineIterator.hasNext()) {
//            val currentLine = lineIterator.next()
//            assertNextLine(currentLine)
//        }
//    }
//    private fun result() : String {
//        return """
//
//    """.trimIndent()
//    }
// }
