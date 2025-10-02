package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AlmondTest {

    private lateinit var almond: Almond

    @BeforeEach
    fun setUp() {
        almond = Almond()
        almond.resetHarvestEstimate()
        almond.pollination.clear()
        almond.animalAttackPenalty.clear()
        almond.animalAttack = false
    }

    @Test
    fun testInitialValues() {
        assertEquals(800000, almond.harvestEstimate)
        assertEquals(false, almond.animalAttack)
        assertEquals(130, almond.neededSunlight)
        assertEquals(400, almond.neededMoisture)
        assertEquals(emptyList<Double>(), almond.pollination)
        assertEquals(emptyList<Double>(), almond.animalAttackPenalty)
    }

    @Test
    fun testAnimalAttackPenalty() {
        almond.animalAttackPenalty.add(0.5)
        almond.animalAttackPenalty()
        assertEquals(400000, almond.harvestEstimate)
    }

    @Test
    fun testApplyPollinationBuff() {
        almond.pollination.add(1.2)
        almond.applyPollinationBuff()
        assertEquals(960000, almond.harvestEstimate)
    }

    @Test
    fun testIsBlooming() {
        assertTrue(almond.isBlooming(4))
        assertTrue(almond.isBlooming(5))
        assertFalse(almond.isBlooming(3))
        assertFalse(almond.isBlooming(6))
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        almond.needsHarvesting(16, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        almond.needsHarvesting(20, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        almond.harvestEstimate = 0
        actions.clear()
        almond.needsHarvesting(16, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        almond.resetHarvestEstimate()
        val actedEnd = almond.applyLateHarvestPenalty(19)
        assertEquals(
            floor(800000 * 0.9).toInt(),
            almond.harvestEstimate
        )
        assertTrue(actedEnd)

        almond.resetHarvestEstimate()
        val actedLate = almond.applyLateHarvestPenalty(20)
        assertEquals(0, almond.harvestEstimate)
        assertTrue(actedLate)
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        almond.filterHarvestingIfNotMissed(16, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetHarvestEstimate() {
        almond.harvestEstimate = 5
        almond.resetHarvestEstimate()
        assertEquals(800000, almond.harvestEstimate)
    }

    @Test
    fun testDoAnimalAttack() {
        // Initially, no penalty and not attacked
        assertFalse(almond.animalAttack)
        assertTrue(almond.animalAttackPenalty.isEmpty())

        // Call the function
        almond.doAnimalAttack()

        // Now animalAttack should be true, and a penalty value should be added
        assertTrue(almond.animalAttack)
        assertEquals(1, almond.animalAttackPenalty.size)
        assertEquals(ANIMAL_ATTACK_PENALTY, almond.animalAttackPenalty.first())
    }

    @Test
    fun testDoBeeHappy() {
        // Initially, no pollination buffs
        assertTrue(almond.pollination.isEmpty())

        // Add a pollination effect
        almond.doBeeHappy(1.25)

        // Now pollination should contain the effect
        assertEquals(1, almond.pollination.size)
        assertEquals(1.25, almond.pollination.first())
    }
}
