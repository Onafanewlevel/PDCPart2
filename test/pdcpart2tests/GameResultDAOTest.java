
package pdcpart2tests;

import static org.junit.Assert.*;

import pdcpart2.dao.GameResultDAO;
import pdcpart2.model.GameResult;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

/**
 * Test class for GameResultDAO using JUnit 4.
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class GameResultDAOTest {

    private static Connection connection;
    private static GameResultDAO gameResultDAO;

    /**
     * Sets up the in-memory database and initializes the Game_Results table before all tests.
     */
    @BeforeClass
    public static void setUpClass() throws SQLException {
        // Initialize in-memory Derby database
        String jdbcURL = "jdbc:derby:memory:TestDB;create=true";
        connection = DriverManager.getConnection(jdbcURL);
        gameResultDAO = new GameResultDAO(connection);

        // Create Game_Results table
        String createTableSQL = "CREATE TABLE Game_Results ("
                + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "player_name VARCHAR(255) NOT NULL,"
                + "score INT NOT NULL,"
                + "last_question_index INT NOT NULL,"
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    /**
     * Cleans up the Game_Results table before each test to ensure test isolation.
     */
    @Before
    public void cleanTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // DELETE is supported by Derby; TRUNCATE is not
            stmt.executeUpdate("DELETE FROM Game_Results");
        }
    }

    /**
     * Cleans up the database after all tests are done.
     */
    @AfterClass
    public static void tearDownClass() {
        // Drop the Game_Results table
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE Game_Results");
        } catch (SQLException e) {
            // Log the exception, but do not fail the tests
            System.err.println("Failed to drop table Game_Results: " + e.getMessage());
        }

        // Close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }

        // Shutdown Derby to clean up the in-memory database
        try {
            DriverManager.getConnection("jdbc:derby:memory:TestDB;shutdown=true");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                // Expected exception on successful shutdown
                System.out.println("Derby shut down successfully.");
            } else {
                // Log unexpected exceptions
                System.err.println("Error shutting down Derby: " + e.getMessage());
            }
        }
    }

    /**
     * Tests inserting a GameResult into the database.
     */
    @Test
    public void testInsertGameResult() throws SQLException {
        GameResult gameResult = new GameResult("Alice", 1000, 3);
        gameResultDAO.insertGameResult(gameResult);

        List<GameResult> results = gameResultDAO.getAllGameResults();
        assertEquals("There should be one game result.", 1, results.size());

        GameResult retrieved = results.get(0);
        assertEquals("Alice", retrieved.getPlayerName());
        assertEquals(1000, retrieved.getScore());
        assertEquals(3, retrieved.getLastQuestionIndex());
        assertNotNull("Timestamp should not be null.", retrieved.getTimestamp());
    }

    /**
     * Tests retrieving all GameResults from the database.
     */
    @Test
    public void testGetAllGameResults() throws SQLException {
        GameResult gr1 = new GameResult("Bob", 2000, 5);
        GameResult gr2 = new GameResult("Carol", 3000, 7);

        gameResultDAO.insertGameResult(gr1);
        gameResultDAO.insertGameResult(gr2);

        List<GameResult> results = gameResultDAO.getAllGameResults();
        assertEquals("There should be two game results.", 2, results.size());

        // Assuming ORDER BY timestamp DESC, the last inserted should be first
        GameResult first = results.get(0);
        GameResult second = results.get(1);

        assertEquals("Carol", first.getPlayerName());
        assertEquals("Bob", second.getPlayerName());
    }

    /**
     * Tests retrieving GameResults filtered by player name.
     */
    @Test
    public void testGetGameResultsByPlayerName() throws SQLException {
        GameResult gr1 = new GameResult("Dave", 1500, 4);
        GameResult gr2 = new GameResult("Eve", 2500, 6);
        GameResult gr3 = new GameResult("Dave", 3500, 8);

        gameResultDAO.insertGameResult(gr1);
        gameResultDAO.insertGameResult(gr2);
        gameResultDAO.insertGameResult(gr3);

        List<GameResult> daveResults = gameResultDAO.getGameResultsByPlayerName("Dave");
        assertEquals("Dave should have two game results.", 2, daveResults.size());

        for (GameResult gr : daveResults) {
            assertEquals("Dave", gr.getPlayerName());
        }

        List<GameResult> eveResults = gameResultDAO.getGameResultsByPlayerName("Eve");
        assertEquals("Eve should have one game result.", 1, eveResults.size());
        assertEquals("Eve", eveResults.get(0).getPlayerName());
    }

    /**
     * Tests retrieving GameResults for a player with no records.
     */
    @Test
    public void testGetGameResultsByPlayerName_NoResults() throws SQLException {
        GameResult gr1 = new GameResult("Frank", 4000, 9);
        gameResultDAO.insertGameResult(gr1);

        List<GameResult> results = gameResultDAO.getGameResultsByPlayerName("Grace");
        assertTrue("Grace should have no game results.", results.isEmpty());
    }

    /**
     * Tests inserting a GameResult with invalid data.
     */
    @Test
    public void testInsertGameResult_InvalidData() {
        // Attempt to insert a GameResult with null player name and negative score/index
        GameResult invalidGameResult = new GameResult(null, -100, -1);

        try {
            gameResultDAO.insertGameResult(invalidGameResult);
            fail("Inserting invalid game result should throw SQLException.");
        } catch (SQLException e) {
            // Expected exception due to NOT NULL constraint
            assertTrue("SQLException should be thrown for invalid data.", true);
        }
    }
}
