# 🧱 Project: Java-Based Tetris-like Game

A fully functional, grid-based puzzle game built with Java Swing and AWT. This project features a custom game engine, real-time input handling, and dynamic sound effects.

---

## 🚀 Deployment & Execution

### **Environment Prerequisites**

* **Java Development Kit (JDK) 8 or higher** is required for compilation and execution.

### **Directory Requirements**

To ensure the `SFX.java` handler functions correctly, the root directory should contain an `sfx/` folder with the necessary `.wav` assets (7 files total).

### **Execution Procedures**

#### **Method 1: Source Compilation (For Developer)**

1. **Compile all source files:**

```
javac *.java

```

2. **Execute the application entry point:**

```
java Main

```

#### **Method 2: Executable Deployment (For Player)**

1. **Package the classes into an executable archive:**

```
jar cvfe Tetris.jar Main *.class

```

2. **Launch the application:**

* Execute the **Tetris.jar** file directly, or use the command: `java -jar Tetris.jar`.
* *Note: The `sfx/` directory must be in the same relative path as the `.jar` for audio initialization.*

---

## 🎮 Gameplay Mechanics & Logic

### **Primary Objective**

The player must strategically manipulate falling **Tetrominoes** to complete horizontal lines. Filled lines are cleared from the `Board`, awarding points and preventing the stack from reaching the "Top-Out" state at the upper boundary.

### **Game Modes**

* **Classic Mode:** A traditional endurance test where the falling gravity (speed) scales based on point progression.
* **Pothole Mode:** At every **1000-point milestone**, the system triggers a "pothole" event. This adds a new line of blocks at the bottom of the grid with a randomly generated gap. Players must navigate these structural irregularities to maintain a clear board.
* **Random Shape Mode:** This mechanic allows for on-the-fly piece transformation. By pressing the **Up Arrow**, the active Tetromino is instantly swapped for a randomized shape with a randomized initial rotation.

### **Scoring Logic**

The scoring system is designed to reward higher difficulty and multi-line clears:

* **Difficulty Multipliers:** Points per line are scaled by the selected difficulty level:
* **Easy:** 50 points per line
* **Normal:** 100 points per line
* **Hard:** 200 points per line

---

## ⌨️ Input Mapping

### **Navigation Controls**

* **Cycle Options:** Up Arrow / Down Arrow
* **Confirm / Initialize:** Enter

### **In-Game Controls**

* **Lateral Movement:** Left / Right Arrow
* **Rotation / Shape Randomization:** Up Arrow
* **Soft Drop:** Down Arrow
* **Instant Hard Drop:** Spacebar
* **Pause:** Escape
* **Return to Menu:** Backspace

---

## 🛠️ System Architecture

* **Graphics Engine:** Implements a stable **60 FPS game loop** using `java.awt.Graphics2D` for smooth frame updates and animations.
* **Audio Handler:** Leverages the `javax.sound.sampled` library to manage concurrent background music (BGM) and low-latency sound effect triggers.
* **Modular Design:** The codebase is split into 11 specialized classes (e.g., `Board.java` for grid logic, `Tetromino.java` for piece geometry, and `GameUserInterface.java` for HUD rendering) to ensure high maintainability and clean abstraction.
