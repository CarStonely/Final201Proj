# Java-Minesweeper-Game
Java Minesweeper game source code

https://zetcode.com/javagames/minesweeper/
# Minesweeper 🌱💣

Welcome to your laid-back Java Minesweeper clone! This project started as a CSE-201 assignment and has grown into a simple, fun little game you can run in two modes: GUI or text-based. No pressure—just dig around for mines and try not to blow up!

---

## What’s Inside

- **`BoardModel.java`** – Holds the grid, places mines, tracks flags & reveals  
- **`Cell.java`** – Represents each square on the board  
- **`GameController.java`** – Game logic glue between model and views  
- **`GUIView.java` / `TextView.java`** – Two different ways to play  
- **`Launcher.java`** – Pick your poison: GUI or text, regular or testing mode  
- **`TestBoardValidator.java`** – Some quick tests to make sure your board behaves  

---

## Quick Start

1. **Compile everything**  
   ```bash
   javac *.java

2. Running the Launcher
# GUI mode
java Launcher gui

# Text mode (console-only)
java Launcher text

How to Play
Reveal a cell:

GUI: Left-click

Text: Type r x y (e.g. r 3 5)

Flag / unflag a cell:

GUI: Right-click

Text: Type f x y (e.g. f 3 5)

Win by uncovering all non-mine cells

Lose by stepping on a mine—boom! 💥



![Minesweeper game screenshot](minesweeper.png)
# Final201Proj
