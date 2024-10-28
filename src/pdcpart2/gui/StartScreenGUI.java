/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import pdcpart2.model.Player;
import pdcpart2.util.DatabaseInitializer;

/**
 * StartScreenGUI prompts the player to enter their name before starting the game.
 * It creates a Player object and passes it to the MillionaireGameGUI.
 * Utilizes Derby Embedded mode for database connectivity.
 * 
 * Author: Setefano Muller 16924823
 */
public class StartScreenGUI extends JFrame {
    private JTextField nameField;
    private JLabel titleLabel;
    private JButton startButton;
    private Font customFont;

    public StartScreenGUI() {
        // Initialize the database
        DatabaseInitializer dbInitializer = new DatabaseInitializer("QuestionsDB");
        dbInitializer.initializeDatabase();
        dbInitializer.populateDatabase();

        // Load custom font
        customFont = loadFont("src/pdcpart2/styles/fonts/MesloLGS NF Regular.ttf", 24f); // Adjust size as needed

        // Frame setup
        setTitle("Who Wants to Become a Millionaire");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Title Label with custom font
        titleLabel = new JLabel("Who Wants to Become a Millionaire", SwingConstants.CENTER);
        titleLabel.setFont(customFont); // Apply custom font
        add(titleLabel, BorderLayout.NORTH);

        // Name Input Panel with custom font
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setFont(customFont.deriveFont(16f)); // Smaller size for labels
        nameField = new JTextField(15);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        add(namePanel, BorderLayout.CENTER);

        // Start Button with custom font
        startButton = new JButton("Start Game");
        startButton.setFont(customFont.deriveFont(18f)); // Adjust size for button
        add(startButton, BorderLayout.SOUTH);

        // Add action listener for the start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    Player player = new Player(playerName); // Create Player object
                    dispose(); // Close the splash screen
                    new MillionaireGameGUI(player); // Start the main game with Player object
                } else {
                    JOptionPane.showMessageDialog(StartScreenGUI.this, "Please enter your name to start the game.",
                            "Input Required", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Optional: Add a window listener to shut down the database when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dbInitializer.shutdownDatabase();
            }
        });

        // Display the splash screen
        setVisible(true);
    }

    /**
     * Method to load a custom font from the specified path.
     * 
     * @param path The path to the font file.
     * @param size The desired font size.
     * @return The loaded Font object.
     */
    private Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font); // Register the font with the graphics environment
            return font;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Serif", Font.PLAIN, (int) size); // Fallback to default font
        }
    }

    public static void main(String[] args) {
        new StartScreenGUI();
    }
}






