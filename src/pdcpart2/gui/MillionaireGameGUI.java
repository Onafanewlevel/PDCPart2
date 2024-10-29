
package pdcpart2.gui;

import pdcpart2.dao.GameResultDAO;
import pdcpart2.interfaces.TimerListener;
import pdcpart2.interfaces.GameControl;
import pdcpart2.lifelines.Hint;
import pdcpart2.lifelines.Lifeline;
import pdcpart2.lifelines.FiftyFifty;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import pdcpart2.util.TimeUtil;
import pdcpart2.model.Player;
import pdcpart2.model.PrizeLevel;
import pdcpart2.model.Question;
import pdcpart2.util.QuestionLoader;
import pdcpart2.util.DatabaseInitializer;
import pdcpart2.util.FontLoader;
import pdcpart2.model.GameResult;

/**
 * MillionaireGameGUI manages the main game interface, handling questions,
 * options, scores, lifelines, and player interactions using the Player object.
 *
 * Implements the GameControl and TimerListener interfaces to control game flow
 * and respond to timer expiration events.
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class MillionaireGameGUI extends JFrame implements GameControl, TimerListener {

    private JTextArea questionTextArea;
    private JButton[] optionButtons = new JButton[4];
    private JLabel scoreLabel;
    private JLabel playerLabel;
    private JLabel messageLabel;
    private JLabel countdownLabel;
    private DatabaseInitializer dbInitializer;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Lifeline fiftyFiftyLifeline;
    private Lifeline hintLifeline;
    private TimeUtil countdownTimer;
    private Player player;

    // Lifeline buttons
    private JButton fiftyFiftyButton;
    private JButton hintButton;

    // Additional Buttons
    private JButton quitGameButton;
    private JButton resetGameButton;

    // Database connection parameters for Embedded Mode
    private static final String DATABASE_PATH = "QuestionDB"; // Relative path to the database

    // Custom font reference
    private Font customFont;

    // GameResultDAO instance
    private GameResultDAO gameResultDAO;

    /**
     * Constructor to initialize the MillionaireGameGUI.
     *
     * @param player The Player object containing player information.
     */
    public MillionaireGameGUI(Player player) {
        this.player = player; // Initialize with Player object
        fiftyFiftyLifeline = new FiftyFifty();
        hintLifeline = new Hint();

        // Initialize GameResultDAO
        gameResultDAO = new GameResultDAO();

        // Load custom font using FontLoader
        customFont = FontLoader.loadFont("src/pdcpart2/styles/fonts/MesloLGS NF Regular.ttf", 18f); // Adjust path as needed

        // Frame setup
        setTitle("Who Wants to be a Millionaire");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true); // Allow frame to be resizable
        setMinimumSize(new Dimension(800, 600)); // Set minimum size

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Initialize and populate the database
        dbInitializer = DatabaseInitializer.getInstance(DATABASE_PATH);

        // Load questions from the embedded database
        QuestionLoader reader = new QuestionLoader(DATABASE_PATH);
        questions = reader.getQuestions();

        // Initialize GUI components
        initializeGUIComponents();

        // Start the game
        StartGame();

        // Add a window listener to shut down the database when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dbInitializer.shutdownDatabase();
                System.exit(0); // Ensure the application exits after shutdown
            }
        });

        // Show frame
        setVisible(true);
    }

    /**
     * Initializes all GUI components and layouts.
     */
    private void initializeGUIComponents() {
        // Top Panel: Player Info, Score, Countdown
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.NORTH);

        // Center Panel: Question and Options
        JPanel questionPanel = createQuestionPanel();
        add(questionPanel, BorderLayout.CENTER);

        // Bottom Panel: Lifelines, Additional Buttons, and Messages
        JPanel lifelinePanel = createLifelinePanel();
        add(lifelinePanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the information panel containing player info, score, and countdown.
     *
     * @return JPanel containing the info components.
     */
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        infoPanel.setBackground(new Color(0, 102, 204)); // Example color: Blue

        // Player Label
        playerLabel = new JLabel("Player: " + player.getName());
        playerLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        playerLabel.setForeground(Color.WHITE);
        infoPanel.add(playerLabel);

        // Score Label
        scoreLabel = new JLabel("Score: $" + player.getScore());
        scoreLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        scoreLabel.setForeground(Color.WHITE);
        infoPanel.add(scoreLabel);

        // Countdown Label
        countdownLabel = new JLabel("Time left: 15");
        countdownLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        countdownLabel.setForeground(Color.WHITE);
        infoPanel.add(countdownLabel);

        return infoPanel;
    }

    /**
     * Creates the question panel containing the question text and answer options.
     *
     * @return JPanel containing the question and options.
     */
    private JPanel createQuestionPanel() {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBackground(new Color(245, 245, 245)); // Example color: White Smoke

        // Question Text Area
        questionTextArea = new JTextArea();
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFont(customFont.deriveFont(18f));
        questionTextArea.setFocusable(false);
        questionTextArea.setBackground(new Color(245, 245, 245));
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        questionPanel.add(scrollPane, BorderLayout.NORTH);

        // Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 2, 20, 20)); // 2x2 grid with spacing
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setFont(customFont.deriveFont(Font.PLAIN, 16f));
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setBackground(new Color(173, 216, 230)); // Example color: Light Blue
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].setMargin(new Insets(10, 10, 10, 10));
            optionsPanel.add(optionButtons[i]);
        }

        // Add action listeners for answer buttons
        for (JButton button : optionButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton sourceButton = (JButton) e.getSource();
                    String selectedAnswer = sourceButton.getActionCommand();
                    handleAnswer(selectedAnswer);
                }
            });
        }

        questionPanel.add(optionsPanel, BorderLayout.CENTER);

        return questionPanel;
    }

    /**
     * Creates the lifeline panel containing lifeline buttons, additional buttons, and message feedback.
     *
     * @return JPanel containing lifelines, additional buttons, and messages.
     */
    private JPanel createLifelinePanel() {
        JPanel lifelinePanel = new JPanel();
        lifelinePanel.setLayout(new BorderLayout());
        lifelinePanel.setBackground(new Color(0, 102, 204)); // Example color: Blue

        // Lifelines Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonsPanel.setBackground(new Color(0, 102, 204));

        fiftyFiftyButton = new JButton("50:50");
        fiftyFiftyButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        fiftyFiftyButton.setFocusPainted(false);
        fiftyFiftyButton.setBackground(new Color(255, 165, 0)); // Example color: Orange
        fiftyFiftyButton.setForeground(Color.BLACK);
        fiftyFiftyButton.setPreferredSize(new Dimension(100, 40));

        hintButton = new JButton("Hint");
        hintButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        hintButton.setFocusPainted(false);
        hintButton.setBackground(new Color(60, 179, 113)); // Example color: Medium Sea Green
        hintButton.setForeground(Color.BLACK);
        hintButton.setPreferredSize(new Dimension(100, 40));

        // Add lifeline buttons to the panel
        buttonsPanel.add(fiftyFiftyButton);
        buttonsPanel.add(hintButton);

        // Additional Buttons Panel
        JPanel additionalButtonsPanel = new JPanel();
        additionalButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        additionalButtonsPanel.setBackground(new Color(0, 102, 204));

        quitGameButton = new JButton("Quit Game");
        quitGameButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        quitGameButton.setFocusPainted(false);
        quitGameButton.setBackground(new Color(220, 20, 60)); // Example color: Crimson
        quitGameButton.setForeground(Color.BLACK);
        quitGameButton.setPreferredSize(new Dimension(120, 40));

        resetGameButton = new JButton("Reset Game");
        resetGameButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        resetGameButton.setFocusPainted(false);
        resetGameButton.setBackground(new Color(30, 144, 255)); // Example color: Dodger Blue
        resetGameButton.setForeground(Color.BLACK);
        resetGameButton.setPreferredSize(new Dimension(120, 40));

        // Add additional buttons to the panel
        additionalButtonsPanel.add(quitGameButton);
        additionalButtonsPanel.add(resetGameButton);

        // Combine Lifeline and Additional Buttons Panels
        JPanel combinedButtonsPanel = new JPanel();
        combinedButtonsPanel.setLayout(new GridLayout(1, 2, 50, 10)); // Two columns: lifelines and additional buttons
        combinedButtonsPanel.setBackground(new Color(0, 102, 204));

        combinedButtonsPanel.add(buttonsPanel);
        combinedButtonsPanel.add(additionalButtonsPanel);

        lifelinePanel.add(combinedButtonsPanel, BorderLayout.NORTH);

        // Message Label
        messageLabel = new JLabel("Select your answer or use a lifeline.");
        messageLabel.setFont(customFont.deriveFont(Font.ITALIC, 14f));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lifelinePanel.add(messageLabel, BorderLayout.SOUTH);

        // Add action listeners for lifeline buttons
        fiftyFiftyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fiftyFiftyLifeline.isUsed()) {
                    fiftyFiftyLifeline.useLifeline(questions.get(currentQuestionIndex), optionButtons, messageLabel);
                    fiftyFiftyButton.setEnabled(false);
                } else {
                    showMessage("You have already used the 50:50 lifeline.");
                }
            }
        });

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hintLifeline.isUsed()) {
                    hintLifeline.useLifeline(questions.get(currentQuestionIndex), optionButtons, messageLabel);
                    hintButton.setEnabled(false);
                } else {
                    showMessage("You have already used the Hint lifeline.");
                }
            }
        });

        // Add action listeners for additional buttons
        quitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuitGame();
            }
        });

        resetGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleResetGame();
            }
        });

        return lifelinePanel;
    }

    /**
     * Shows a message in the message label.
     *
     * @param message The message to display.
     */
    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void StartGame() {
        loadNextQuestion();  // Start by loading the first question
    }

    @Override
    public void StopGame() {
        // Before showing the dialog, record the game result
        recordGameResult();

        // Ensure that this is executed on the EDT
        SwingUtilities.invokeLater(() -> {
            // Create a message based on whether the player won or lost
            String message;
            if (currentQuestionIndex >= questions.size()) {
                message = "Congratulations " + player.getName() + "! You won $" + player.getScore() + ".\nDo you want to play again?";
            } else {
                message = "Game Over! You won $" + player.getScore() + ".\nDo you want to play again?";
            }

            // Show a confirmation dialog with Yes and No options
            int response = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                // Player chose to play again
                // Dispose of the current game window
                this.dispose();

                // Open the StartScreenGUI
                new StartScreenGUI();
            } else {
                // Player chose not to play again; exit the application 
                dbInitializer.shutdownDatabase();
                System.exit(0);               
            }
        });
    }

    /**
     * Handles the Quit Game functionality.
     * Prompts the user for confirmation. If confirmed, shuts down the database and stops the game.
     */
    private void handleQuitGame() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit the game?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dbInitializer.shutdownDatabase();
            StopGame();
        }
    }

    /**
     * Handles the Reset Game functionality.
     * Prompts the user for confirmation. If confirmed, does not save the game result and returns to StartScreenGUI.
     */
    private void handleResetGame() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to reset the game? Your current progress will not be saved.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Dispose of the current game window without saving the result
            countdownTimer.StopTimer();
            this.dispose();

            // Open the StartScreenGUI
            new StartScreenGUI();
        }
    }

    /**
     * This method is called when the countdown timer expires. It ends the game
     * by invoking StopGame().
     */
    @Override
    public void timerExpired() {
        // End the game when the timer runs out
        StopGame();
    }

    /**
     * Loads the next question in the list.
     */
    private void loadNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionTextArea.setText(currentQuestion.getQuestionText());

            // Set button texts with HTML for wrapping and assign ActionCommands
            optionButtons[0].setText(formatButtonText(currentQuestion.getOptionA()));
            optionButtons[0].setActionCommand(currentQuestion.getOptionA());

            optionButtons[1].setText(formatButtonText(currentQuestion.getOptionB()));
            optionButtons[1].setActionCommand(currentQuestion.getOptionB());

            optionButtons[2].setText(formatButtonText(currentQuestion.getOptionC()));
            optionButtons[2].setActionCommand(currentQuestion.getOptionC());

            optionButtons[3].setText(formatButtonText(currentQuestion.getOptionD()));
            optionButtons[3].setActionCommand(currentQuestion.getOptionD());

            // Optionally, set tooltips to show full text
            optionButtons[0].setToolTipText(currentQuestion.getOptionA());
            optionButtons[1].setToolTipText(currentQuestion.getOptionB());
            optionButtons[2].setToolTipText(currentQuestion.getOptionC());
            optionButtons[3].setToolTipText(currentQuestion.getOptionD());

            // Re-enable all option buttons for the new question
            for (JButton button : optionButtons) {
                button.setEnabled(true);
            }

            // Re-enable lifeline buttons if not used
            if (!fiftyFiftyLifeline.isUsed()) {
                fiftyFiftyButton.setEnabled(true);
            }
            if (!hintLifeline.isUsed()) {
                hintButton.setEnabled(true);
            }

            // Initialize and start the countdown timer (e.g., 15 seconds to answer)
            countdownTimer = new TimeUtil(false, countdownLabel, this); // Pass 'this' as the listener
            countdownTimer.StartTimer();

            // Reset message label for new question
            messageLabel.setText("Select your answer or use a lifeline.");
        } else {
            StopGame();
        }
    }

    /**
     * Handles the answer selected by the player.
     *
     * @param selectedAnswer The answer selected by the player.
     */
    private void handleAnswer(String selectedAnswer) {
        if (countdownTimer != null) {
            countdownTimer.StopTimer();
        }

        // Pause for a brief moment before showing the result
        // Consider using a Swing Timer instead of Thread.sleep to avoid freezing the UI
        countdownTimer.pause(2000);

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            int prizeAmount = PrizeLevel.getAmountByQuestionLevel(currentQuestionIndex + 1);
            player.setScore(prizeAmount); // Update score in Player object
            scoreLabel.setText("Score: $" + player.getScore());
            JOptionPane.showMessageDialog(this, "Correct! You won $" + prizeAmount);
        } else {
            JOptionPane.showMessageDialog(this, "Wrong answer! The correct answer was: " + currentQuestion.getCorrectAnswer());
            StopGame(); // End the game immediately on wrong answer
            return; // Exit the method to prevent proceeding to the next question
        }

        currentQuestionIndex++;
        loadNextQuestion();
    }

    /**
     * Formats the button text with HTML to enable text wrapping and alignment.
     *
     * @param text The original button text.
     * @return The formatted HTML string.
     */
    private String formatButtonText(String text) {
        // Using table layout to center text vertically and horizontally
        return "<html>"
                + "<div style='display: table; height: 100%; width: 150px; text-align: center;'>"
                + "<span style='display: table-cell; vertical-align: middle;'>" + text + "</span>"
                + "</div>"
                + "</html>";
    }

    /**
     * Records the game result to the database.
     */
    private void recordGameResult() {
        try {
            int lastQuestionIndex = currentQuestionIndex;
            // If the game was completed successfully, adjust the index
            if (currentQuestionIndex >= questions.size()) {
                lastQuestionIndex = questions.size();
            }

            GameResult gameResult = new GameResult(
                    player.getName(),
                    player.getScore(),
                    lastQuestionIndex
            );

            gameResultDAO.insertGameResult(gameResult);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to record game result.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays a message to the user in the message label.
     *
     * @param message The message to display.
     */
    private void displayMessage(String message) {
        messageLabel.setText(message);
    }
}





