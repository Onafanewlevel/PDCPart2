/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603projectpart2;

/**
 *
 * @author setefanomuller
 */
import javax.swing.*;
import java.util.Random;

public class FiftyFifty extends Lifeline {

    /**
     * Implements the 50:50 lifeline logic by removing two incorrect answers.
     * 
     * @param question The current Question object to access correct and incorrect options.
     * @param optionButtons The array of option buttons in the GUI.
     * @param messageLabel The label to display messages in the GUI.
     */
    @Override
    public void useLifeline(Question question, JButton[] optionButtons, JLabel messageLabel) {
        if (isUsed) {
            messageLabel.setText("You have already used the 50:50 lifeline.");
            return;
        }

        messageLabel.setText("50:50 Lifeline activated!");
        Random random = new Random();
        String correctAnswer = question.getCorrectAnswer();
        int removed = 0;

        // Disable two incorrect options randomly
        while (removed < 2) {
            int randIndex = random.nextInt(optionButtons.length);
            String optionText = optionButtons[randIndex].getText();
            if (!optionText.equals(correctAnswer) && optionButtons[randIndex].isEnabled()) {
                optionButtons[randIndex].setEnabled(false); // Disable the incorrect option
                removed++;
            }
        }

        isUsed = true;  // Mark the lifeline as used
    }
}

