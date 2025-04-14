package com.zetcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DifficultySelection extends JFrame {

    public DifficultySelection() {
        initUI();
    }

    private void initUI() {
        setTitle("Minesweeper - Select Difficulty");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));
        setSize(300, 300);
        setLocationRelativeTo(null);
        setResizable(false);


        getContentPane().setBackground(new Color(0,0,0));
        JLabel titleLabel = new JLabel("Select Difficulty", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Press Start 2P", Font.PLAIN, 14)); // Pixel font
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);


        
        JButton beginnerBtn = createDifficultyButton("Beginner", Board.BEGINNER);
        beginnerBtn.setBackground(new Color(255,135,191));
        beginnerBtn.setFont(new Font("Press Start 2P", Font.PLAIN, 14)); // Pixel font

        JButton intermediateBtn = createDifficultyButton("Intermediate", Board.INTERMEDIATE);
        intermediateBtn.setBackground(new Color(255,135,191));
        intermediateBtn.setFont(new Font("Press Start 2P", Font.PLAIN, 14)); // Pixel font

        JButton expertBtn = createDifficultyButton("Expert", Board.EXPERT);
        expertBtn.setBackground(new Color(255,135,191));
        expertBtn.setFont(new Font("Press Start 2P", Font.PLAIN, 14)); // Pixel font


        add(beginnerBtn);
        add(intermediateBtn);
        add(expertBtn);
    }

    private JButton createDifficultyButton(String text, int difficulty) {
        JButton button = new JButton(text);
        button.addActionListener((ActionEvent e) -> {
            dispose(); // Close this window
            startGame(difficulty);
        });
        return button;
    }

    private void startGame(int difficulty) {
        EventQueue.invokeLater(() -> {
            Minesweeper game = new Minesweeper(difficulty);
            game.setVisible(true);
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            DifficultySelection selection = new DifficultySelection();
            selection.setVisible(true);
        });
    }
}
/*
 * 
 * 
 */