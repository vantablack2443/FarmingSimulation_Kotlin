package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppleTest {

    private lateinit var apple: Apple

    @BeforeEach
    fun setUp() {
        apple = Apple()
        apple.resetHarvestEstimate()
        apple.pollination.clear()
        apple.animalAttackPenalty.clear()
        apple.animalAttack = false
    }

    @Test
    fun testInitialValues() {
        assertEquals(1700000, apple.harvestEstimate)
        assertFalse(apple.animalAttack)
        assertEquals(50, apple.neededSunlight)
        assertEquals(100, apple.neededMoisture)
        assertEquals(emptyList<Double>(), apple.pollination)
        assertEquals(emptyList<Double>(), apple.animalAttackPenalty)
    }

    @Test
    fun testAnimalAttackPenalty() {
        apple.animalAttackPenalty.add(0.9)
        apple.animalAttackPenalty()
        assertEquals(floor(1700000 * 0.9).toInt(), apple.harvestEstimate)
    }

    @Test
    fun testApplyPollinationBuff() {
        apple.pollination.add(1.2)
        apple.applyPollinationBuff()
        assertEquals(floor(1700000 * 1.2).toInt(), apple.harvestEstimate)
    }

    @Test
    fun testIsBlooming() {
        assertTrue(apple.isBlooming(8))
        assertTrue(apple.isBlooming(9))
        assertFalse(apple.isBlooming(7))
        assertFalse(apple.isBlooming(10))
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        apple.needsHarvesting(17, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        apple.needsHarvesting(20, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        apple.harvestEstimate = 0
        actions.clear()
        apple.needsHarvesting(17, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        apple.resetHarvestEstimate()
        val actedEnd = apple.applyLateHarvestPenalty(19)
        assertEquals(floor(1700000 * 0.5).toInt(), apple.harvestEstimate)
        assertTrue(actedEnd)

        apple.resetHarvestEstimate()
        val actedLate = apple.applyLateHarvestPenalty(20)
        assertEquals(0, apple.harvestEstimate)
        assertTrue(actedLate)
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        apple.filterHarvestingIfNotMissed(17, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetHarvestEstimate() {
        apple.harvestEstimate = 5
        apple.resetHarvestEstimate()
        assertEquals(1700000, apple.harvestEstimate)
    }

    @Test
    fun testDoAnimalAttack() {
        assertFalse(apple.animalAttack)
        assertTrue(apple.animalAttackPenalty.isEmpty())

        apple.doAnimalAttack()

        assertTrue(apple.animalAttack)
        assertEquals(1, apple.animalAttackPenalty.size)
        assertEquals(0.9, apple.animalAttackPenalty.first())
    }

    @Test
    fun testDoBeeHappy() {
        assertTrue(apple.pollination.isEmpty())

        apple.doBeeHappy(1.25)

        assertEquals(1, apple.pollination.size)
        assertEquals(1.25, apple.pollination.first())
    }
}
