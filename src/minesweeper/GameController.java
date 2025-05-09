package minesweeper;

public class GameController {
    private final BoardModel model;
    private final IView view;

    public GameController(BoardModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Starts the Minesweeper game loop. The game continues until the player either
     * wins by revealing all non-mine cells or loses by revealing a mine. During
     * each iteration of the loop, the current game state is displayed, and the
     * player is prompted to make a move. The move can either reveal a cell or mark
     * a cell as containing a mine. At the end of the game, a message is displayed
     * indicating whether the player won or lost.
     */
    public void startGame() {
        while (!model.isGameOver() && !model.isGameWon()) {
            view.display(model);
            Move move = view.promptMove();
            if (move.getType() == Move.Type.REVEAL) {
                model.revealCell(move.getRow(), move.getCol());
            } else {
                model.markCell(move.getRow(), move.getCol());
            }
        }

        view.display(model);
        if (model.isGameWon()) System.out.println("🎉 Congratulations, you won!");
        else                   System.out.println("💥 You hit a mine. Game over.");
    }
}
