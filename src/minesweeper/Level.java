// Level.java
// NEW: Define play levels
package minesweeper;

public enum Level {
    BEGINNER(8, 8, 10, 2),
    INTERMEDIATE(16, 16, 40, 5),
    EXPERT(16, 30, 99, 10);

    private final int rows, cols, mines, treasures;
    Level(int rows, int cols, int mines, int treasures) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.treasures = treasures;
    }
    public int rows()     { return rows; }
    public int cols()     { return cols; }
    public int mines()    { return mines; }
    public int treasures(){ return treasures; }
}
