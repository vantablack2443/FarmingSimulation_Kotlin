# Farm Simulation Engine

A comprehensive farm management simulator written in Kotlin that models agricultural operations including crop cultivation, weather patterns, machine operations, and farm expansion over configurable time periods.

## Project Overview

This project implements a detailed agricultural simulation system where farmers manage multiple farms, plan crop cultivation, handle weather events, and optimize farm operations across configurable simulation periods. The simulator tracks farm state across multiple ticks and years, enforcing realistic constraints and mechanics for all farming activities.

## Features

- **Farm Management**: Create and manage farms with customizable fields and plantations
- **Crop Cultivation**: Plant, irrigate, weed, cut, and harvest crops with realistic growth mechanics
- **Weather System**: Simulate cloud movement and drought conditions affecting farm operations
- **Incident Handling**: Handle random farm incidents including animal attacks and bee colonies
- **Machine Operations**: Manage farm machinery including tractors and sowing machines
- **Farm Expansion**: Expand farm territories and adapt to changing conditions
- **Detailed Logging**: Comprehensive logging system for simulation tracking and debugging
- **Scenario Planning**: Configure and execute complex farming scenarios through structured scenario files

## Requirements

- Java 17 or higher
- Kotlin 2.1.21
- Gradle 8.0 or higher

## Building

```bash
./gradlew build
```

## Running the Simulation

```bash
./gradlew run --args="--map <map_file> --farms <farms_file> --scenario <scenario_file> --max_ticks <ticks> --log_level <level>"
```

### Command Line Arguments

- `--map`: Path to the map configuration file (required)
- `--farms`: Path to the farms configuration file (required)
- `--scenario`: Path to the scenario file (required)
- `--max_ticks`: Maximum number of simulation ticks (required, max 1000)
- `--log_level`: Logging level for output (required)
- `--start_year_tick`: Starting year tick (optional)
- `--out`: Output file path (optional)

## Testing

Run all tests:
```bash
./gradlew test
```

Run system tests:
```bash
./gradlew systemtest
```

## Implementation Approach

### Design Architecture

The project follows a **modular design pattern** with clear separation of concerns:

- **Parser Layer**: Handles input validation and file parsing (maps, farms, scenarios) with comprehensive error handling
- **Simulation Engine**: Orchestrates the simulation loop, managing state transitions across ticks and years
- **Handler System**: Specialized action handlers manage distinct farming operations (sowing, harvesting, irrigation, etc.)
- **Domain Models**: Rich domain objects (Crop, Machine, Farm, Tile) encapsulate farm entities and their behavior
- **Logging Framework**: Centralized logging system for tracking all significant simulation events

### Testing Strategy

The project employs a **multi-layered testing approach**:

#### Unit Tests
- Comprehensive unit test coverage for core logic including:
  - Coordinate system validation
  - Map and terrain mechanics
  - Sowing and harvest logic
  - Machine operations
  - Logging system

#### System Tests (Simulation Tests)
- End-to-end scenario testing covering:
  - Complete farm operation workflows (planting, irrigation, harvesting cycles)
  - Weather system interactions (cloud movement, drought effects)
  - Complex multi-phase scenarios with cascading farm actions
  - Validation phase correctness
  - Edge cases and error scenarios

Each scenario is configured through structured test resources, allowing comprehensive validation of the entire simulation pipeline from input parsing through action execution and result verification.

### Code Quality

The project uses:
- **Detekt**: For static code analysis and linting
- **JaCoCo**: For code coverage reporting

Run code analysis:
```bash
./gradlew detekt
```

View coverage:
```bash
./gradlew jacoco
```

## License

This is a group project implementation for an educational course.
