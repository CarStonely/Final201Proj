package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame menu = new JFrame("Minesweeper Launcher");
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menu.setSize(400, 250);
            menu.setLayout(new GridLayout(4, 1, 10, 10));

            JButton newGameBtn = new JButton("Start New Game");
            JButton loadGameBtn = new JButton("Load Saved Game");
            JButton testModeBtn = new JButton("Enter Testing Mode");
            JButton textModeBtn = new JButton("Play in Text-Based Mode");

            // --- New Game ---
            newGameBtn.addActionListener(e -> {
                Level level = (Level) JOptionPane.showInputDialog(
                    menu,
                    "Select difficulty:",
                    "New Game",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Level.values(),
                    Level.BEGINNER
                );
                if (level != null) {
                    BoardModel model = new BoardModel(
                        level.rows(), level.cols(), level.mines(), level.treasures()
                    );
                    menu.dispose();
                    new GUIView(model);
                }
            });

            // --- Load Saved Game ---
            loadGameBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(menu) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        BoardModel model = CSVUtils.loadFromCSV(file.getAbsolutePath());
                        menu.dispose();
                        new GUIView(model);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(menu,
                            "Failed to load game:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // --- Testing Mode ---
            testModeBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(menu) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        int[][] cells = CSVUtils.loadTestBoard(file.getAbsolutePath());
                        TestBoardValidator.validate(cells);
                        BoardModel model = BoardModel.fromTestConfig(cells);
                        menu.dispose();
                        new GUIView(model);
                    } catch (IOException | IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(menu,
                            "Invalid test board:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // --- Text-Based Mode ---
            textModeBtn.addActionListener(e -> {
                Level level = (Level) JOptionPane.showInputDialog(
                    menu,
                    "Select difficulty for text-based mode:",
                    "Text Mode",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Level.values(),
                    Level.BEGINNER
                );
                if (level != null) {
                    BoardModel model = new BoardModel(
                        level.rows(), level.cols(), level.mines(), level.treasures()
                    );
                    menu.dispose();
                    new GameController(model, new TextView()).startGame();
                }
            });

            // Add buttons
            menu.add(newGameBtn);
            menu.add(loadGameBtn);
            menu.add(testModeBtn);
            menu.add(textModeBtn);

            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        });
    }
}
