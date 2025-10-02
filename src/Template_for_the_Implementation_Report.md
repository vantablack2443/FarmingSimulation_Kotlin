# Implementation Report

## Individual Contributions

The following table summarizes the actual contributions of each group member, including deviations from the planned responsibilities. 

| Member   | Implemented Components                                          | Testing Contributions                                               | Additional Work |
|----------|-----------------------------------------------------------------|---------------------------------------------------------------------|-----------------|
| Ana      | MapParser, CommandLineParser, Main, Simulation, CloudCreation   | Incidents, Cloud, Irrigation Unit test; System Tests for all phases |                 |
| Thejitha | ScenarioParser, SowingHandler, CloudHandler                     | System tests for plantation plant actions                           |                 |
| Nehesh   | Plant classes, Animal Attack, Bee Happy, Map, IrrigationHandler | System tests for parsers and simulation logic                       |                 |
| Dipu     | WeedingHandler, CuttingHandler, CityExpansion, Logger           | System tests for validation and simulation phases                   |                 |
| Mahmoud  | HarvestingHandler, BrokenMachine                                | Unit tests for map, mowing handler; System tests for all phases     |                 |
| Kevin    | MowingHandler, PlantData, Map, Drought, CloudHandler            | Unit tests for sowing, logger; System tests for farm actions        |                 |
| Kerem    | FarmParser, HarvestEstimateHandler, IrrigationHandler           | System tests for all phases except cloud movement                   |                 |

---

## Adjustments from the Implementation Plan

- [Describe adjustment here, e.g., which tasks took more time or were reassigned]  

---

## Detailed Timeline 

### Ana

- **Day 1:**
- ...

### Thejitha
- **Day 1:**

- ...

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
- ...

### Mahmoud
- ...

### Kevin
- ...

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


**Thejitha:**


**Nehesh:** <br>
Nehesh used the following tools in the implementation phase:
- GitHub Copilot for code completion 
- [Perplexity](https://www.perplexity.ai/) to help with specific syntax for Kotlin and comparing logs.

**Dipu:**


**Mahmoud:**


**Kevin:**

**Kerem:** <br>
Kerem used the following tools in the implementation phase:
- ChatGPT (GPT-4) for code completion and debugging assistance. He used it for system tests, in which he used it to autocomplete some of the test cases. In addition
- Used Copilot to find edge cases while writing system tests.


We are aware of the potential dangers of using these tools and take full responsibility for any code, documents and other content produced during the group phase.