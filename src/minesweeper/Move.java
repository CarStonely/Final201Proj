package minesweeper;

/**
 * Represents a player's action in the Minesweeper game.
 * A Move consists of a Type (REVEAL or MARK) and target coordinates (row, col).
 */
public class Move {

    /**
     * Enum for possible move actions: reveal a cell or mark (flag) a cell.
     */
    public enum Type {
        REVEAL, // uncover the cell at the given coordinates
        MARK    // toggle a flag on the cell at the given coordinates
    }

    private final Type type; // the action type
    private final int row;   // zero-based row index of the target cell
    private final int col;   // zero-based column index of the target cell

    /**
     * Constructs a Move with the specified type and coordinates.
     *
     * @param type the type of move to perform (REVEAL or MARK)
     * @param row  the row index of the target cell
     * @param col  the column index of the target cell
     */
    public Move(Type type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the type of this move.
     *
     * @return the Move.Type (REVEAL or MARK)
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the row index for this move.
     *
     * @return zero-based row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index for this move.
     *
     * @return zero-based column
     */
    public int getCol() {
        return col;
    }
}
