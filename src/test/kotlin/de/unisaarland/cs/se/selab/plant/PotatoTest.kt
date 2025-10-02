package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PotatoTest {
    private lateinit var potato: Potato

    @BeforeEach
    fun setUp() {
        potato = Potato()
        potato.resetHarvestEstimate()
        potato.pollination.clear()
        potato.animalAttackPenalty.clear()
        potato.animalAttack = false
        potato.sownSimTick = 5
        potato.sownYearTick = 10 // reset to normal sowing time
    }

    @Test
    fun testInitialValues() {
        assertEquals(1000000, potato.harvestEstimate)
        assertFalse(potato.animalAttack)
        assertEquals(130, potato.neededSunlight)
        assertEquals(500, potato.neededMoisture)
        assertEquals(emptyList<Double>(), potato.pollination)
        assertEquals(emptyList<Double>(), potato.animalAttackPenalty)
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()
        potato.needsHarvesting(16, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))

        actions.clear()
        potato.needsHarvesting(17, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        potato.needsHarvesting(19, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        potato.needsHarvesting(20, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        potato.needsHarvesting(21, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))

        actions.clear()
        potato.harvestEstimate = 0
        potato.needsHarvesting(18, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testIsBlooming() {
        assertTrue(potato.isBlooming(9)) // sownSimTick + 4
        assertFalse(potato.isBlooming(8))
        assertFalse(potato.isBlooming(10))
    }

    @Test
    fun testNeedsWeeding() {
        val actions = mutableListOf<ActionType>()

        potato.needsWeeding(6, actions) // sownSimTick + 1
        assertFalse(actions.contains(ActionType.WEEDING))

        actions.clear()
        potato.needsWeeding(7, actions) // sownSimTick + 2
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        potato.needsWeeding(9, actions) // sownSimTick + 4
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        potato.needsWeeding(10, actions) // sownSimTick + 5
        assertFalse(actions.contains(ActionType.WEEDING))
    }

    @Test
    fun testCheckLateSowing() {
        val lateActions = mutableListOf<ActionType>()

        // late sowing 1 tick after sow end
        potato.checkLateSowing(lateActions, 11)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        // late sowing 2 ticks after sow end
        potato.checkLateSowing(lateActions, 12)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        // not late sowing
        potato.checkLateSowing(lateActions, 10)
        assertFalse(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        // early sowing
        potato.checkLateSowing(lateActions, 9)
        assertFalse(lateActions.contains(ActionType.SOWING))
    }

    @Test
    fun testApplyLateSowingPenalty() {
        potato.sownYearTick = 12
        potato.harvestEstimate = 1000000

        potato.applyLateSowingPenalty()
        // 20% penalty per delayed tick for 2 ticks late => 0.8 * 0.8 = 0.64
        val expected = floor(1000000 * 0.64).toInt()
        assertEquals(expected, potato.harvestEstimate)
    }

    @Test
    fun testApplyLateHarvestPenalty() {
        potato.resetHarvestEstimate()

        // not late, should return false
        assertFalse(potato.applyLateHarvestPenalty(17))

        potato.resetHarvestEstimate()
        // last harvest tick - apply penalty and set harvestEstimate to 0
        assertTrue(potato.applyLateHarvestPenalty(20))
        assertEquals(0, potato.harvestEstimate)
    }

    @Test
    fun testResetHarvestEstimate() {
        potato.harvestEstimate = 5
        potato.resetHarvestEstimate()
        assertEquals(1000000, potato.harvestEstimate)
    }
}
