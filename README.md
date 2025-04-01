# Farm Simulation with Sheep and Dogs

This Java-based simulation involves a farm grid where sheep and dogs interact. The sheep try to escape through gates while the dogs chase them. The simulation uses multithreading to simulate the movement of sheep and dogs in real-time.

## Project Overview

The farm is represented by a grid with walls, gates, and empty cells. Sheep and dogs are placed randomly within the grid. The simulation continues until a sheep escapes through one of the gates.

### Key Components:
- **Farm**: The main simulation environment, containing a grid and managing the interaction between sheep, dogs, and the environment.
- **Entity**: A base class for both sheep and dogs, representing the position and name of the entity.
- **Sheep**: A class that extends `Entity` and represents the sheep. It moves randomly within the grid, avoiding dogs and trying to escape through the gates.
- **Dog**: A class that extends `Entity` and represents the dogs. They chase the sheep within the grid.
- **Cell**: Represents each cell in the grid, which can either be empty, a wall, or a gate.
- **CellType**: Enum that defines the types of cells (`EMPTY`, `WALL`, `GATE`).

## Features

- **Multithreading**: Sheep and dogs move concurrently using threads.
- **Random Movement**: Both sheep and dogs move randomly across the grid.
- **Escape Mechanism**: Sheep attempt to escape through the gates, and the simulation ends when a sheep escapes.
- **Grid Rendering**: The farm grid is displayed on the console with different symbols for walls, sheep, dogs, and the escaped gate.

## Running the Simulation

### Requirements
- JDK 8 or higher.

### Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/farm-simulation.git
