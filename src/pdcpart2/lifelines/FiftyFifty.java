
package pdcpart2.lifelines;

import pdcpart2.model.Question;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.Random;

/**
 * FiftyFifty lifeline removes two incorrect answers from the available options.
 * It ensures that the correct answer remains enabled.
 * 
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
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
            JButton selectedButton = optionButtons[randIndex];
            String selectedAnswer = selectedButton.getActionCommand(); // Use ActionCommand

            if (!selectedAnswer.equals(correctAnswer) && selectedButton.isEnabled()) {
                selectedButton.setEnabled(false); // Disable the incorrect option
                removed++;
            }
        }

        isUsed = true;  // Mark the lifeline as used
    }
}


