package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import kotlin.math.floor
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PumpkinTest {
    private lateinit var pumpkin: Pumpkin

    @BeforeTest
    fun setUp() {
        pumpkin = Pumpkin()
        pumpkin.resetHarvestEstimate()
        pumpkin.pollination.clear()
        pumpkin.animalAttackPenalty.clear()
        pumpkin.animalAttack = false
        pumpkin.sownSimTick = 5
        pumpkin.sownYearTick = 12
    }

    @Test
    fun testInitialValues() {
        assertEquals(500000, pumpkin.harvestEstimate)
        assertFalse(pumpkin.animalAttack)
        assertEquals(120, pumpkin.neededSunlight)
        assertEquals(600, pumpkin.neededMoisture)
        assertEquals(emptyList<Double>(), pumpkin.pollination)
        assertEquals(emptyList<Double>(), pumpkin.animalAttackPenalty)
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        pumpkin.needsHarvesting(17, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        pumpkin.needsHarvesting(20, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        pumpkin.harvestEstimate = 0
        pumpkin.needsHarvesting(17, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testNeedsWeeding() {
        val actions = mutableListOf<ActionType>()

        // simTick = sownSimTick + 2 = 7 (even difference, weeding)
        pumpkin.needsWeeding(7, actions)
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        // simTick = sownSimTick + 3 = 8 (odd difference, no weeding)
        pumpkin.needsWeeding(8, actions)
        assertFalse(actions.contains(ActionType.WEEDING))

        actions.clear()
        // simTick = sownSimTick + 4 = 9 (even difference, weeding)
        pumpkin.needsWeeding(9, actions)
        assertTrue(actions.contains(ActionType.WEEDING))
    }

    @Test
    fun testIsBlooming() {
        // blooming between sownSimTick + 3 and sownSimTick + 4 => 8..9
        assertTrue(pumpkin.isBlooming(8))
        assertTrue(pumpkin.isBlooming(9))
        assertFalse(pumpkin.isBlooming(7))
        assertFalse(pumpkin.isBlooming(10))
    }

    @Test
    fun testPollinationBuff() {
        pumpkin.pollination.add(1.1)
        pumpkin.pollination.add(1.2)
        pumpkin.harvestEstimate = 500000

        pumpkin.applyPollinationBuff()
        // sequential application: 500000 * 1.1 = 550000, then 550000 * 1.2 = 660000
        val expected = floor(floor(500000 * 1.1) * 1.2).toInt()
        assertEquals(expected, pumpkin.harvestEstimate)
    }

    @Test
    fun testDoBeeHappyAddsPollination() {
        pumpkin.doBeeHappy(0.15)
        assertEquals(listOf(0.15), pumpkin.pollination)
    }

    @Test
    fun testCheckLateSowing() {
        val lateActions = mutableListOf<ActionType>()

        pumpkin.checkLateSowing(lateActions, 13)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        pumpkin.checkLateSowing(lateActions, 14)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        pumpkin.checkLateSowing(lateActions, 12)
        assertFalse(lateActions.contains(ActionType.SOWING))
    }

    @Test
    fun testApplyLateSowingPenalty() {
        pumpkin.sownYearTick = 14
        pumpkin.harvestEstimate = 500000

        pumpkin.applyLateSowingPenalty()
        // 20% penalty per delayed tick for 2 ticks late => 0.8 * 0.8 = 0.64
        val expected = floor(500000 * 0.64).toInt()
        assertEquals(expected, pumpkin.harvestEstimate)
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        pumpkin.resetHarvestEstimate()

        // at PUMPKIN_HARVEST_END, harvest estimate should set to 0, return true
        assertTrue(pumpkin.applyLateHarvestPenalty(20))
        assertEquals(0, pumpkin.harvestEstimate)

        // before harvest end, returns false
        pumpkin.resetHarvestEstimate()
        assertFalse(pumpkin.applyLateHarvestPenalty(19))
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        pumpkin.filterHarvestingIfNotMissed(17, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))

        val actions2 = mutableListOf(ActionType.HARVESTING)
        pumpkin.filterHarvestingIfNotMissed(20, actions2)
        // Should not remove harvesting action at PUMPKIN_HARVEST_END
        assertTrue(actions2.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetHarvestEstimate() {
        pumpkin.harvestEstimate = 5
        pumpkin.resetHarvestEstimate()
        assertEquals(500000, pumpkin.harvestEstimate)
    }
}
