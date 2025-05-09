// CSVUtils.java
package minesweeper;

import java.io.*;
import java.util.Arrays;

public class CSVUtils {

 
    /**
     * Loads a Minesweeper board model from a CSV file.
     *
     * The CSV file must have the following format:
     * - The first line is the header containing three values:
     *   1. The level token, which can either be a predefined enum value (e.g., "EASY")
     *      or a custom dimension string in the format "rowsxcols_mines" (e.g., "10x10_20").
     *   2. The number of flags left.
     *   3. The number of treasures on the board.
     * - The subsequent lines represent the board grid, where each cell is encoded as an integer.
     *
     * The method performs the following steps:
     * 1. Parses the level token to determine the board dimensions and mine count.
     * 2. Reads the number of flags left and the treasure count from the header.
     * 3. Constructs a BoardModel instance and populates it with cell data from the CSV.
     * 4. Recomputes adjacent mine counts based on the loaded mine positions.
     * 5. Restores the flags left and treasure count to the model.
     *
     * @param path The file path to the CSV file.
     * @return A fully initialized {@link BoardModel} instance based on the CSV data.
     * @throws IOException If the file cannot be read, the format is invalid, or the level token is unrecognized.
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

/**
 * Loads a test Minesweeper board from a CSV file.
 * The file is expected to contain exactly 8 lines, each with 8 comma-separated integer values.
 *
 * @param path The file path to the CSV file containing the board data.
 * @return A 2D integer array representing the Minesweeper board, where each element corresponds
 *         to a cell value from the file.
 * @throws IOException If the file cannot be read, does not contain exactly 8 lines, 
 *                     or if any line does not contain exactly 8 comma-separated values.
 * @throws NumberFormatException If any of the values in the file cannot be parsed as integers.
 */
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
    /**
     * Saves the current state of the game board to a CSV file.
     *
     * The CSV file will include a header row containing the game level, the number
     * of flags left, and the treasure count. Following the header, the grid of the
     * board will be written, where each cell is represented by a code determined
     * by the `codeForCell` method of the `BoardModel`.
     *
     * @param model The `BoardModel` object representing the current state of the game.
     * @param path  The file path where the CSV file will be saved.
     * @throws IOException If an I/O error occurs while writing to the file.
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
