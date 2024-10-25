/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

/**
 *
 * @author setefanomuller
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SplashScreenGUI extends JFrame {
    private JTextField nameField;
    private JLabel titleLabel;
    private JButton startButton;

    public SplashScreenGUI() {
        // Frame setup
        setTitle("Who Wants to Become a Millionaire");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Title Label
        titleLabel = new JLabel("Who Wants to Become a Millionaire", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Name Input Panel
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameField = new JTextField(15);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        add(namePanel, BorderLayout.CENTER);

        // Start Button
        startButton = new JButton("Start Game");
        add(startButton, BorderLayout.SOUTH);

        // Add action listener for the start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText();
                if (!playerName.isEmpty()) {
                    dispose(); // Close the splash screen
                    new MillionaireGameGUI(playerName); // Start the main game
                } else {
                    JOptionPane.showMessageDialog(SplashScreenGUI.this, "Please enter your name to start the game.");
                }
            }
        });

        // Display the splash screen
        setVisible(true);
    }

    public static void main(String[] args) {
        new SplashScreenGUI();
    }
}

