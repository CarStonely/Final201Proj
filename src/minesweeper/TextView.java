package minesweeper;

import java.util.Scanner;

public class TextView implements IView {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void display(BoardModel model) {
        Cell[][] grid = model.getGrid();
        System.out.println("\nCurrent Board:");
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                Cell cell = grid[r][c];
                if (cell.isRevealed()) {
                    if (cell.isMine()) System.out.print("* ");
                    else if (cell.hasTreasure()) System.out.print("T ");
                    else System.out.print(cell.getAdjMines() + " ");
                } else if (cell.isFlagged()) {
                    System.out.print("F ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println("Flags left: " + model.getFlagsLeft() +
                           " | Treasures: " + model.getTreasureCount());
    }

    @Override
    public Move promptMove() {
        System.out.print("Enter move ([R]eveal or [M]ark) row col: ");
        String type = scanner.next();
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        Move.Type moveType = type.equalsIgnoreCase("M") ? Move.Type.MARK : Move.Type.REVEAL;
        return new Move(moveType, row, col);
    }
}
