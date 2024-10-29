
package pdcpart2.lifelines;

/**
 * Hint Lifeline provides a clue to Player to assist them in selecting the correct answer.
 * 
 * @author setefanomuller
 */
import javax.swing.*;
import pdcpart2.model.Question;

public class Hint extends Lifeline {

    /**
     * Implements the Hint lifeline logic by displaying a hint.
     *
     * @param question The current Question object to access the hint.
     * @param optionButtons Not used in this lifeline.
     * @param messageLabel The label to display the hint in the GUI.
     */
    @Override
    public void useLifeline(Question question, JButton[] optionButtons, JLabel messageLabel) {
        if (isUsed) {
            messageLabel.setText("You have already used the Hint lifeline.");
            return;
        }

        if (question.getHint() != null && !question.getHint().isEmpty()) {
            messageLabel.setText("Hint: " + question.getHint());
        } else {
            messageLabel.setText("No hint available for this question.");
        }
        isUsed = true;  // Mark the lifeline as used
    }

}
