/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.model;

import java.sql.Timestamp;

/**
 * Represents a game result record.
 *
 * Author: Setefano Muller
 */
public class GameResult {
    private int id;
    private String playerName;
    private int score;
    private int lastQuestionIndex;
    private Timestamp timestamp;

    // Constructors
    public GameResult(String playerName, int score, int lastQuestionIndex) {
        this.playerName = playerName;
        this.score = score;
        this.lastQuestionIndex = lastQuestionIndex;
    }

    public GameResult(int id, String playerName, int score, int lastQuestionIndex, Timestamp timestamp) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
        this.lastQuestionIndex = lastQuestionIndex;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }

    public String getPlayerName() { return playerName; }

    public int getScore() { return score; }

    public int getLastQuestionIndex() { return lastQuestionIndex; }

    public Timestamp getTimestamp() { return timestamp; }

    public void setId(int id) { this.id = id; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public void setScore(int score) { this.score = score; }

    public void setLastQuestionIndex(int lastQuestionIndex) { this.lastQuestionIndex = lastQuestionIndex; }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
