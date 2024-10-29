
package pdcpart2tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import pdcpart2.lifelines.FiftyFifty;
import pdcpart2.lifelines.Hint;
import pdcpart2.model.Question;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Test suite for the lifeline classes: FiftyFifty and Hint.
 *
 * This class contains unit tests to verify:
 * 1. Correct functionality of each lifeline.
 * 2. Lifelines cannot be used more than once.
 * 3. Proper handling when no hint is available.
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class LifelineTest {

    private Question questionWithHint;
    private Question questionWithoutHint;
    private JButton[] optionButtons;
    private JLabel messageLabel;

    @Before
    public void setUp() {
        // Create a sample question with a hint
        questionWithHint = new Question(
            "What is the capital of France?",
            "Paris",
            "Berlin",
            "Madrid",
            "Rome",
            "Paris", // Correct answer
            "It's known as the city of love." // Hint
        );

        // Create a sample question without a hint
        questionWithoutHint = new Question(
            "What is the capital of Germany?",
            "Paris",
            "Berlin",
            "Madrid",
            "Rome",
            "Berlin", // Correct answer
            "" // No hint
        );

        // Create option buttons and set their action commands
        optionButtons = new JButton[4];
        optionButtons[0] = new JButton(questionWithHint.getOptionA());
        optionButtons[0].setActionCommand(questionWithHint.getOptionA());
        optionButtons[1] = new JButton(questionWithHint.getOptionB());
        optionButtons[1].setActionCommand(questionWithHint.getOptionB());
        optionButtons[2] = new JButton(questionWithHint.getOptionC());
        optionButtons[2].setActionCommand(questionWithHint.getOptionC());
        optionButtons[3] = new JButton(questionWithHint.getOptionD());
        optionButtons[3].setActionCommand(questionWithHint.getOptionD());

        // Initially, all buttons are enabled
        for (JButton button : optionButtons) {
            button.setEnabled(true);
        }

        // Initialize the message label
        messageLabel = new JLabel();
    }

    /**
     * Test that the FiftyFifty lifeline removes two incorrect options,
     * leaving the correct answer enabled.
     */
    @Test
    public void testFiftyFiftyRemovesTwoIncorrectOptions() {
        FiftyFifty fiftyFifty = new FiftyFifty();

        fiftyFifty.useLifeline(questionWithHint, optionButtons, messageLabel);

        // Count the number of disabled buttons
        int disabledButtons = 0;
        int enabledButtons = 0;
        for (JButton button : optionButtons) {
            if (!button.isEnabled()) {
                disabledButtons++;
                // Ensure that disabled buttons are incorrect options
                assertNotEquals("Disabled button should not be the correct answer",
                                questionWithHint.getCorrectAnswer(), button.getActionCommand());
            } else {
                enabledButtons++;
            }
        }

        // There should be exactly two disabled buttons
        assertEquals("Two options should be disabled", 2, disabledButtons);
        // There should be exactly two enabled buttons
        assertEquals("Two options should remain enabled", 2, enabledButtons);

        // The correct answer should remain enabled
        boolean correctAnswerEnabled = false;
        for (JButton button : optionButtons) {
            if (button.isEnabled() && button.getActionCommand().equals(questionWithHint.getCorrectAnswer())) {
                correctAnswerEnabled = true;
                break;
            }
        }
        assertTrue("Correct answer should remain enabled", correctAnswerEnabled);

        // Check that the lifeline is marked as used
        assertTrue("FiftyFifty lifeline should be marked as used", fiftyFifty.isUsed());
    }

    /**
     * Test that the FiftyFifty lifeline cannot be used more than once.
     */
    @Test
    public void testFiftyFiftyCannotBeUsedTwice() {
        FiftyFifty fiftyFifty = new FiftyFifty();

        // First use
        fiftyFifty.useLifeline(questionWithHint, optionButtons, messageLabel);

        // Reset the message label
        messageLabel.setText("");

        // Try to use it again
        fiftyFifty.useLifeline(questionWithHint, optionButtons, messageLabel);

        // The message should indicate that the lifeline has already been used
        assertEquals("You have already used the 50:50 lifeline.", messageLabel.getText());
    }

    /**
     * Test that the Hint lifeline displays the hint message.
     */
    @Test
    public void testHintDisplaysHintMessage() {
        Hint hint = new Hint();

        hint.useLifeline(questionWithHint, optionButtons, messageLabel);

        // Check that the message label displays the hint
        assertEquals("Hint: " + questionWithHint.getHint(), messageLabel.getText());

        // Check that the lifeline is marked as used
        assertTrue("Hint lifeline should be marked as used", hint.isUsed());
    }

    /**
     * Test that the Hint lifeline cannot be used more than once.
     */
    @Test
    public void testHintCannotBeUsedTwice() {
        Hint hint = new Hint();

        // First use
        hint.useLifeline(questionWithHint, optionButtons, messageLabel);

        // Reset the message label
        messageLabel.setText("");

        // Try to use it again
        hint.useLifeline(questionWithHint, optionButtons, messageLabel);

        // The message should indicate that the lifeline has already been used
        assertEquals("You have already used the Hint lifeline.", messageLabel.getText());
    }

    /**
     * Test that the Hint lifeline displays a message when no hint is available.
     */
    @Test
    public void testHintDisplaysNoHintAvailableMessageIfNoHint() {
        Hint hint = new Hint();

        hint.useLifeline(questionWithoutHint, optionButtons, messageLabel);

        // Check that the message label displays the appropriate message
        assertEquals("No hint available for this question.", messageLabel.getText());

        // Check that the lifeline is marked as used
        assertTrue("Hint lifeline should be marked as used", hint.isUsed());
    }
}

