package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.AnotherWayHome
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.Apples
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BigScenarioFirst4Ticks
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BrokenMachineThenSow
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CasualCheckBehaviorTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CityExpansionDissipation
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CityExpansionOnTilesWithClouds
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CityExpansionSameTick
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudCreationThenCityExpansions
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CutAppleMissedAction3
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.DieAndComeBack
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.DroughtRainThenSow
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.DroughtSowExpand
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.DroughtThenSow
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.DroughtToExpansionValid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExpansionStuck
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmNoPlantableTiles
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestMissingTilesInField
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestNoFieldInPlan
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestPlanTileOnNextFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FullSimAAWithFieldLog
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FullSimTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FullSimTestUntilHarvesting
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.IncidentSpaceBetweenTiles
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.IrrigationLogic
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.JustMow
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.LogCherryDrought
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineBehaviorTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineCantReturnDueHarvest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultAction
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationSecondTick
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachinePriority
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineWrongShed
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MapParserTestFarmsteadAdjoinOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MapParserTestVillageAdjoinForest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MultipleBeeHappies
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmAnimallAttackInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseCloudCreationValidPlusMerge
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseCloudCreationValidPlusMergeTestingOrder
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseandCloudCreationSimple
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmandCloudCreationInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoWayHome
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.OatBehaviourTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.OatWheatYearTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationPlantsDefault
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationPlantsDefaultTickOne
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationPlantsDefaultTickOneWithoutLog
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationPlantsTickOneMachines
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationRegeneration
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PotatoBehaviourTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ScenarioParserTestCityExpnsion
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ScenarioParserTestCloudOnVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingAndDroughtInOneTick
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingPlanPrioritization
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingPlanSimplePlan
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.TwentyEightDays
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ValidatePlantCoupling

/**
 * Used for test registration
 */
object SystemTestRegistration {
    /**
     * Register your tests to run against the reference implementation!
     * This can also be used to debug our system test, or to see if we
     * understood something correctly or not (everything should work
     * the same as their reference implementation)
     */
    fun registerSystemTestsForReferenceImplementation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(BigScenarioFirst4Ticks())
        testSuite.registerTest(IrrigationLogic())
        testSuite.registerTest(DieAndComeBack())
        testSuite.registerTest(JustMow())
        testSuite.registerTest(ExpansionStuck())
        testSuite.registerTest(CutAppleMissedAction3())
        testSuite.registerTest(MachinePriority())
        testSuite.registerTest(DroughtToExpansionValid())
        testSuite.registerTest(LogCherryDrought())

        testSuite.registerTest(MachineIrrigationSecondTick())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(NoFarmPhaseandCloudCreationSimple())
        testSuite.registerTest(NoFarmandCloudCreationInvalid())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMergeTestingOrder())
        testSuite.registerTest(SowingPlanSimplePlan())
        testSuite.registerTest(MachineCantReturnDueHarvest())
        registerMoreTests(testSuite)
        registerAdditionalTests(testSuite)
    }
    private fun registerMoreTests(testSuite: SELab25TestSuite) {
        testSuite.run {
            testSuite.registerTest(PlantationPlantsDefault())
            testSuite.registerTest(PlantationPlantsDefaultTickOneWithoutLog())
            testSuite.registerTest(PlantationPlantsTickOneMachines())
            testSuite.registerTest(PlantationPlantsDefaultTickOne())
            testSuite.registerTest(PlantationPlantsDefault())
            testSuite.registerTest(MapParserTestFarmsteadAdjoinOtherFarm())
            testSuite.registerTest(FarmParserTestMissingTilesInField())
            testSuite.registerTest(MapParserTestVillageAdjoinForest())
            testSuite.registerTest(ScenarioParserTestCloudOnVillage())
            testSuite.registerTest(FarmParserTestNoFieldInPlan())
            testSuite.registerTest(CityExpansionDissipation())
            testSuite.registerTest(SowingPlanPrioritization())
            testSuite.registerTest(CasualCheckBehaviorTest())
            testSuite.registerTest(MachineBehaviorTest())
            testSuite.registerTest(FarmParserTestPlanTileOnNextFarm())
            testSuite.registerTest(ScenarioParserTestCityExpnsion())
            testSuite.registerTest(PotatoBehaviourTest())
            testSuite.registerTest(SowingAndDroughtInOneTick())
            testSuite.registerTest(IncidentSpaceBetweenTiles())
            testSuite.registerTest(FullSimTest())
            testSuite.registerTest(FullSimAAWithFieldLog())
            testSuite.registerTest(NoWayHome())
            testSuite.registerTest(FullSimTestUntilHarvesting())
        }
    }
    private fun registerAdditionalTests(testSuite: SELab25TestSuite) {
        testSuite.run {
            testSuite.registerTest(PlantationRegeneration())
            testSuite.registerTest(AnotherWayHome())
            testSuite.registerTest(BigScenarioFirst4Ticks())
            testSuite.registerTest(OatBehaviourTest())
            testSuite.registerTest(BrokenMachineThenSow())
            testSuite.registerTest(Apples())
            testSuite.registerTest(ValidatePlantCoupling())
            testSuite.registerTest(DroughtRainThenSow())
            testSuite.registerTest(DroughtThenSow())
            testSuite.registerTest(DroughtSowExpand())
            testSuite.registerTest(CityExpansionOnTilesWithClouds())
            testSuite.registerTest(CloudCreationThenCityExpansions())
        }
    }

    /**
     * Register the tests you want to run against the validation mutants here!
     * The test only check validation, so they log messages will only possibly
     * be incorrect during the parsing/validation.
     * Everything after 'Simulation start' works correctly
     */
    fun registerSystemTestsMutantValidation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
        testSuite.registerTest(FarmNoPlantableTiles())
        testSuite.registerTest(MachineWrongShed())

        testSuite.registerTest(MapParserTestFarmsteadAdjoinOtherFarm())
        testSuite.registerTest(FarmParserTestMissingTilesInField())
        testSuite.registerTest(MapParserTestVillageAdjoinForest())
        testSuite.registerTest(ScenarioParserTestCloudOnVillage())
        testSuite.registerTest(FarmParserTestNoFieldInPlan())
        testSuite.registerTest(MachineDefaultAction())
        testSuite.registerTest(NoFarmAnimallAttackInvalid())
        testSuite.registerTest(FarmParserTestPlanTileOnNextFarm())
        testSuite.registerTest(ScenarioParserTestCityExpnsion())
        testSuite.registerTest(CityExpansionSameTick())
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
        testSuite.registerTest(CloudMovementTest())
        testSuite.registerTest(CasualCheckBehaviorTest())
        testSuite.registerTest(ExpansionStuck())
        testSuite.registerTest(MachinePriority())
        testSuite.registerTest(CutAppleMissedAction3())
        testSuite.registerTest(MachineDefaultAction())
        testSuite.registerTest(SowingPlanPrioritization())
        testSuite.registerTest(PlantationPlantsTickOneMachines())
        testSuite.registerTest(PlantationPlantsDefault())
        testSuite.registerTest(PotatoBehaviourTest())
        testSuite.registerTest(DieAndComeBack())
        testSuite.registerTest(JustMow())
        testSuite.registerTest(MachineIrrigationSecondTick())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(SowingAndDroughtInOneTick())
        testSuite.registerTest(IncidentSpaceBetweenTiles())
        testSuite.registerTest(TwentyEightDays())
        testSuite.registerTest(NoFarmPhaseandCloudCreationSimple())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMergeTestingOrder())
        testSuite.registerTest(FullSimAAWithFieldLog())
        testSuite.registerTest(MultipleBeeHappies())
        testSuite.registerTest(SowingPlanSimplePlan())
        testSuite.registerTest(NoWayHome())
        testSuite.registerTest(AnotherWayHome())
        testSuite.registerTest(MachineBehaviorTest())
        testSuite.registerTest(FullSimTest())
        testSuite.registerTest(OatBehaviourTest())
        testSuite.registerTest(BigScenarioFirst4Ticks())
        testSuite.registerTest(Apples())
        testSuite.registerTest(OatWheatYearTest())
    }
}
