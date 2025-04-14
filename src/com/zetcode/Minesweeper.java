package com.zetcode;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Minesweeper extends JFrame {

    private JLabel statusbar;

    // Default constructor (uses intermediate difficulty)
    public Minesweeper() {
        this(Board.INTERMEDIATE); // Default to intermediate
    }

    // New constructor with difficulty parameter
    public Minesweeper(int difficulty) {
        initUI(difficulty);
    }

    private void initUI(int difficulty) {
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        
        // Create board with selected difficulty
        add(new Board(statusbar, difficulty));

        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        // Check if we should show difficulty selection
        if (args.length > 0 && args[0].equals("--select-difficulty")) {
            EventQueue.invokeLater(() -> {
                new DifficultySelection().setVisible(true);
            });
        } else {
            // Default behavior (direct launch with intermediate)
            EventQueue.invokeLater(() -> {
                new Minesweeper().setVisible(true);
            });
        }
    }
}