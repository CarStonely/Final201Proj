package minesweeper;

/**
 * IView defines the contract for all view implementations in the Minesweeper game.
 * It allows the game to swap between different presentation modes (e.g., GUIView, TextView)
 * without changing game logic.
 */
public interface IView {

    /**
     * Display or refresh the current state of the board model to the user.
     * Implementations should render cells, flags, and any game status as appropriate.
     *
     * @param model the BoardModel containing the latest game state
     */
    void display(BoardModel model);

    /**
     * Prompt the user to make a move, such as revealing or flagging a cell.
     * Implementations may use console input, dialog boxes, or other UI elements.
     *
     * @return a Move object representing the user's chosen action (row, column, action type)
     */
    Move promptMove();
}
