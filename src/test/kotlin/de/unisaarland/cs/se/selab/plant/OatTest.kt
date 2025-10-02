package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OatTest {
    private lateinit var oat: Oat

    @BeforeEach
    fun setUp() {
        oat = Oat()
        oat.resetHarvestEstimate()
        oat.pollination.clear()
        oat.animalAttackPenalty.clear()
        oat.animalAttack = false
        oat.sownSimTick = 5
        oat.sownYearTick = OAT_SOW_END // reset to normal sowing time
    }

    @Test
    fun testInitialValues() {
        assertEquals(1200000, oat.harvestEstimate)
        assertFalse(oat.animalAttack)
        assertEquals(90, oat.neededSunlight)
        assertEquals(300, oat.neededMoisture)
        assertEquals(emptyList<Double>(), oat.pollination)
        assertEquals(emptyList<Double>(), oat.animalAttackPenalty)
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        oat.needsHarvesting(13, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        oat.needsHarvesting(16, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        oat.needsHarvesting(17, actions) // in late harvest period
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        oat.harvestEstimate = 0
        oat.needsHarvesting(13, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testNeedsWeeding() {
        val actions = mutableListOf<ActionType>()

        // simTick = sownSimTick + 1 => 6
        oat.needsWeeding(6, actions)
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        oat.needsWeeding(7, actions) // sownSimTick + 2
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        oat.needsWeeding(8, actions) // sownSimTick + 3
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        oat.needsWeeding(9, actions) // outside weeding range
        assertFalse(actions.contains(ActionType.WEEDING))
    }

    @Test
    fun testCheckLateSowing() {
        val lateActions = mutableListOf<ActionType>()

        // late sowing 1 tick after sow end
        oat.checkLateSowing(lateActions, OAT_SOW_END + 1)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        // late sowing 2 ticks after sow end
        oat.checkLateSowing(lateActions, OAT_SOW_END + 2)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        // not late sowing
        oat.checkLateSowing(lateActions, OAT_SOW_END)
        assertFalse(lateActions.contains(ActionType.SOWING))
    }

    @Test
    fun testApplyLateSowingPenalty() {
        oat.sownYearTick = OAT_SOW_END + 2
        oat.harvestEstimate = 1200000

        oat.applyLateSowingPenalty()
        // 20% penalty per delayed tick for 2 ticks late => 0.8 * 0.8 = 0.64
        val expected = floor(1200000 * 0.64).toInt()
        assertEquals(expected, oat.harvestEstimate)
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        oat.resetHarvestEstimate()

        // not late, should return false
        assertFalse(oat.applyLateHarvestPenalty(OAT_HARVEST_START))

        oat.resetHarvestEstimate()
        // last harvest tick - apply 20% reduction
        assertTrue(oat.applyLateHarvestPenalty(OAT_HARVEST_END))
        assertEquals(floor(1200000 * OAT_LATE_HARVEST_PENALTY).toInt(), oat.harvestEstimate)

        oat.resetHarvestEstimate()
        // 1st late tick - apply 20% reduction again
        oat.applyLateHarvestPenalty(OAT_HARVEST_END)
        assertTrue(oat.applyLateHarvestPenalty(OAT_HARVEST_END + 1))
        val expectedAfterTwoReductions = floor(floor(1200000 * OAT_LATE_HARVEST_PENALTY) * OAT_LATE_HARVEST_PENALTY)
            .toInt()
        assertEquals(expectedAfterTwoReductions, oat.harvestEstimate)

        oat.resetHarvestEstimate()
        // 2nd late tick after harvest, kill plant (estimate = 0)
        assertTrue(oat.applyLateHarvestPenalty(OAT_HARVEST_END + 2))
        assertEquals(0, oat.harvestEstimate)
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        oat.filterHarvestingIfNotMissed(OAT_HARVEST_START, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))

        val actions2 = mutableListOf(ActionType.HARVESTING)
        oat.filterHarvestingIfNotMissed(OAT_HARVEST_END, actions2)
        // Should not remove harvesting action at OAT_HARVEST_END
        assertTrue(actions2.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetHarvestEstimate() {
        oat.harvestEstimate = 5
        oat.resetHarvestEstimate()
        assertEquals(1200000, oat.harvestEstimate)
    }
}
