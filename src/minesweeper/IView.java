package minesweeper;


// IView.java
// NEW: Interface for interchangeable views
public interface IView {
    void display(BoardModel model);
    Move promptMove();
}
