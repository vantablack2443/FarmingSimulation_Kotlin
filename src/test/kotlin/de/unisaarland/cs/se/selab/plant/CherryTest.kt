package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CherryTest {

    private lateinit var cherry: Cherry

    @BeforeEach
    fun setUp() {
        cherry = Cherry()
        cherry.resetHarvestEstimate()
        cherry.pollination.clear()
        cherry.animalAttackPenalty.clear()
        cherry.animalAttack = false
    }

    @Test
    fun testInitialValues() {
        assertEquals(1200000, cherry.harvestEstimate)
        assertFalse(cherry.animalAttack)
        assertEquals(120, cherry.neededSunlight)
        assertEquals(150, cherry.neededMoisture)
        assertEquals(emptyList<Double>(), cherry.pollination)
        assertEquals(emptyList<Double>(), cherry.animalAttackPenalty)
    }

    @Test
    fun testAnimalAttackPenalty() {
        cherry.animalAttackPenalty.add(0.9)
        cherry.animalAttackPenalty()
        assertEquals(floor(1200000 * 0.9).toInt(), cherry.harvestEstimate)
    }

    @Test
    fun testApplyPollinationBuff() {
        cherry.pollination.add(1.2)
        cherry.applyPollinationBuff()
        assertEquals(floor(1200000 * 1.2).toInt(), cherry.harvestEstimate)
    }

    @Test
    fun testIsBlooming() {
        assertTrue(cherry.isBlooming(8))
        assertTrue(cherry.isBlooming(9))
        assertFalse(cherry.isBlooming(7))
        assertFalse(cherry.isBlooming(10))
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        cherry.needsHarvesting(13, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        cherry.needsHarvesting(15, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        cherry.harvestEstimate = 0
        actions.clear()
        cherry.needsHarvesting(13, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        cherry.resetHarvestEstimate()
        val actedEnd = cherry.applyLateHarvestPenalty(14)
        assertEquals(floor(1200000 * 0.3).toInt(), cherry.harvestEstimate)
        assertTrue(actedEnd)

        cherry.resetHarvestEstimate()
        val actedLate = cherry.applyLateHarvestPenalty(15)
        assertEquals(0, cherry.harvestEstimate)
        assertTrue(actedLate)
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        cherry.filterHarvestingIfNotMissed(13, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetHarvestEstimate() {
        cherry.harvestEstimate = 5
        cherry.resetHarvestEstimate()
        assertEquals(1200000, cherry.harvestEstimate)
    }

    @Test
    fun testDoAnimalAttack() {
        assertFalse(cherry.animalAttack)
        assertTrue(cherry.animalAttackPenalty.isEmpty())

        cherry.doAnimalAttack()

        assertTrue(cherry.animalAttack)
        assertEquals(1, cherry.animalAttackPenalty.size)
        assertEquals(0.9, cherry.animalAttackPenalty.first())
    }

    @Test
    fun testDoBeeHappy() {
        assertTrue(cherry.pollination.isEmpty())

        cherry.doBeeHappy(1.25)

        assertEquals(1, cherry.pollination.size)
        assertEquals(1.25, cherry.pollination.first())
    }
}
