# Implementation Report

## Individual Contributions

The following table summarizes the actual contributions of each group member, including deviations from the planned responsibilities. 

| Member   | Implemented Components                                          | Testing Contributions                                               | Additional Work                                                        |
|----------|-----------------------------------------------------------------|---------------------------------------------------------------------|------------------------------------------------------------------------|
| Ana      | MapParser, CommandLineParser, Main, Simulation, CloudCreation   | Incidents, Cloud, Irrigation Unit test; System Tests for all phases |                                                                        |
| Thejitha | ScenarioParser, SowingHandler, CloudHandler                     | System tests for plantation plant actions, Unit Tests Coordinate    | Editing the ActionHandlers, Plant Classes, HarvestEstimationHandler    |
| Nehesh   | Plant classes, Animal Attack, Bee Happy, Map, IrrigationHandler | System tests for parsers and simulation logic                       | Bug fixes for cloud handler, harvest estimate handler, action handlers |
| Dipu     | WeedingHandler, CuttingHandler, CityExpansion, Logger           | System tests for validation and simulation phases                   |                                                                        |
| Mahmoud  | HarvestingHandler, BrokenMachine                                | Unit tests for map, mowing handler; System tests for all phases     |                                                                        |
| Kevin    | MowingHandler, PlantData, Map, Drought, CloudHandler            | Unit tests for sowing, logger; System tests for farm actions        |                                                                        |
| Kerem    | FarmParser, HarvestEstimateHandler, IrrigationHandler           | System tests for all phases except cloud movement                   |                                                                        |

---

## Adjustments from the Implementation Plan

- We encountered issues with the cloud handler logic implementation and this took more time than initially accounted for.
The task of fixing the logic was mostly reassigned to Thejitha, Kevin and Ana.
- Another issue encountered was the plant logic and their implementation scattered across many different classes, so
for redesigning these classes Thejitha and Nehesh took the responsibility.
- Certain attributes had to be adjusted to work with the harvest Estimating Handler.
- The remaining discrepancies between the initial and the final implementation plans stem from the time-management related 
redistribution of the workload.
---

## Detailed Timeline 

### Ana

- **Day 1 - Day 2:** Skeleton for parser and other classes, initial implementation of Parser, MapParser, CommandLineParser classes
- **Day 3:** Implementation of Simulation, Main and CloudCreation classes.
- **Day 4:** Parser fixes, implementation of plantation plant classes.
- **Day 5:** Incidents unit test.
- **Day 6:** Cloud Movement System test, fixes to the cloud handler.
- **Day 7:** Fixes to the parser and cloud handler, validation tests.
- **Day 8:** Harvest estimation test, bug fixes.
- **Day 9 - Day 11:** Additional system tests, debugging and bug fixes.

### Thejitha
- **Day 1:** Added skeleton files, implemented Scenario Parser
- **Day 2:** Implementing Scenario Parser, Functions added to Plant, Farm, actionHandler. Implemented SowingHandler
- **Day 3:** First version of CloudHandler . Fixes to scenario parser, Improving Field Plants, minor fixes to SowingHandler
- **Day 4:** Adding functions to tile and harvestEstimateHandler, Fixes to Plantations and Fields
- **Day 5:** Farm Parser edits, initial tests, Fixes to farm Parser
- **Day 6:** Redesign Plantation and Field Plants, changing HarvestEstimateHandler, changes to sowingHandler, farmParser, Farm
- **Day 7:** Edits to actionPhaseHandler, SowingHandler, ActionPhaseHandler, SimulationMap fixes
- **Day 8:** Redesign of harvestEstimateHandler, fixes to farm and sowingHandler
- **Day 9:** Adjusting Plants to work with harvestEstimateHandler, small fixes to farm
- **Day 10:** HarvestEstimateHandler, fixes irrigationHandler, scenarioParser, sowingHandler
- **Day 11:** Tests, HarvestEstimateHandler, Unit Testing Coordinate, System tests for plantation plants

### Nehesh
- **Day 1:** Added skeleton files, implemented parts of Map
- **Day 2:** Completed Map, implemented machine data class
- **Day 3:** Implemented Animal Attack and bee happy, implemented plant data classes
- **Day 4:** Bug fixed in Broken Machine, Farm, Implemented basis of action phase handler
- **Day 5:** Tried schema validation approach for parsers, bugfixes in tiles and incidents
- **Day 6:** Bug fixes in map class and other files
- **Day 7:** started writing unit and parser tests for parsers
- **Day 8:** Wrote system tests and bug fixes in scenario parser
- **Day 9:** Wrote more system tests and bug fixes in action handlers and fixed incorrect tests
- **Day 10:** Bug fixes in irrigation handler, estimate handler, animal attack and bee happy and wrote system tests
- **Day 11:** Final bug fixes to harvest estimation handler, cloud handler, plantdata, and other files. Wrote system tests


### Dipu
- **Day 1:** Created the skeleton files, and started implementing the Logger.
- **Day 2:** Finished and polished logger, started with testing the map.
- **Day 3:** Started the logic for Weeding and Cutting handlers.
- **Day 4:** Wrote tests for the Parser and bug fixes.
- **Day 4:** Finished weeding handler, and cutting handler. Checked bug fixes in the other Handlers and Parser.
- **Day 5:** Finished city expansion, started with writing system tests for validation phase.
- **Day 6:** Continued writing system tests for validation phase, started writing system tests for simulation phase.
- **Day 7:** Continued writing system tests for simulation phase. Bug fixes in the Handlers and Parser.
- **Day 8:** Worked on the Action Phase Handler. Changed logic.
- **Day 10:** Wrote more system tests for Incidents and checking out edge cases.
- **Day 11:** Final day continuous work with group to figure out the bugs and finalize the project.

### Mahmoud
- **Day 1:** started directly with testing as we were told to split up and do testing and implementation in parallel
- **Day 2:** continued with testing, worked a bit on simulation map tests
- **Day 3:** started working on harvesting handler and broken machine
- **Day 4:** continue working on harvesting handler and broken machine and fixed errors with the unit tests I wrote earlier
- **Day 5:** continued working on harvesting handler and broken machine but also spent some time off because I had an exam the day after that
- **Day 6:** it was our day off and I had an exam in the morning and had to continue looking for apartments as my rent contract was about to end
- **Day 7:** finished harvesting handler and broken machine and started writing system tests
- **Day 8:** continued writing system tests and fixing bugs that were found during testing but also had to stop a bit because I was moving out between apartments
- **Day 9:** code reviewed some classes while working on some big scenarios for system testing to catch more bugs in our implementation  
- **Day 10:** wrote tests specifically for incidents and clouds because we weren't passing all their tests
- **Day 11:** general code reviewing and debugging

### Kevin
- **Day 1:** Added skeleton classes for plants, logger and implemented half of simulationMap
- **Day 2:** Added unit tests for logger, implemented PlantData
- **Day 3:** Added the implementation of Mowing Handler, Drought incident and half of Tile
- **Day 4:** Added missing logging on sowing handler, fixed the behaviour of machine in sowing handler
- **Day 5:** Started revising old cloud handler
- **Day 6:** Finished cloud handler revision, note : the revised handler is added on a different file "cloudHandlerAlt" commit : 4095ad90
- **Day 7:** Focused on fixing small bugs mainly on weeding, irrigating and mowing, fixed big bugs on harvesting handler
- **Day 8:** Added system test "sowingPlanSimplePlan", "sowingPrioritizationTest"
- **Day 9:** Added system test "potatoBehaviourTest"
- **Day 10:** Added system tests "noWayHome, AnotherWayHome, oatBehaviourTest", a few validation system tests
- **Day 11:** Added more validation system tests, Unit tests for SowingHandler, mapParser, farmParser, harvestEstimateHandler

### Kerem
- **Day 1:** Writing the skeletons for MapParser & FarmParser, implemented most of FarmParser.
- **Day 2:** Finished FarmParser, added skeletons for Farm, Machine and SowingPlan, started to work on HarvestEstimateHandler.
- **Day 3:** Finished HarvestEstimateHandler, though wasn't tested, started to work on IrrigationHandler. Fixed detekt errors.
- **Day 4:** Started to write tests for MapParser. Fixed some bugs both in MapParser and FarmParser.
- **Day 5:** Fixed some bugs in ActionHandler and MowingHandler. Started to write system tests.
- **Day 6:** Fixed more bugs in ScenarioParser and FarmParser. Fixed bugs all around.
- **Day 7:** Fixed some bugs in ScenarioParser, Simulation, CloudHandler. Started to write more system tests.
- **Day 8:** Fixed bugs in IrrigationHandler, HarvestingHandler. Checked if the action handlers worked.
- **Day 9:** Started to emphasize heavily on writing system tests. Wrote system tests for all action handlers. Also started to write mutant tests.
- **Day 10:** Writing system tests all day. Fixing bugs accordingly.
- **Day 11:** Emphasize heavily on system and unit tests. Fixing final bugs.



---

## Usage of Generative AI

**Ana:**
Ana used the following tools in the implementation phase:
- ChatGPT for Kotlin syntax

**Thejitha:**
Theijitha used the following tools in the implementation phase:
- Github Copilot for code completion
- ChatGPT for Kotlin data structures and methods and writing tests

**Nehesh:** <br>
Nehesh used the following tools in the implementation phase:
- GitHub Copilot for code completion 
- [Perplexity](https://www.perplexity.ai/) to help with specific syntax for Kotlin and comparing logs.

**Dipu:**
Dipu used the following tools in the implementation phase:
- GitHub Copilot for basic code completion and suggestions.
- NotebookLM for sourcing specific points from the specification for easier verification.


**Mahmoud:**
Mahmoud used the following tools in the implementation phase:
- Copilot for syntax and code completion

**Kevin:**
Kevin used the following tools in the implementaion phase:
- NotebookLM for sourcing specific points from the specification for easier verification.
- GPT-5 for syntax

**Kerem:** <br>
Kerem used the following tools in the implementation phase:
- ChatGPT (GPT-4) for code completion and debugging assistance. He used it for system tests, in which he used it to autocomplete some of the test cases. In addition
- Used Copilot to find edge cases while writing system tests.


We are aware of the potential dangers of using these tools and take full responsibility for any code, documents and other content produced during the group phase.