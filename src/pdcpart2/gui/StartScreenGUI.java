package pdcpart2.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pdcpart2.model.Player;
import pdcpart2.util.DatabaseInitializer;
import pdcpart2.util.FontLoader; // Import the FontLoader class

/**
 * StartScreenGUI prompts the player to enter their name before starting the
 * game. It creates a Player object and passes it to the MillionaireGameGUI.
 * Utilizes Derby Embedded mode for database connectivity.
 *
 * Author: Setefano Muller 16924823
 */
public class StartScreenGUI extends JFrame {

    private DatabaseInitializer dbInitializer;
    private JTextField nameField;
    private JLabel titleLabel;
    private JButton startButton;
    private Font customFont;

    public StartScreenGUI() {
        // Load custom font using FontLoader
        customFont = FontLoader.loadFont("src/pdcpart2/styles/fonts/MesloLGS NF Regular.ttf", 24f); // Adjust size as needed

        // Frame setup
        setTitle("Who Wants to Become a Millionaire");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Title Label with custom font
        titleLabel = new JLabel("Who Wants to Become a Millionaire", SwingConstants.CENTER);
        titleLabel.setFont(customFont.deriveFont(40f)); // Apply custom font
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
        startButton.setFont(customFont.deriveFont(18f));
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

        // Display the splash screen
        setVisible(true);
    }

    public static void main(String[] args) {
        new StartScreenGUI();
    }
}
