
package minesweeper;

import java.util.Random;

public class BoardModel {
    private final int rows, cols, totalMines, totalTreasures;
    private Cell[][] grid;
    private boolean gameOver;
    private int flagsLeft, treasureCount;

    /**
     * Constructs a new board model.
     * @param rows      number of rows
     * @param cols      number of columns
     * @param mines     total number of mines
     * @param treasures total number of hidden treasures (randomly placed)
     */
    public BoardModel(int rows, int cols, int mines, int treasures) {
        this.rows           = rows;
        this.cols           = cols;
        this.totalMines     = mines;
        this.totalTreasures = treasures;
        initialize();
    }

    /**
     * Initialize or reset the board: create all cells, randomly place mines & treasures,
     * then compute adjacency counts.
     */
    public void initialize() {
        grid         = new Cell[rows][cols];
        flagsLeft    = totalMines;
        treasureCount = 0;
        gameOver     = false;

        // create blank cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell();
            }
        }

        placeMines();
        placeTreasures();
        calcAdjacents();
    }

    /** Randomly place exactly totalMines mines. */
    private void placeMines() {
        Random rand = new Random();
        int placed = 0;
        while (placed < totalMines) {
            int r = rand.nextInt(rows), c = rand.nextInt(cols);
            if (!grid[r][c].isMine()) {
                grid[r][c].setMine(true);
                placed++;
            }
        }
    }

    /** Randomly place exactly totalTreasures treasures (not on a mine). */
    private void placeTreasures() {
        Random rand = new Random();
        int placed = 0;
        while (placed < totalTreasures) {
            int r = rand.nextInt(rows), c = rand.nextInt(cols);
            if (!grid[r][c].isMine() && !grid[r][c].hasTreasure()) {
                grid[r][c].setTreasure(true);
                placed++;
            }
        }
    }

    /** Compute adjacency counts for every non-mine cell. */
    private void calcAdjacents() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!grid[r][c].isMine()) {
                    int count = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int rr = r + dr, cc = c + dc;
                            if (rr >= 0 && rr < rows
                             && cc >= 0 && cc < cols
                             && grid[rr][cc].isMine()) {
                                count++;
                            }
                        }
                    }
                    grid[r][c].setAdjMines(count);
                }
            }
        }
    }

    /** Public helper to re-run adjacency logic after loading from CSV. */
    public void recalcAdjacents() {
        calcAdjacents();
    }

    /**
     * Reveal cell (r,c).  If it’s a treasure, increment treasureCount.
     * If it’s a mine and treasureCount>0, spend one treasure; else gameOver=true.
     * If it’s a zero‐adj cell, flood‐reveal neighbors.
     */
    public void revealCell(int r, int c) {
        if (outOfBounds(r, c)
         || grid[r][c].isRevealed()
         || grid[r][c].isFlagged()
         || gameOver) {
            return;
        }

        Cell cell = grid[r][c];
        cell.setRevealed(true);

        if (cell.hasTreasure()) {
            treasureCount++;
        }
        else if (cell.isMine()) {
            if (treasureCount > 0) {
                treasureCount--;
            } else {
                gameOver = true;
            }
        }
        else if (cell.getAdjMines() == 0) {
            floodReveal(r, c);
        }
    }

    /** Flood‐fill for empty (0‐adj) cells. */
    private void floodReveal(int r, int c) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int rr = r + dr, cc = c + dc;
                if (!outOfBounds(rr, cc) && !grid[rr][cc].isRevealed()) {
                    revealCell(rr, cc);
                }
            }
        }
    }

    /** Toggle a flag at (r,c) (if not revealed), adjusting flagsLeft. */
    public void markCell(int r, int c) {
        if (outOfBounds(r, c) || grid[r][c].isRevealed() || gameOver) return;
        boolean now = !grid[r][c].isFlagged();
        grid[r][c].setFlagged(now);
        flagsLeft += now ? -1 : 1;
    }

    /** Returns true if the player has hit a mine (and has no treasure to defuse it). */
    public boolean isGameOver() {
        return gameOver;
    }

    /** 
     * Returns true only when *all* non-mine cells (including treasures) 
     * have been revealed.
     */
    public boolean isGameWon() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (!cell.isMine() && !cell.isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    // --- Accessors ---

    /** The grid of Cell objects. */
    public Cell[][] getGrid() {
        return grid;
    }
    public int getRows()            { return rows; }
    public int getCols()            { return cols; }
    public int getFlagsLeft()       { return flagsLeft; }
    public int getTreasureCount()   { return treasureCount; }

    /** Reset to a fresh random board. */
    public void clearGrid() {
        initialize();
    }

    /**
     * Restore a single cell’s state from its CSV code:
     *  0  = covered safe cell
     *  1–8  = revealed safe cell with that many adj mines
     *  9  = hidden (unrevealed) mine
     *  11 = flagged safe cell
     *  12 = flagged mine cell
     *  13 = revealed treasure cell
     */
    public void setCellFromCode(int r, int c, int code) {
        Cell cell = grid[r][c];

        // Mine & treasure flags
        cell.setMine(code == 9 || code == 12);
        cell.setTreasure(code == 13);

        // Flag state
        cell.setFlagged(code == 11 || code == 12);

        // Revealed?  Only numbered (1–8) and treasure (13) are shown
        if (code >= 1 && code <= 8) {
            cell.setAdjMines(code);
            cell.setRevealed(true);
        }
        else if (code == 13) {
            cell.setRevealed(true);
        }
        else {
            cell.setRevealed(false);
        }
    }

    /**
     * Serialize current cell state into a CSV code.
     * See setCellFromCode for code meanings.
     */
    public int codeForCell(int r, int c) {
        Cell cell = grid[r][c];
        // Unrevealed cells are either flagged or hidden mines
        if (cell.isFlagged()) {
            return cell.isMine() ? 12 : 11;
        }
        if (cell.isMine()) {
            return 9;
        }
        if (!cell.isRevealed()) {
            return 0;
        }
        if (cell.hasTreasure()) {
            return 13;
        }
        return cell.getAdjMines();
    }

    /** Header string for CSV, e.g. "8x8_10". */
    public String getLevel() {
        return String.format("%dx%d_%d", rows, cols, totalMines);
    }

    public void setFlagsLeft(int f)    { this.flagsLeft = f; }
    public void setTreasureCount(int t){ this.treasureCount = t; }

    /** @return true if (r,c) is outside the board. */
    private boolean outOfBounds(int r, int c) {
        return r < 0 || r >= rows || c < 0 || c >= cols;
    }

    // --- Factory for testing mode ---

    /**
     * Build a strictly‐configured 8×8 test board from a CSV of 0/1/2 values:
     *   0 = empty, 1 = mine, 2 = treasure.
     *
     * @param cells an 8×8 int array loaded from CSV
     * @return a BoardModel with exactly those mines & treasures, no randomness
     */
    public static BoardModel fromTestConfig(int[][] cells) {
        // Start with 8×8, 10 mines (treasures will be placed below)
        BoardModel model = new BoardModel(8, 8, 10, 0);

        // Override grid entirely
        model.grid = new Cell[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                model.grid[r][c] = new Cell();
            }
        }
        // Place exactly according to the 0/1/2 spec
        // Iterate through each cell in the 8x8 grid
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
            int v = cells[r][c]; // Get the value from the input configuration
            if (v == 1) {
                // If the value is 1, set the cell as a mine
                model.grid[r][c].setMine(true);
            } else if (v == 2) {
                // If the value is 2, set the cell as a treasure
                model.grid[r][c].setTreasure(true);
            }
            }
        }

        // Recompute adjacency, reset counters
        model.recalcAdjacents();
        model.flagsLeft    = 10;
        model.treasureCount = 0;
        model.gameOver     = false;
        return model;
    }
}
