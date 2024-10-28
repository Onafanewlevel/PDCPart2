package pdcpart2tests;

import org.junit.Test;
import static org.junit.Assert.*;

import pdcpart2.model.Player;

/**
 * Test suite for the Player class.
 *
 * This class contains unit tests to verify:
 * 1. Proper initialization of Player objects.
 * 2. Correct updating of player scores.
 *
 * Author: [Your Name]
 */
public class PlayerTest {

    /**
     * Test that a Player object is initialized correctly.
     */
    @Test
    public void testPlayerInitialization() {
        String playerName = "TestPlayer";
        Player player = new Player(playerName);

        assertNotNull("Player object should not be null.", player);
        assertEquals("Player name should match the provided name.", playerName, player.getName());
        assertEquals("Initial player score should be zero.", 0, player.getScore());
    }

    /**
     * Test that the player's score is updated correctly.
     */
    @Test
    public void testScoreUpdate() {
        Player player = new Player("TestPlayer");

        // Update the score
        int newScore = 1000;
        player.setScore(newScore);

        assertEquals("Player score should be updated to the new value.", newScore, player.getScore());
    }
}
