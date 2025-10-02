package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.enumerations.ActionType
import kotlin.math.floor
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WheatTest {
    private lateinit var wheat: Wheat

    @BeforeTest
    fun setUp() {
        wheat = Wheat()
        wheat.resetHarvestEstimate()
        wheat.pollination.clear()
        wheat.animalAttackPenalty.clear()
        wheat.animalAttack = false
        wheat.sownSimTick = 5
        wheat.sownYearTick = 20
    }

    @Test
    fun testInitialValues() {
        assertEquals(1500000, wheat.harvestEstimate)
        assertFalse(wheat.animalAttack)
        assertEquals(90, wheat.neededSunlight)
        assertEquals(450, wheat.neededMoisture)
        assertEquals(emptyList<Double>(), wheat.pollination)
        assertEquals(emptyList<Double>(), wheat.animalAttackPenalty)
    }

    @Test
    fun testNeedsHarvesting() {
        val actions = mutableListOf<ActionType>()

        // During harvest period
        wheat.needsHarvesting(11, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        // During harvest period end
        wheat.needsHarvesting(13, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        // In late harvest period (up to 2 ticks after end)
        wheat.needsHarvesting(14, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        wheat.needsHarvesting(15, actions)
        assertTrue(actions.contains(ActionType.HARVESTING))

        actions.clear()
        // With zero harvest estimate no harvesting needed
        wheat.harvestEstimate = 0
        wheat.needsHarvesting(11, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))
    }

    @Test
    fun testNeedsWeeding() {
        val actions = mutableListOf<ActionType>()
        // Weeding at sownSimTick + WHEAT_WEED_START_OFFSET = 5 + 3 = 8
        wheat.needsWeeding(8, actions)
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        // Weeding at sownSimTick + WHEAT_WEED_END_OFFSET = 5 + 9 = 14
        wheat.needsWeeding(14, actions)
        assertTrue(actions.contains(ActionType.WEEDING))

        actions.clear()
        // No weeding at a random tick
        wheat.needsWeeding(10, actions)
        assertFalse(actions.contains(ActionType.WEEDING))
    }

    @Test
    fun testCheckLateSowing() {
        val lateActions = mutableListOf<ActionType>()

        wheat.checkLateSowing(lateActions, 21)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        wheat.checkLateSowing(lateActions, 22)
        assertTrue(lateActions.contains(ActionType.SOWING))

        lateActions.clear()
        wheat.checkLateSowing(lateActions, 20)
        assertFalse(lateActions.contains(ActionType.SOWING))
    }

    @Test
    fun testApplyLateSowingPenalty() {
        wheat.sownYearTick = 22
        wheat.harvestEstimate = 1500000

        wheat.applyLateSowingPenalty()
        // 20% penalty per delayed tick for 2 ticks late => 0.8 * 0.8 = 0.64
        val expected = floor(1500000 * 0.64).toInt()
        assertEquals(expected, wheat.harvestEstimate)
    }

    @Test
    fun testFilterHarvestingIfNotMissed() {
        val actions = mutableListOf(ActionType.HARVESTING)
        wheat.filterHarvestingIfNotMissed(11, actions)
        assertFalse(actions.contains(ActionType.HARVESTING))

        val actions2 = mutableListOf(ActionType.HARVESTING)
        wheat.filterHarvestingIfNotMissed(19, actions2)
        // Should not remove harvesting action at WHEAT_HARVEST_END
        assertTrue(actions2.contains(ActionType.HARVESTING))
    }

    @Test
    fun testResetHarvestEstimate() {
        wheat.harvestEstimate = 5
        wheat.resetHarvestEstimate()
        assertEquals(1500000, wheat.harvestEstimate)
    }
}
