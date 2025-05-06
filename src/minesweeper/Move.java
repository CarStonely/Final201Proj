package minesweeper;

public class Move {
    public enum Type { REVEAL, MARK }
    private final Type type;
    private final int row;
    private final int col;

    public Move(Type type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public Type getType() { return type; }
    public int getRow()   { return row; }
    public int getCol()   { return col; }
}
