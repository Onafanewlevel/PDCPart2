
package pdcpart2.dao;

import pdcpart2.model.GameResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import pdcpart2.util.DatabaseInitializer;

/**
 * Data Access Object for GameResult.
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class GameResultDAO {

    private Connection connection; // For Testing purposes
    
    /**
     * Default Constructor
     */
    public GameResultDAO(){
        connection = DatabaseInitializer.getInstance("QuestionDB").getConnection();   
    }

    /**
     * Constructor initializes the database connection for Testing purposes.
     *
     * @param connection The Connection object to the database.
     */
    public GameResultDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new game result into the database.
     *
     * @param gameResult The GameResult object to insert.
     * @throws SQLException If a database access error occurs.
     */
    public void insertGameResult(GameResult gameResult) throws SQLException {
        String insertSQL = "INSERT INTO Game_Results (player_name, score, last_question_index) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, gameResult.getPlayerName());
            pstmt.setInt(2, gameResult.getScore());
            pstmt.setInt(3, gameResult.getLastQuestionIndex());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all game results from the database.
     *
     * @return A list of GameResult objects.
     * @throws SQLException If a database access error occurs.
     */
    public List<GameResult> getAllGameResults() throws SQLException {
        List<GameResult> results = new ArrayList<>();
        String querySQL = "SELECT id, player_name, score, last_question_index, timestamp FROM Game_Results ORDER BY timestamp DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GameResult result = new GameResult(
                        rs.getInt("id"),
                        rs.getString("player_name"),
                        rs.getInt("score"),
                        rs.getInt("last_question_index"),
                        rs.getTimestamp("timestamp")
                );
                results.add(result);
            }
        }
        return results;
    }

    /**
     * Retrieves game results filtered by player name.
     *
     * @param playerName The name of the player.
     * @return A list of GameResult objects matching the player name.
     * @throws SQLException If a database access error occurs.
     */
    public List<GameResult> getGameResultsByPlayerName(String playerName) throws SQLException {
        List<GameResult> results = new ArrayList<>();
        String querySQL = "SELECT id, player_name, score, last_question_index, timestamp FROM Game_Results WHERE player_name = ? ORDER BY timestamp DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, playerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GameResult result = new GameResult(
                            rs.getInt("id"),
                            rs.getString("player_name"),
                            rs.getInt("score"),
                            rs.getInt("last_question_index"),
                            rs.getTimestamp("timestamp")
                    );
                    results.add(result);
                }
            }
        }
        return results;
    }
}



