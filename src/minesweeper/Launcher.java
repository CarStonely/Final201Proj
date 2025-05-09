package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Launcher provides the starting menu for the Minesweeper application.
 * Users can start a new game, load a saved game, enter testing mode with custom boards,
 * or play in a text-based mode. All UI operations run on the Swing Event Dispatch Thread.
 */
public class Launcher {

    /**
     * Entry point of the application. Sets up the main menu with buttons
     * for different play modes.
     *
     * @param args CLI arguments (unused)
     */
    public static void main(String[] args) {
        // Ensure GUI code runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame menu = new JFrame("Minesweeper Launcher");
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menu.setSize(400, 250);
            // 4 rows, 1 column layout for buttons with spacing
            menu.setLayout(new GridLayout(4, 1, 10, 10));

            // Create menu buttons
            JButton newGameBtn    = new JButton("Start New Game");
            JButton loadGameBtn   = new JButton("Load Saved Game");
            JButton testModeBtn   = new JButton("Enter Testing Mode");
            JButton textModeBtn   = new JButton("Play in Text-Based Mode");

            // --- New Game: choose difficulty and launch GUIView ---
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
                    // Initialize model with chosen parameters
                    BoardModel model = new BoardModel(
                        level.rows(), level.cols(), level.mines(), level.treasures()
                    );
                    menu.dispose();  // close launcher
                    new GUIView(model);  // open GUI mode
                }
            });

            // --- Load Saved Game: load CSV then launch GUIView ---
            loadGameBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(menu) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        BoardModel model = CSVUtils.loadFromCSV(file.getAbsolutePath());
                        menu.dispose();
                        new GUIView(model);
                    } catch (IOException ex) {
                        // Show error if loading fails
                        JOptionPane.showMessageDialog(menu,
                            "Failed to load game:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });

            // --- Testing Mode: load test board, validate, then launch GUIView ---
            testModeBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(menu) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        // Load custom board configuration (mines & treasures)
                        int[][] cells = CSVUtils.loadTestBoard(file.getAbsolutePath());
                        // Validate the test board structure
                        TestBoardValidator.validate(cells);
                        // Create model from test config
                        BoardModel model = BoardModel.fromTestConfig(cells);
                        menu.dispose();
                        new GUIView(model);
                    } catch (IOException | IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(menu,
                            "Invalid test board:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });

            // --- Text-Based Mode: choose difficulty then launch GameController with TextView ---
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
                    // Start game loop in text mode
                    new GameController(model, new TextView()).startGame();
                }
            });

            // Add buttons to the frame
            menu.add(newGameBtn);
            menu.add(loadGameBtn);
            menu.add(testModeBtn);
            menu.add(textModeBtn);

            // Center on screen and display launcher
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        });
    }
}
