/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

/**
 *
 * @author setefanomuller
 */
public enum PrizeLevel {
    LEVEL_0(0), // No prize
    LEVEL_1(100), // Prize for the first question
    LEVEL_2(200), // Prize for the second question
    LEVEL_3(300), // Prize for the third question
    LEVEL_4(500), // Prize for the fourth question
    LEVEL_5(1000), // Prize for the fifth question
    LEVEL_6(2000), // Prize for the sixth question
    LEVEL_7(4000), // Prize for the seventh question
    LEVEL_8(8000), // Prize for the eighth question
    LEVEL_9(16000), // Prize for the ninth question
    LEVEL_10(32000), // Prize for the tenth question
    LEVEL_11(64000), // Prize for the eleventh question
    LEVEL_12(125000),// Prize for the twelfth question
    LEVEL_13(250000),// Prize for the thirteenth question
    LEVEL_14(500000),// Prize for the fourteenth question
    LEVEL_15(1000000); // Top prize for the fifteenth question

    private final int amount;

    /**
     * Constructor for each enum constant to set the prize amount.
     *
     * @param amount The monetary value associated with this prize level.
     */
    PrizeLevel(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the prize amount for this level.
     *
     * @return The monetary amount of the prize.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Static method to get the prize amount by question number. Ensures that
     * the question number is within valid range.
     *
     * @param questionNumber The question number (0-based index).
     * @return The prize amount corresponding to the question number.
     * @throws IllegalArgumentException if the question number is invalid.
     */
    public static int getAmountByQuestionLevel(int questionNumber) {
        // Ensure the question number is within the range of levels
        if (questionNumber >= 0 && questionNumber < PrizeLevel.values().length) {
            return PrizeLevel.values()[questionNumber].getAmount();
        } else {
            throw new IllegalArgumentException("Invalid question number: " + questionNumber);
        }
    }

}