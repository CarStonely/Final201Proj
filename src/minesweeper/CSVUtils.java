// CSVUtils.java
package minesweeper;

import java.io.*;
import java.util.Arrays;

public class CSVUtils {

    /**
     * Load a saved board from CSV.  Header: level,flagsLeft,treasureCount
     * Level may be enum name or "rowsxcols_mines" string.
     */
    public static BoardModel loadFromCSV(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] header = br.readLine().split(",");
            if (header.length < 3) {
                throw new IOException("Invalid CSV header");
            }

            // 1) Parse the level token (enum or dimension string)
            Level level;
            try {
                level = Level.valueOf(header[0]);
            } catch (IllegalArgumentException ex) {
                String[] parts = header[0].split("[x_]");
                if (parts.length != 3) {
                    throw new IOException("Unknown level token: " + header[0]);
                }
                int rows  = Integer.parseInt(parts[0]);
                int cols  = Integer.parseInt(parts[1]);
                int mines = Integer.parseInt(parts[2]);
                level = Arrays.stream(Level.values())
                              .filter(l -> l.rows()==rows && l.cols()==cols && l.mines()==mines)
                              .findFirst()
                              .orElseThrow(() -> new IOException("Unknown level dims: " + header[0]));
            }

            // 2) Flags & treasures
            int flagsLeft     = Integer.parseInt(header[1]);
            int treasureCount = Integer.parseInt(header[2]);

            // 3) Build model and load each code
            BoardModel model = new BoardModel(
                level.rows(), level.cols(), level.mines(), level.treasures()
            );

            for (int r = 0; r < model.getRows(); r++) {
                String[] tokens = br.readLine().split(",");
                for (int c = 0; c < model.getCols(); c++) {
                    int code = Integer.parseInt(tokens[c]);
                    model.setCellFromCode(r, c, code);
                }
            }

            // 4) Recompute adjacents from loaded mines
            model.recalcAdjacents();

            // 5) Restore counters
            model.setFlagsLeft(flagsLeft);
            model.setTreasureCount(treasureCount);

            return model;
        }
    }

    // CSVUtils.java (add this new method)
public static int[][] loadTestBoard(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        int[][] cells = new int[8][8];
        for (int r = 0; r < 8; r++) {
            String line = br.readLine();
            if (line == null) throw new IOException("Expected 8 lines, got fewer");
            String[] parts = line.split(",");
            if (parts.length != 8)
                throw new IOException("Line " + r + " does not have 8 values");
            for (int c = 0; c < 8; c++) {
                cells[r][c] = Integer.parseInt(parts[c]);
            }
        }
        return cells;
    }
}


    /**
     * Write out a current board to CSV: header + grid codes.
     */
    public static void saveToCSV(BoardModel model, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            // header
            pw.printf("%s,%d,%d%n",
                model.getLevel(),
                model.getFlagsLeft(),
                model.getTreasureCount()
            );
            // grid
            for (int r = 0; r < model.getRows(); r++) {
                for (int c = 0; c < model.getCols(); c++) {
                    pw.print(model.codeForCell(r, c));
                    if (c < model.getCols() - 1) pw.print(',');
                }
                pw.println();
            }
        }
    }
}
