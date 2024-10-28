/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.util;

import pdcpart2.model.Question;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * QuestionLoader loads quiz questions from the Derby embedded database.
 */
public class QuestionLoader {
    private List<Question> questions = new ArrayList<>();

    // Constructor to initialize and load questions from the database
    public QuestionLoader(String databasePath) {
        loadQuestions(databasePath);
    }

    // Load questions from the database
    private void loadQuestions(String databasePath) {
        String jdbcURL = "jdbc:derby:" + databasePath + ";create=true";
        String query = "SELECT question, option_a, option_b, option_c, option_d, correct_answer, hint FROM Questions";

        // Load the Derby Embedded Driver
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Derby Embedded Driver not found.");
            e.printStackTrace();
            return;
        }

        try (Connection connection = DriverManager.getConnection(jdbcURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String question = resultSet.getString("question");
                String optionA = resultSet.getString("option_a");
                String optionB = resultSet.getString("option_b");
                String optionC = resultSet.getString("option_c");
                String optionD = resultSet.getString("option_d");
                String correctAnswer = resultSet.getString("correct_answer");
                String hint = resultSet.getString("hint");

                questions.add(new Question(question, optionA, optionB, optionC, optionD, correctAnswer, hint));
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to or querying the database.");
            e.printStackTrace();
        }
    }

    // Return the list of questions
    public List<Question> getQuestions() {
        return questions;
    }
}



