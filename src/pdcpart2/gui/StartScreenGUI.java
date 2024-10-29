
package pdcpart2.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import pdcpart2.dao.GameResultDAO;
import pdcpart2.model.GameResult;
import pdcpart2.model.Player;
import pdcpart2.util.DatabaseInitializer;
import pdcpart2.util.FontLoader;

/**
 * StartScreenGUI prompts the player to enter their name before starting the
 * game. It creates a Player object and passes it to the MillionaireGameGUI.
 * Utilizes Derby Embedded mode for database connectivity.
 *
 * Additionally, it displays a table of previous game results.
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class StartScreenGUI extends JFrame {

    private DatabaseInitializer dbInitializer;
    private JTextField nameField;
    private JLabel titleLabel;
    private JButton startButton;
    private Font customFont;

    // Components for displaying game results
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    private GameResultDAO gameResultDAO;

    public StartScreenGUI() {
        // Load custom font using FontLoader
        customFont = FontLoader.loadFont("src/pdcpart2/styles/fonts/MesloLGS NF Regular.ttf", 24f); // Adjust size as needed

        // Initialize DAO
        gameResultDAO = new GameResultDAO();

        // Frame setup
        setTitle("Who Wants to Become a Millionaire");
        setSize(800, 600); // Adjusted size for better balance
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Initialize and populate the database
        dbInitializer = DatabaseInitializer.getInstance("QuestionDB");

        // Create and add main panels
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);

        // Display the splash screen
        setVisible(true);
    }

    /**
     * Creates the title panel.
     *
     * @return JPanel containing the title label.
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 102, 204)); // Example color: Blue
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        titleLabel = new JLabel("Who Wants to Become a Millionaire");
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 32f));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        return titlePanel;
    }

    /**
     * Creates the main content panel containing input and results.
     *
     * @return JPanel containing input and results sections.
     */
    private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(230, 230, 250)); // Example color: Lavender

        // Input Panel
        mainContentPanel.add(createInputPanel());
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer

        // Results Panel
        mainContentPanel.add(createResultsPanel());

        return mainContentPanel;
    }

    /**
     * Creates the input panel for player name and start button.
     *
     * @return JPanel containing name input and start button.
     */
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        inputPanel.setBackground(new Color(230, 230, 250)); // Match main content background

        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setFont(customFont.deriveFont(18f));
        nameLabel.setForeground(new Color(0, 0, 128)); // Example color: Navy

        nameField = new JTextField(15);
        nameField.setFont(customFont.deriveFont(16f));

        startButton = new JButton("Start Game");
        startButton.setFont(customFont.deriveFont(16f));
        startButton.setBackground(new Color(60, 179, 113));
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);

        // Add action listener for the start button
        startButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                Player player = new Player(playerName); // Create Player object
                dispose(); // Close the start screen
                new MillionaireGameGUI(player); // Start the main game with Player object
            } else {
                JOptionPane.showMessageDialog(StartScreenGUI.this, "Please enter your name to start the game.",
                        "Input Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(startButton);

        return inputPanel;
    }

    /**
     * Creates the results panel containing the game results table.
     *
     * @return JPanel containing the results table.
     */
    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBackground(new Color(230, 230, 250)); // Match main content background
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(72, 61, 139)), // Example color: Dark Slate Blue
                "Previous Game Results",
                SwingConstants.CENTER,
                SwingConstants.TOP,
                customFont.deriveFont(Font.BOLD, 20f),
                new Color(72, 61, 139)
        ));

        // Table columns
        String[] columnNames = {"Player Name", "Score", "Last Question Index", "Date & Time"};

        // Initialize table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Initialize JTable
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(customFont.deriveFont(14f));
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(customFont.deriveFont(Font.BOLD, 16f));
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        resultsTable.getTableHeader().setBackground(new Color(72, 61, 139)); // Dark Slate Blue
        resultsTable.setFillsViewportHeight(true);
        resultsTable.setBackground(new Color(245, 245, 245)); // White Smoke

        // Enable sorting
        resultsTable.setAutoCreateRowSorter(true);

        // Load game results and populate the table
        loadGameResults();

        // Add the table to a scroll pane
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 200)); // Reduced size

        resultsPanel.add(tableScrollPane, BorderLayout.CENTER);

        return resultsPanel;
    }

    /**
     * Loads game results from the database and populates the table.
     */
    private void loadGameResults() {
        try {
            List<GameResult> results = gameResultDAO.getAllGameResults();
            for (GameResult result : results) {
                Object[] rowData = {
                        result.getPlayerName(),
                        "$" + result.getScore(),
                        result.getLastQuestionIndex(),
                        result.getTimestamp().toString()
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load game results.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Initialize the database (optional, as it will be initialized in DatabaseInitializer)
        new StartScreenGUI();
    }
}
