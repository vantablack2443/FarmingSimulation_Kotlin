package de.unisaarland.cs.se.selab.systemtest.selab25

// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BrokenScenarioTest
// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SimpleTestWithHarvestandNoCloudsOrIncidents
// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestCloudNotOnVillage
// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ScnearioWIthCloudsAndIncident

import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CasualCheckBehaviorTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmNoPlantableTiles
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestMissingTilesInField
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestNoFieldInPlan
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineBehaviorTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultAction
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineWrongShed
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MapParserTestFarmsteadAdjoinOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MapParserTestVillageAdjoinForest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ScenarioParserTestCloudOnVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingPlanSimplePlan
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
        /*
        testSuite.registerTest(ExampleSystemTest())
        testSuite.registerTest(CloudMovementTest())
        testSuite.registerTest(IrrigationLogic())
        // testSuite.registerTest(SowingPlanFieldsDifferentFarms())
        // testSuite.registerTest(FarmNoPlantableTiles())
        // testSuite.registerTest(MachineWrongShed())
        testSuite.registerTest(MachineDefaultAction())
        testSuite.registerTest(MachineIrrigationSecondTick())
        testSuite.registerTest(NoFarmDroughtSimple())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(NoFarmAnimallAttackInvalid())
//        testSuite.registerTest(ScnearioWIthCloudsAndIncident())
        testSuite.registerTest(NoFarmAnimalAttackValidSimple())
        testSuite.registerTest(NoFarmPhaseandCloudCreationSimple())
        testSuite.registerTest(NoFarmandCloudCreationInvalid())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMergeTestingOrder())
        testSuite.registerTest(NoFarmAnimalAttackValidOnePlantation())
        testSuite.registerTest(SowingPlanSimplePlan())
        testSuite.registerTest(MachineCantReturnDueHarvest())
        testSuite.registerTest(AnimalAttackOneEffect())
        // testSuite.registerTest(CloudMovementTestStuckCloud())

        testSuite.registerTest(MaxTickEndLogEquals())
        testSuite.registerTest(MaxTickEndLogOneLess())
        testSuite.registerTest(PlantationHarvestMoistureHundred())
        testSuite.registerTest(PlantationHarvestMoistureSeventy())
        testSuite.registerTest(NoFarmMultipleValidAnimalAttack())
        // testSuite.registerTest(PlantationPlantsDefault())
        testSuite.registerTest(PlantationPlantsDefaultTickOneWithoutLog())
        testSuite.registerTest(PlantationPlantsTickOneMachines())
        testSuite.registerTest(PlantationPlantsDefaultLogHarvestMiss())
        testSuite.registerTest(PlantationPlantsDefaultTickOne())

        testSuite.registerTest(MapParserTestFarmsteadAdjoinOtherFarm())
        testSuite.registerTest(FarmParserTestMissingTilesInField())
        testSuite.registerTest(MapParserTestVillageAdjoinForest())
        testSuite.registerTest(ScenarioParserTestCloudOnVillage())
        testSuite.registerTest(FarmParserTestNoFieldInPlan())

        testSuite.registerTest(EmptyTest())
        testSuite.registerTest(SowingPlanPrioritization())
        testSuite.registerTest(CasualCheckBehaviorTest())
         */
        testSuite.registerTest(MachineBehaviorTest())
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
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
        testSuite.registerTest(CloudMovementTest())
        testSuite.registerTest(SowingPlanSimplePlan())
        testSuite.registerTest(CasualCheckBehaviorTest())
        testSuite.registerTest(MachineDefaultAction())
        testSuite.registerTest(MachineBehaviorTest())
    }
}
