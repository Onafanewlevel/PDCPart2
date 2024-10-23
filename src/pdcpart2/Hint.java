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

        messageLabel.setText("Hint: " + question.getHint());
        isUsed = true;  // Mark the lifeline as used
    }
}

