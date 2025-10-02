# Implementation Report

## Individual Contributions

The following table summarizes the actual contributions of each group member, including deviations from the planned responsibilities. 
The "implemented components" column should only include major contributions to the respective components (i.e., no bugfixes or small additions to existing components).

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

For each student, this section should provide a detailed day-by-day timeline of their activities and progress.

### Ana

- **Day 1:**
- ...

### Thejitha
- **Day 1:**

- ...

### Nehesh
- ...

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

Choose the correct option that applies to each individual student (if you diverted from your initial statement in the implementation plan, make sure to explicitely state it here):

**Alice:**
*Option 1:*
We did not use generative AI in the implementation phase (neither for code-completion, rewriting code, nor testing).

*Option 2:*
(insert tool names properly; if applicable add links or add version numbers of the used tools)
Alice used the following tools in the implementation phase:
Tool-1 for code completion and tool Tool-2 for ... . In addition, she used Tool-3 for ...

**Bob:**
...

**Kerem:**
Kerem used the following tools in the implementation phase:
ChatGPT (GPT-4) for code completion and debugging assistance. He used it for system tests, in which he used it to auto-
complete some of the test cases. In addition, he used Copilot to find edge cases while writing system tests.

In case of option 2, add additional sentences in which you provide more details on which tools you used for which specific tasks and to which extent.

We are aware of the potential dangers of using these tools and take full responsibility for any code, documents and other content produced during the group phase.