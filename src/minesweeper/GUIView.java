
// GUIView.java
package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GUIView extends JFrame implements IView {
    private static final int NUM_IMAGES = 14;
    private final BoardModel model;
    private final Image[] img = new Image[NUM_IMAGES];
    private final int CELL_SIZE;

    private final JLabel flagsLabel    = new JLabel();
    private final JLabel treasureLabel = new JLabel();
    private final JLabel timerLabel    = new JLabel();
    private int secondsElapsed = 0;
    private final Timer swingTimer;

    public GUIView(BoardModel model) {
        super("Minesweeper");
        this.model = model;

        loadIcons();
        CELL_SIZE = img[0].getWidth(null);

        // --- Menu Bar ---
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Game...");
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

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

        // --- Top Panel ---
        flagsLabel.setText("Flags: " + model.getFlagsLeft());
        treasureLabel.setText("Treasures: " + model.getTreasureCount());
        timerLabel.setText("Time: 0");

        swingTimer = new Timer(1000, ev -> timerLabel.setText("Time: " + ++secondsElapsed));
        swingTimer.start();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        topPanel.add(flagsLabel);
        topPanel.add(treasureLabel);
        topPanel.add(timerLabel);

        // --- Board Panel ---
        JPanel boardPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int r = 0; r < model.getRows(); r++) {
                    for (int c = 0; c < model.getCols(); c++) {
                        Cell cell = model.getGrid()[r][c];
                        int code;
                        if (!cell.isRevealed()) code = cell.isFlagged() ? 11 : 0;
                        else if (cell.isMine()) code = 9;
                        else if (cell.hasTreasure()) code = 13;
                        else code = cell.getAdjMines();
                        g.drawImage(img[code], c * CELL_SIZE, r * CELL_SIZE, this);
                    }
                }
            }
        };
        boardPanel.setPreferredSize(new Dimension(
            model.getCols() * CELL_SIZE,
            model.getRows() * CELL_SIZE
        ));

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (model.isGameOver() || model.isGameWon()) return;
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;
                if (SwingUtilities.isRightMouseButton(e)) {
                    model.markCell(row, col);
                    flagsLabel.setText("Flags: " + model.getFlagsLeft());
                } else {
                    model.revealCell(row, col);
                    treasureLabel.setText("Treasures: " + model.getTreasureCount());
                    if (model.isGameOver()) {
                        swingTimer.stop();
                        revealAllMines(); boardPanel.repaint();
                        JOptionPane.showMessageDialog(GUIView.this, "You hit a mine! Game Over.");
                        dispose(); SwingUtilities.invokeLater(() -> Launcher.main(new String[0]));
                        return;
                    }
                    if (model.isGameWon()) {
                        swingTimer.stop();
                        JOptionPane.showMessageDialog(GUIView.this,
                            "Congratulations! You won in " + secondsElapsed + " seconds.");
                        dispose(); SwingUtilities.invokeLater(() -> Launcher.main(new String[0]));
                        return;
                    }
                }
                boardPanel.repaint();
            }
        });

        setLayout(new BorderLayout(0, 5));
        add(topPanel,    BorderLayout.NORTH);
        add(boardPanel,  BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack(); setLocationRelativeTo(null); setVisible(true);
    }

    private void loadIcons() {
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = new ImageIcon("src/resources/" + i + ".png").getImage();
        }
    }

    private void revealAllMines() {
        for (Cell[] rowArr : model.getGrid()) for (Cell cell : rowArr)
            if (cell.isMine()) cell.setRevealed(true);
    }

    @Override public void display(BoardModel m) {}
    @Override public Move promptMove() { return null; }
}
