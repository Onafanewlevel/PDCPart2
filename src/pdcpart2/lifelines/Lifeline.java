
package pdcpart2.lifelines;

/**
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
import javax.swing.*;
import pdcpart2.model.Question;

public abstract class Lifeline {
    protected boolean isUsed; // Common attribute for lifelines (whether the lifeline is used)

    /**
     * Constructor to initialize lifeline.
     */
    public Lifeline() {
        this.isUsed = false;
    }

    /**
     * Abstract method to use the lifeline.
     * This will be implemented by subclasses.
     * 
     * @param question The current Question object.
     * @param optionButtons The array of option buttons in the GUI.
     * @param messageLabel The label to display messages in the GUI.
     */
    public abstract void useLifeline(Question question, JButton[] optionButtons, JLabel messageLabel);

    /**
     * Checks if the lifeline has been used.
     */
    public boolean isUsed() {
        return isUsed;
    }
}



