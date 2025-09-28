package de.unisaarland.cs.se.selab.systemtest.selab25

// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BrokenScenarioTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestStuckCloud
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestThreeClouds
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestTwoClouds
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestWithMositure
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmNoPlantableTiles
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultAction
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultActionIncidents
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultActionOneMachine
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultActionParsing
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultActionSecond
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultActionSecondFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineWrongShed
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SimpleTestWithHarvestandNoCloudsOrIncidents
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTestCloudNotOnVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmandCloudCreationInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseandCloudCreationSimple
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseCloudCreationValidPlusMerge
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.NoFarmPhaseCloudCreationValidPlusMergeTestingOrder
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
        // testSuite.registerTest(SowingPlanFieldsDifferentFarms())
        testSuite.registerTest(FarmNoPlantableTiles())
        testSuite.registerTest(MachineWrongShed())
        testSuite.registerTest(MachineDefaultAction())
        testSuite.registerTest(CloudMovementTestStuckCloud())
        testSuite.registerTest(CloudMovementTestWithMositure())
        testSuite.registerTest(MachineDefaultActionParsing())
        testSuite.registerTest(MachineDefaultActionOneMachine())
        testSuite.registerTest(MachineDefaultActionSecond())
        testSuite.registerTest(MachineDefaultActionSecondFarm())
        testSuite.registerTest(MachineDefaultActionIncidents())
        testSuite.registerTest(CloudMovementTestCloudNotOnVillage())
        testSuite.registerTest(SimpleTestWithHarvestandNoCloudsOrIncidents())
        testSuite.registerTest(NoFarmandCloudCreationInvalid())
        testSuite.registerTest(NoFarmPhaseandCloudCreationSimple())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMerge())
        testSuite.registerTest(NoFarmPhaseCloudCreationValidPlusMergeTestingOrder())
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
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
    }
}
