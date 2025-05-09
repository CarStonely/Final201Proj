package minesweeper;

/**
 * The Cell class represents a single cell in a Minesweeper game grid.
 * It contains information about the cell's state, such as whether it contains
 * a mine, is flagged, has treasure, or is revealed, as well as the number of
 * adjacent mines.
 */
public class Cell {
    private int adjMines;
    private boolean isMine;
    private boolean isFlagged;
    private boolean hasTreasure;
    private boolean isRevealed;

    public Cell() {
        this.adjMines = 0;
        this.isMine = false;
        this.isFlagged = false;
        this.hasTreasure = false;
        this.isRevealed = false;
    }

    // NEW: getters and setters
    public int getAdjMines() { return adjMines; }
    public void setAdjMines(int adjMines) { this.adjMines = adjMines; }

    public boolean isMine() { return isMine; }
    public void setMine(boolean mine) { isMine = mine; }

    public boolean isFlagged() { return isFlagged; }
    public void setFlagged(boolean flagged) { isFlagged = flagged; }

    public boolean hasTreasure() { return hasTreasure; }
    public void setTreasure(boolean treasure) { this.hasTreasure = treasure; }

    public boolean isRevealed() { return isRevealed; }
    public void setRevealed(boolean revealed) { isRevealed = revealed; }
}