package de.unisaarland.cs.se.selab.Incidents
import de.unisaarland.cs.se.selab.Map

abstract class Incident (
    val id: Int,
    val tick: Int,
    val type: IncidentType
) {
    enum class IncidentType {
        ANIMAL_ATTACK, BEE_HAPPY, BROKEN_MACHINE, CLOUD_CREATION, CITY_EXPANSION, DROUGHT
    }

    abstract fun execute(map: Map, yearTick: Int)
}