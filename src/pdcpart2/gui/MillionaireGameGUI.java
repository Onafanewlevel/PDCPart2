/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.gui;

/**
 * MillionaireGameGUI manages the main game interface, handling questions, options, scores,
 * lifelines, and player interactions using the Player object.
 * 
 * Implements the GameControl and TimerListener interfaces to control game flow and respond
 * to timer expiration events.
 * 
 * Author: 
 */

import pdcpart2.interfaces.TimerListener;
import pdcpart2.interfaces.GameControl;
import pdcpart2.lifelines.Hint;
import pdcpart2.lifelines.Lifeline;
import pdcpart2.lifelines.FiftyFifty;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import pdcpart2.util.CountdownTimer;
import pdcpart2.model.Player;
import pdcpart2.model.PrizeLevel;
import pdcpart2.model.Question;
import pdcpart2.util.QuestionDatabaseLoader;

public class MillionaireGameGUI extends JFrame implements GameControl, TimerListener {
    private JTextArea questionTextArea;
    private JButton[] optionButtons = new JButton[4];
    private JLabel scoreLabel;
    private JLabel playerLabel;
    private JLabel messageLabel;
    private JLabel countdownLabel;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Lifeline fiftyFiftyLifeline;
    private Lifeline hintLifeline;
    private CountdownTimer countdownTimer;
    private Player player; // Player object

    // Lifeline buttons
    private JButton fiftyFiftyButton;
    private JButton hintButton;

    // Database connection parameters
    private static final String DATABASE_URL = "jdbc:derby://localhost:1527/QuestionsDB";
    private static final String USERNAME = "pdc2";
    private static final String PASSWORD = "pdc2";

    // Custom font reference
    private Font customFont;

    public MillionaireGameGUI(Player player) {
        this.player = player; // Initialize with Player object
        fiftyFiftyLifeline = new FiftyFifty();
        hintLifeline = new Hint();

        // Load custom font
        customFont = loadFont("src/resources/fonts/MesloLGS NF Regular.ttf", 18f);

        // Frame setup
        setTitle("Who Wants to be a Millionaire");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true); // Allow frame to be resizable
        setMinimumSize(new Dimension(800, 500)); // Set minimum size

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Load questions from the database
        QuestionDatabaseLoader reader = new QuestionDatabaseLoader(DATABASE_URL, USERNAME, PASSWORD);
        questions = reader.getQuestions();

        // Score and player display with custom font
        scoreLabel = new JLabel("Score: $" + player.getScore());
        scoreLabel.setFont(customFont.deriveFont(16f));
        playerLabel = new JLabel("Player: " + player.getName());
        playerLabel.setFont(customFont.deriveFont(16f));
        
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.add(scoreLabel);
        infoPanel.add(playerLabel);

        // Countdown label with custom font
        countdownLabel = new JLabel("Time left: ");
        countdownLabel.setFont(customFont.deriveFont(16f));
        infoPanel.add(countdownLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Message label for feedback and lifeline info
        messageLabel = new JLabel("Messages will appear here");
        messageLabel.setFont(customFont.deriveFont(14f));

        // Question display using JTextArea with JScrollPane and custom font
        questionTextArea = new JTextArea();
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFont(customFont.deriveFont(18f));
        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Options buttons with custom font
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 2, 10, 10)); // Added spacing for better UI
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setFont(customFont.deriveFont(16f));
            // Removed vertical alignment setting to allow centering
            optionButtons[i].setMargin(new Insets(10, 10, 10, 10)); // Optional: Adjust padding
            optionsPanel.add(optionButtons[i]);
        }

        // Lifeline buttons with custom font
        JPanel lifelinePanel = new JPanel();
        lifelinePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        fiftyFiftyButton = new JButton("50:50");
        fiftyFiftyButton.setFont(customFont.deriveFont(14f));
        hintButton = new JButton("Hint");
        hintButton.setFont(customFont.deriveFont(14f));

        lifelinePanel.add(fiftyFiftyButton);
        lifelinePanel.add(hintButton);

        // South Panel to hold both options and lifelines
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(optionsPanel, BorderLayout.CENTER);
        southPanel.add(lifelinePanel, BorderLayout.EAST);
        southPanel.add(messageLabel, BorderLayout.SOUTH); // Place messageLabel below lifelines

        add(southPanel, BorderLayout.SOUTH);

        // Event listeners for answer buttons
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

        // Add action listeners for lifeline buttons
        fiftyFiftyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fiftyFiftyLifeline.isUsed()) {
                    fiftyFiftyLifeline.useLifeline(questions.get(currentQuestionIndex), optionButtons, messageLabel);
                    fiftyFiftyButton.setEnabled(false);
                } else {
                    messageLabel.setText("You have already used the 50:50 lifeline.");
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
                    messageLabel.setText("You have already used the Hint lifeline.");
                }
            }
        });

        // Start the game
        StartGame();

        // Show frame
        setVisible(true);
    }

    @Override
    public void StartGame() {
        loadNextQuestion();  // Start by loading the first question
    }

    @Override
    public void StopGame() {
        // Ensure that this is executed on the EDT
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Congratulations " + player.getName() + "! You won $" + player.getScore());
            System.exit(0);
        });
    }

    /**
     * This method is called when the countdown timer expires.
     * It ends the game by invoking StopGame().
     */
    @Override
    public void timerExpired() {
        // End the game when the timer runs out
        StopGame();
    }

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

            // Start countdown for the question (e.g., 15 seconds to answer)
            countdownTimer = new CountdownTimer(false, countdownLabel, this); // Pass 'this' as the listener
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

        // Pause for 2 seconds before showing the result
        try {
            Thread.sleep(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            int prizeAmount = PrizeLevel.getAmountByQuestionLevel(currentQuestionIndex + 1);
            player.setScore(prizeAmount); // Update score in Player object
            scoreLabel.setText("Score: $" + player.getScore());
            JOptionPane.showMessageDialog(this, "Correct! You won $" + prizeAmount);
            System.out.println("Selected Answer: " + selectedAnswer);
            System.out.println("Correct Answer: " + currentQuestion.getCorrectAnswer());
        } else {
            JOptionPane.showMessageDialog(this, "Wrong answer! The correct answer was: " + currentQuestion.getCorrectAnswer());
            System.out.println("Selected Answer: " + selectedAnswer);
            System.out.println("Correct Answer: " + currentQuestion.getCorrectAnswer());
            StopGame(); // End the game immediately on wrong answer
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
     * Loads a custom font from the specified path.
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
}

