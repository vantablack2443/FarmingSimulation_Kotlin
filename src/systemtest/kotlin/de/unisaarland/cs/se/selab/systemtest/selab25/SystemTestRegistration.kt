package de.unisaarland.cs.se.selab.systemtest.selab25

// import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BrokenScenarioTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudMovementTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MachineDefaultAction
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingPlanFieldsDifferentFarms
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingSameFarmTilesFalse
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ValidateMachineActionPlantTrue

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
        testSuite.registerTest(SowingPlanFieldsDifferentFarms())
        testSuite.registerTest(SowingSameFarmTilesFalse())
        testSuite.registerTest(SowingSameFarmTilesFalse())
        testSuite.registerTest(ValidateMachineActionPlantTrue())
        testSuite.registerTest(MachineDefaultAction())
    }

    /**
     * Register the tests you want to run against the validation mutants here!
     * The test only check validation, so they log messages will only possibly
     * be incorrect during the parsing/validation.
     * Everything after 'Simulation start' works correctly
     */
    fun registerSystemTestsMutantValidation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
    }
}
