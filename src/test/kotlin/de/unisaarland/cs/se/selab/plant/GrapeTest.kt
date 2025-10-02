package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GrapeTest {
    private lateinit var grape: Grape

    @BeforeEach
    fun setUp() {
        grape = Grape()
        grape.resetHarvestEstimate()
        grape.pollination.clear()
        grape.animalAttackPenalty.clear()
        grape.animalAttack = false
    }

    @Test
    fun testInitialValues() {
        assertEquals(1200000, grape.harvestEstimate)
        assertFalse(grape.animalAttack)
        assertEquals(150, grape.neededSunlight)
        assertEquals(250, grape.neededMoisture)
        assertEquals(emptyList<Double>(), grape.pollination)
        assertEquals(emptyList<Double>(), grape.animalAttackPenalty)
    }

    @Test
    fun testAnimalAttackPenalty() {
        grape.animalAttackPenalty.add(0.5)
        grape.animalAttackPenalty()
        assertEquals(floor(1200000 * 0.5).toInt(), grape.harvestEstimate)
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        grape.needsHarvesting(17, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        grape.needsHarvesting(18, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        grape.harvestEstimate = 0
        actions.clear()
        grape.needsHarvesting(17, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        grape.resetHarvestEstimate()
        val actedTick1 = grape.applyLateHarvestPenalty(17)
        assertEquals(floor(1200000 * 0.95).toInt(), grape.harvestEstimate)
        assertTrue(actedTick1)

        grape.resetHarvestEstimate()
        grape.applyLateHarvestPenalty(17)
        val actedTick2 = grape.applyLateHarvestPenalty(18)
        assertEquals(floor(floor(1200000 * 0.95) * 0.95).toInt(), grape.harvestEstimate)
        assertTrue(actedTick2)

        grape.resetHarvestEstimate()
        val actedEnd = grape.applyLateHarvestPenalty(20) // 3rd late tick
        assertEquals(0, grape.harvestEstimate)
        assertTrue(actedEnd)
    }

    @Test
    fun testResetHarvestEstimate() {
        grape.harvestEstimate = 5
        grape.resetHarvestEstimate()
        assertEquals(1200000, grape.harvestEstimate)
    }

    @Test
    fun testDoAnimalAttack() {
        assertFalse(grape.animalAttack)
        assertTrue(grape.animalAttackPenalty.isEmpty())

        grape.doAnimalAttack()

        assertTrue(grape.animalAttack)
        assertEquals(1, grape.animalAttackPenalty.size)
        assertEquals(0.5, grape.animalAttackPenalty.first())
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        grape.filterHarvestingIfNotMissed(17, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetMowingTime() {
        grape.resetMowingTime(0)
    }
}
