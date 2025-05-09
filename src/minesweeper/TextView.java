package minesweeper;

import java.util.Scanner;

/**
 * TextView provides a console-based interface for the Minesweeper game.
 * It displays the board in text form and reads user moves from standard input.
 */
public class TextView implements IView {
    // Scanner for reading user input from the console
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints the current board state to the console.
     * Revealed cells show '*' for mines, 'T' for treasures, or the number of adjacent mines.
     * Hidden cells show '.' or 'F' if flagged.
     * Also displays remaining flags and collected treasures.
     *
     * @param model the BoardModel containing the game state
     */
    @Override
    public void display(BoardModel model) {
        Cell[][] grid = model.getGrid();
        System.out.println("\nCurrent Board:");
        // Loop over each row and column to render the grid
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                Cell cell = grid[r][c];
                if (cell.isRevealed()) {
                    // Revealed cell: show mine, treasure, or adjacent count
                    if (cell.isMine()) System.out.print("* ");
                    else if (cell.hasTreasure()) System.out.print("T ");
                    else System.out.print(cell.getAdjMines() + " ");
                } else if (cell.isFlagged()) {
                    // Flagged but not revealed
                    System.out.print("F ");
                } else {
                    // Hidden and unflagged
                    System.out.print(". ");
                }
            }
            System.out.println();  // Newline at end of row
        }
        // Summary line for flags and treasures
        System.out.println("Flags left: " + model.getFlagsLeft() +
                           " | Treasures: " + model.getTreasureCount());
    }

    /**
     * Prompts the user to enter a move in the format: "R row col" or "M row col".
     * 'R' or 'r' for reveal, 'M' or 'm' for mark (flag).
     * Parses the input and constructs a Move object.
     *
     * @return a Move representing the user's chosen action and coordinates
     */
    @Override
    public Move promptMove() {
        System.out.print("Enter move ([R]eveal or [M]ark) row col: ");
        // Read action type token and coordinates
        String type = scanner.next();
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        // Determine move type based on input (default to REVEAL)
        Move.Type moveType = type.equalsIgnoreCase("M")
            ? Move.Type.MARK
            : Move.Type.REVEAL;
        // Return new Move instance
        return new Move(moveType, row, col);
    }
}
