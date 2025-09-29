package de.unisaarland.cs.se.selab.systemtest.selab25

// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BrokenScenarioTest
// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SimpleTestWithHarvestandNoCloudsOrIncidents
// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestCloudNotOnVillage
// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ScnearioWIthCloudsAndIncident

import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.AnimalAttackOneEffect
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.EmptyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmNoPlantableTiles
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestMissingTilesInField
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmParserTestNoFieldInPlan
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.IrrigationLogic
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineCantReturnDueHarvest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultAction
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationMowingNext
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationSecondTick
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationTillSowing
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationTwoMachines
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationUntilSowing
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineIrrigationWaterNext
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineWrongShed
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MapParserTestFarmsteadAdjoinOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MapParserTestVillageAdjoinForest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MaxTickEndLogEquals
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MaxTickEndLogOneLess
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmAnimalAttackValidOnePlantation
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmAnimalAttackValidSimple
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmAnimallAttackInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmDroughtSimple
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmMultipleValidAnimalAttack
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseCloudCreationValidPlusMerge
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseCloudCreationValidPlusMergeTestingOrder
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseandCloudCreationSimple
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmandCloudCreationInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationHarvestMoistureHundred
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationHarvestMoistureSeventy
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationPlantsDefaultLogHarvestMiss
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationPlantsDefaultTickOneNoLog
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
        testSuite.registerTest(ExampleSystemTest())
        testSuite.registerTest(CloudMovementTest())
        testSuite.registerTest(IrrigationLogic())
        // testSuite.registerTest(SowingPlanFieldsDifferentFarms())
        // testSuite.registerTest(FarmNoPlantableTiles())
        // testSuite.registerTest(MachineWrongShed())
        testSuite.registerTest(MachineDefaultAction())
        testSuite.registerTest(MachineIrrigationTillSowing())
        testSuite.registerTest(MachineIrrigationUntilSowing())
        testSuite.registerTest(MachineIrrigationSecondTick())
        testSuite.registerTest(MachineIrrigationWaterNext())
        testSuite.registerTest(MachineIrrigationTwoMachines())
        testSuite.registerTest(MachineIrrigationMowingNext())
        testSuite.registerTest(MachineIrrigationTest())
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
        testSuite.registerTest(PlantationPlantsDefaultTickOneNoLog())
        testSuite.registerTest(PlantationPlantsDefaultLogHarvestMiss())

        testSuite.registerTest(MapParserTestFarmsteadAdjoinOtherFarm())
        testSuite.registerTest(FarmParserTestMissingTilesInField())
        testSuite.registerTest(MapParserTestVillageAdjoinForest())
        testSuite.registerTest(ScenarioParserTestCloudOnVillage())
        testSuite.registerTest(FarmParserTestNoFieldInPlan())

        testSuite.registerTest(EmptyTest())
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
    }
}
