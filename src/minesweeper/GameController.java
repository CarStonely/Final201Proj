package minesweeper;

public class GameController {
    private final BoardModel model;
    private final IView view;

    public GameController(BoardModel model, IView view) {
        this.model = model;
        this.view = view;
    }

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
