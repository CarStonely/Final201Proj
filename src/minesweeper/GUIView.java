package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * GUIView provides a Swing-based graphical interface for the Minesweeper game.
 * It displays the game board, handles user interactions (clicks to reveal or flag cells),
 * and updates game status (flags, treasures, timer).
 */
public class GUIView extends JFrame implements IView {
    private static final int NUM_IMAGES = 14;           // total number of tile icons
    private final BoardModel model;                     // game state model
    private final Image[] img = new Image[NUM_IMAGES];  // loaded tile images
    private final int CELL_SIZE;                        // pixel size of each cell

    private final JLabel flagsLabel    = new JLabel();  // displays remaining flags
    private final JLabel treasureLabel = new JLabel();  // displays collected treasures
    private final JLabel timerLabel    = new JLabel();  // displays elapsed time
    private int secondsElapsed = 0;                     // timer counter
    private final Timer swingTimer;                     // Swing timer for updating the timerLabel

    /**
     * Constructs the GUI, loads resources, and initializes components.
     * @param model the BoardModel representing game state
     */
    public GUIView(BoardModel model) {
        super("Minesweeper");
        this.model = model;

        loadIcons();  // load tile images from resources
        CELL_SIZE = img[0].getWidth(null);  // assume all icons are square and same size

        // --- Menu Bar for saving game ---
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Game...");
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Action for "Save Game" menu item
        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                try {
                    CSVUtils.saveToCSV(model, path);
                    JOptionPane.showMessageDialog(this, "Game saved to " + path);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Failed to save: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Top Panel showing flags, treasures, and timer ---
        flagsLabel.setText("Flags: " + model.getFlagsLeft());
        treasureLabel.setText("Treasures: " + model.getTreasureCount());
        timerLabel.setText("Time: 0");

        // Timer updates every second
        swingTimer = new Timer(1000, ev -> timerLabel.setText("Time: " + ++secondsElapsed));
        swingTimer.start();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        topPanel.add(flagsLabel);
        topPanel.add(treasureLabel);
        topPanel.add(timerLabel);

        // --- Board Panel: custom painting of cells ---
        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // iterate over each cell and draw correct icon
                for (int r = 0; r < model.getRows(); r++) {
                    for (int c = 0; c < model.getCols(); c++) {
                        Cell cell = model.getGrid()[r][c];
                        int code;
                        if (!cell.isRevealed()) {
                            code = cell.isFlagged() ? 11 : 10;  // flagged or covered
                        } else if (cell.isMine()) {
                            code = 9;                           // mine icon
                        } else if (cell.hasTreasure()) {
                            code = 13;                          // treasure icon
                        } else {
                            code = cell.getAdjMines();         // number of adjacent mines
                        }
                        g.drawImage(img[code], c * CELL_SIZE, r * CELL_SIZE, this);
                    }
                }
            }
        };
        boardPanel.setPreferredSize(new Dimension(
            model.getCols() * CELL_SIZE,
            model.getRows() * CELL_SIZE
        ));

        // Mouse listener to handle clicks on the board
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ignore clicks if game already ended
                if (model.isGameOver() || model.isGameWon()) return;

                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;

                if (SwingUtilities.isRightMouseButton(e)) {
                    // right-click: toggle flag
                    model.markCell(row, col);
                    flagsLabel.setText("Flags: " + model.getFlagsLeft());
                } else {
                    // left-click: reveal cell
                    model.revealCell(row, col);
                    treasureLabel.setText("Treasures: " + model.getTreasureCount());

                    if (model.isGameOver()) {
                        // stop timer and reveal all mines on loss
                        swingTimer.stop();
                        revealAllMines();
                        boardPanel.repaint();
                        JOptionPane.showMessageDialog(GUIView.this, "You hit a mine! Game Over.");
                        dispose();
                        SwingUtilities.invokeLater(() -> Launcher.main(new String[0]));
                        return;
                    }
                    if (model.isGameWon()) {
                        // stop timer and congratulate on win
                        swingTimer.stop();
                        JOptionPane.showMessageDialog(GUIView.this,
                            "Congratulations! You won in " + secondsElapsed + " seconds.");
                        dispose();
                        SwingUtilities.invokeLater(() -> Launcher.main(new String[0]));
                        return;
                    }
                }
                boardPanel.repaint();  // refresh board state
            }
        });

        // Layout setup
        setLayout(new BorderLayout(0, 5));
        add(topPanel,    BorderLayout.NORTH);
        add(boardPanel,  BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);  // center on screen
        setVisible(true);
    }

    /**
     * Load tile images (0.png to 13.png) from the resources folder.
     */
    private void loadIcons() {
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = new ImageIcon("src/resources/" + i + ".png").getImage();
        }
    }

    /**
     * Reveal all mines on the board (used when the player loses).
     */
    private void revealAllMines() {
        for (Cell[] rowArr : model.getGrid()) {
            for (Cell cell : rowArr) {
                if (cell.isMine()) {
                    cell.setRevealed(true);
                }
            }
        }
    }

    @Override
    public void display(BoardModel m) {
        // Not used in GUI mode; GUI updates automatically via repaint
    }

    @Override
    public Move promptMove() {
        // Not used in GUI mode
        return null;
    }
}
