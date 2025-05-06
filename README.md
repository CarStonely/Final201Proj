# Java Minesweeper Game 🌱💣

Welcome to your laid-back Java Minesweeper clone! This project started as a CSE-201 assignment and has grown into a simple, fun little game you can run in two modes: GUI or text-based. No pressure—just dig around for mines and try not to blow up!

---

## Table of Contents
- [What’s Inside](#whats-inside)
- [Quick Start](#quick-start)
- [How to Play](#how-to-play)
- [Screenshot](#screenshot)

---

## What’s Inside

- **`BoardModel.java`** – Manages the grid, places mines, tracks flags, and reveals cells.  
- **`Cell.java`** – Represents each square on the board.  
- **`GameController.java`** – Connects the game logic between the model and views.  
- **`GUIView.java` / `TextView.java`** – Two different ways to play: graphical or text-based.  
- **`Launcher.java`** – Allows you to choose between GUI or text mode, and regular or testing mode.  
- **`TestBoardValidator.java`** – Provides quick tests to validate board behavior.  

---

## Quick Start

1. **Compile everything**  
    ```bash
    javac *.java
    ```

2. **Run the Launcher**  
    - **GUI mode**:  
      ```bash
      java Launcher gui
      ```
    - **Text mode (console-only)**:  
      ```bash
      java Launcher text
      ```

---

## How to Play

### Reveal a Cell:
- **GUI**: Left-click on a cell.  
- **Text**: Type `r x y` (e.g., `r 3 5`).  

### Flag / Unflag a Cell:
- **GUI**: Right-click on a cell.  
- **Text**: Type `f x y` (e.g., `f 3 5`).  

### Objective:
- **Win**: Uncover all non-mine cells.  
- **Lose**: Step on a mine—boom! 💥  

---

## Screenshot

![Minesweeper game screenshot](minesweeper.png)

---
