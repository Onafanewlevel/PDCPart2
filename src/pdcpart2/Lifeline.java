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



