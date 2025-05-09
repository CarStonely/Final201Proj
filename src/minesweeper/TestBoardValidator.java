package minesweeper;

import java.util.*;

/**
 * Validates a custom test board configuration for Minesweeper.
 * Ensures the board is 8x8, contains exactly 10 mines (with specific placement rules),
 * and no more than 9 treasures. Throws IllegalArgumentException on invalid input.
 */
public class TestBoardValidator {

    /**
     * Validates the given 2D cell array as a proper test board.
     * <ul>
     *   <li>Must be exactly 8 rows and 8 columns</li>
     *   <li>Cell values: 0=empty, 1=mine, 2=treasure</li>
     *   <li>Exactly 10 mines and at most 9 treasures</li>
     *   <li>Placement rules for the first 8, 9th, and 10th mines</li>
     * </ul>
     * @param cells 8x8 array of integers representing board contents
     * @throws IllegalArgumentException if any rule is violated
     */
    public static void validate(int[][] cells) {
        // Check board dimensions
        if (cells.length != 8)
            throw new IllegalArgumentException("Board must be 8 rows");
        for (int r = 0; r < 8; r++) {
            if (cells[r].length != 8)
                throw new IllegalArgumentException("Row " + r + " must have 8 columns");
        }

        // Collect coordinates of mines (1) and treasures (2)
        List<Coord> mineCoords = new ArrayList<>();
        List<Coord> treasureCoords = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                int v = cells[r][c];
                if (v == 1) {
                    mineCoords.add(new Coord(r, c));
                } else if (v == 2) {
                    treasureCoords.add(new Coord(r, c));
                } else if (v != 0) {
                    // Invalid cell value
                    throw new IllegalArgumentException(
                        "Invalid cell value at " + r + "," + c + ": " + v);
                }
            }
        }

        // Check counts of mines and treasures
        if (mineCoords.size() != 10)
            throw new IllegalArgumentException(
                "Must have exactly 10 mines, found " + mineCoords.size());
        if (treasureCoords.size() > 9)
            throw new IllegalArgumentException(
                "At most 9 treasures allowed, found " + treasureCoords.size());

        // Enforce rules on mine placement:
        // 1) First 8 mines: unique row & column, no orthogonal adjacency, one on main diagonal
        List<Coord> first8 = mineCoords.subList(0, 8);
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        boolean diagonalFound = false;
        for (Coord m : first8) {
            if (!rows.add(m.r))
                throw new IllegalArgumentException(
                    "Duplicate row in first 8 mines: " + m);
            if (!cols.add(m.c))
                throw new IllegalArgumentException(
                    "Duplicate column in first 8 mines: " + m);
            if (m.r == m.c) diagonalFound = true;  // on main diagonal
        }
        if (!diagonalFound)
            throw new IllegalArgumentException(
                "One of the first 8 mines must lie on the main diagonal (r==c)");

        // Check no two of the first 8 are orthogonally adjacent
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                Coord a = first8.get(i);
                Coord b = first8.get(j);
                boolean sameRowAdj = a.r == b.r && Math.abs(a.c - b.c) == 1;
                boolean sameColAdj = a.c == b.c && Math.abs(a.r - b.r) == 1;
                if (sameRowAdj || sameColAdj) {
                    throw new IllegalArgumentException(
                        "First 8 mines must not be orthogonally adjacent: " + a + " & " + b);
                }
            }
        }

        // 2) 9th mine: must be adjacent to at least one of the first 8 (orthogonally)
        Coord ninth = mineCoords.get(8);
        boolean adjToOne = first8.stream().anyMatch(m ->
            (m.r == ninth.r && Math.abs(m.c - ninth.c) == 1) ||
            (m.c == ninth.c && Math.abs(m.r - ninth.r) == 1)
        );
        if (!adjToOne)
            throw new IllegalArgumentException(
                "9th mine " + ninth + " must be adjacent to one of the first 8");

        // 3) 10th mine: must be isolated from all previous nine (no orthogonal adjacency)
        Coord tenth = mineCoords.get(9);
        for (int i = 0; i < 9; i++) {
            Coord m = mineCoords.get(i);
            boolean rowAdj = m.r == tenth.r && Math.abs(m.c - tenth.c) == 1;
            boolean colAdj = m.c == tenth.c && Math.abs(m.r - tenth.r) == 1;
            if (rowAdj || colAdj) {
                throw new IllegalArgumentException(
                    "10th mine " + tenth + " must be isolated from all others");
            }
        }
    }

    /**
     * Simple coordinate holder for row and column indices.
     */
    private static class Coord {
        final int r, c;
        Coord(int r, int c) { this.r = r; this.c = c; }
        @Override
        public String toString() { return "(" + r + "," + c + ")"; }
    }
}
