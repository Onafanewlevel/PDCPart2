
package pdcpart2tests;

import org.junit.Test;
import static org.junit.Assert.*;

import pdcpart2.model.PrizeLevel;

/**
 * Test suite for the PrizeLevel class.
 *
 * This class contains unit tests to verify:
 * 1. Correct prize amounts are returned for valid question levels.
 * 2. Proper handling of invalid question levels.
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class PrizeLevelTest {

    /**
     * Test that the correct prize amount is returned for each question level.
     */
    @Test
    public void testGetAmountByQuestionLevel() {
        // Assuming prize levels from 1 to 15 with increasing amounts
        int[] expectedAmounts = {
            100,    // Level 1
            200,    // Level 2
            300,    // Level 3
            500,    // Level 4
            1000,   // Level 5
            2000,   // Level 6
            4000,   // Level 7
            8000,   // Level 8
            16000,  // Level 9
            32000,  // Level 10
            64000,  // Level 11
            125000, // Level 12
            250000, // Level 13
            500000, // Level 14
            1000000 // Level 15
        };

        for (int level = 1; level <= expectedAmounts.length; level++) {
            int amount = PrizeLevel.getAmountByQuestionLevel(level);
            assertEquals("Prize amount for level " + level + " should be correct.",
                expectedAmounts[level - 1], amount);
        }
    }

    /**
     * Test that an invalid question level returns zero or handles appropriately.
     */
    @Test
    public void testInvalidQuestionLevel() {
        int invalidLevel = 0; // Level less than minimum
        int amount = PrizeLevel.getAmountByQuestionLevel(invalidLevel);
        assertEquals("Prize amount for invalid level should be zero.", 0, amount);
    }
}

