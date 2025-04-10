Skip to content
Navigation Menu
CarStonely
CSE-201-MINESWEEPER

Type / to search
Code
Issues
Pull requests
Actions
Projects
Wiki
Security
Insights
Commit ca348a2
janbodnar
janbodnar
authored
on May 17, 2019
Unverified
Update Board.java
master
1 parent 
d75b5a2
 commit 
ca348a2
File tree
Filter files…
src/com/zetcode
Board.java
1 file changed
+19
-10
lines changed
Search within code
 
‎src/com/zetcode/Board.java
+19
-10
Original file line number	Diff line number	Diff line change
@@ -31,8 +31,8 @@ public class Board extends JPanel {
    private final int N_ROWS = 16;
    private final int N_COLS = 16;

    private final int BOARD_WIDTH = N_ROWS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = N_COLS * CELL_SIZE + 1;
    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;

    private int[] field;
    private boolean inGame;
@@ -55,7 +55,8 @@ private void initBoard() {
        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            String path = "src/resources/" + i + ".png";
            var path = "src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

@@ -67,21 +68,22 @@ private void newGame() {

        int cell;

        Random random = new Random();
        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;
        
        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());
@@ -121,7 +123,7 @@ private void newGame() {
                        field[cell] += 1;
                    }
                }
                
                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
@@ -153,7 +155,7 @@ private void newGame() {
        }
    }

    public void find_empty_cells(int j) {
    private void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;
@@ -250,15 +252,18 @@ public void paintComponent(Graphics g) {
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
@@ -270,6 +275,7 @@ public void paintComponent(Graphics g) {
                    }

                } else {
                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
@@ -284,8 +290,10 @@ public void paintComponent(Graphics g) {
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
@@ -315,9 +323,11 @@ public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {
                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
@@ -338,6 +348,7 @@ public void mousePressed(MouseEvent e) {
                } else {

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {
                        return;
                    }

@@ -350,7 +361,7 @@ public void mousePressed(MouseEvent e) {
                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            inGame = false;
                        }
                        
                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);
                        }
@@ -360,9 +371,7 @@ public void mousePressed(MouseEvent e) {
                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}
0 commit comments
Comments
0
 (0)
Comment
You're not receiving notifications from this thread.

Update Board.java · CarStonely/CSE-201-MINESWEEPER@ca348a2 
