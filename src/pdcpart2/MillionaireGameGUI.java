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
import java.util.List;

public class MillionaireGameGUI extends JFrame implements GameControl {
    private JTextArea questionTextArea;
    private JButton[] optionButtons = new JButton[4];
    private JLabel scoreLabel;
    private JLabel playerLabel;
    private JLabel messageLabel;
    private JLabel countdownLabel;
    private int score = 0;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Lifeline fiftyFiftyLifeline;
    private Lifeline hintLifeline;
    private CountdownTimer countdownTimer;
    private String playerName;

    // Lifeline buttons
    private JButton fiftyFiftyButton;
    private JButton hintButton;

    public MillionaireGameGUI(String playerName) {
        this.playerName = playerName;
        fiftyFiftyLifeline = new FiftyFifty();
        hintLifeline = new Hint();

        // Frame setup
        setTitle("Who Wants to be a Millionaire");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Load questions
        QuestionFileReader reader = new QuestionFileReader("src/pdcpart2/questions.txt");
        questions = reader.getQuestions();

        // Score and player display
        scoreLabel = new JLabel("Score: $" + score);
        playerLabel = new JLabel("Player: " + playerName);
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.add(scoreLabel);
        infoPanel.add(playerLabel);

        // Countdown label
        countdownLabel = new JLabel("Time left: ");
        infoPanel.add(countdownLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Message label for feedback and lifeline info
        messageLabel = new JLabel("Messages will appear here");
        add(messageLabel, BorderLayout.SOUTH);

        // Question display using JTextArea with JScrollPane
        questionTextArea = new JTextArea();
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFont(new Font("Serif", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Options buttons
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 2));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton("Option " + (i + 1));
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.SOUTH);

        // Event listeners for buttons
        for (JButton button : optionButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleAnswer(button.getText());
                }
            });
        }

        // Lifeline buttons
        JPanel lifelinePanel = new JPanel();
        fiftyFiftyButton = new JButton("50:50");
        hintButton = new JButton("Hint");

        lifelinePanel.add(fiftyFiftyButton);
        lifelinePanel.add(hintButton);

        add(lifelinePanel, BorderLayout.EAST);

        // Add action listeners for lifeline buttons
        fiftyFiftyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fiftyFiftyLifeline.isUsed()) {
                    fiftyFiftyLifeline.useLifeline(questions.get(currentQuestionIndex), optionButtons, messageLabel);
                    fiftyFiftyButton.setEnabled(false);
                }
            }
        });

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hintLifeline.isUsed()) {
                    hintLifeline.useLifeline(questions.get(currentQuestionIndex), optionButtons, messageLabel);
                    hintButton.setEnabled(false);
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
        JOptionPane.showMessageDialog(this, "The game has ended.");
        System.exit(0);
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionTextArea.setText(currentQuestion.getQuestionText());
            optionButtons[0].setText(currentQuestion.getOptionA());
            optionButtons[1].setText(currentQuestion.getOptionB());
            optionButtons[2].setText(currentQuestion.getOptionC());
            optionButtons[3].setText(currentQuestion.getOptionD());

            // Re-enable all option buttons for the new question
            for (JButton button : optionButtons) {
                button.setEnabled(true);
            }

            // Start countdown for the question (e.g., 15 seconds to answer)
            countdownTimer = new CountdownTimer(false, countdownLabel);
            countdownTimer.StartTimer();

            // Reset message label for new question
            messageLabel.setText("Select your answer or use a lifeline.");
        } else {
            StopGame();
        }
    }

    private void handleAnswer(String answer) {
        if (countdownTimer != null) {
            countdownTimer.StopTimer();
        }

        TimeUtil.pause(2000);  // Pause for 2 seconds before showing the result

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (answer.equals(currentQuestion.getCorrectAnswer())) {
            int prizeAmount = PrizeLevel.getAmountByQuestionLevel(currentQuestionIndex + 1);
            score = prizeAmount;
            scoreLabel.setText("Score: $" + score);
            JOptionPane.showMessageDialog(this, "Correct! You won $" + prizeAmount);
        } else {
            JOptionPane.showMessageDialog(this, "Wrong answer!");
        }

        currentQuestionIndex++;
        loadNextQuestion();
    }
}


