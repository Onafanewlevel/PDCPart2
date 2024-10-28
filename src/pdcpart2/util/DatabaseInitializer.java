/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.util;

import pdcpart2.model.Question;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 * DatabaseInitializer is responsible for setting up the Apache Derby Embedded database.
 * It creates the Questions table if it doesn't exist and populates it with initial data.
 *
 * Usage:
 *     DatabaseInitializer initializer = new DatabaseInitializer("QuestionsDB");
 *     initializer.initializeDatabase();
 *     initializer.populateDatabase();
 */
public class DatabaseInitializer {

    private static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private String databasePath;
    private String jdbcURL;

    /**
     * Constructor to initialize the DatabaseInitializer with the specified database path.
     *
     * @param databasePath The path to the Embedded Derby database.
     */
    public DatabaseInitializer(String databasePath) {
        this.databasePath = databasePath;
        this.jdbcURL = "jdbc:derby:" + this.databasePath + ";create=true";
    }

    /**
     * Initializes the database by creating the Questions table if it doesn't exist.
     */
    public void initializeDatabase() {
        try {
            // Load the Derby Embedded Driver
            Class.forName(DERBY_EMBEDDED_DRIVER);
            System.out.println("Derby Embedded Driver loaded successfully.");

            try (Connection conn = DriverManager.getConnection(jdbcURL);
                 Statement stmt = conn.createStatement()) {

                // Check if the Questions table exists by attempting to query it
                if (!doesTableExist(conn, "QUESTIONS")) {
                    // Create the Questions table
                    String createTableSQL = "CREATE TABLE Questions ("
                            + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                            + "question VARCHAR(500) NOT NULL,"
                            + "option_a VARCHAR(255) NOT NULL,"
                            + "option_b VARCHAR(255) NOT NULL,"
                            + "option_c VARCHAR(255) NOT NULL,"
                            + "option_d VARCHAR(255) NOT NULL,"
                            + "correct_answer VARCHAR(255) NOT NULL,"
                            + "hint VARCHAR(500)"
                            + ")";
                    stmt.executeUpdate(createTableSQL);
                    System.out.println("Questions table created successfully.");
                } else {
                    System.out.println("Questions table already exists.");
                }

            } catch (SQLException e) {
                System.err.println("Error during database initialization.");
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Derby Embedded Driver not found.");
            e.printStackTrace();
        }
    }

    /**
     * Populates the Questions table with initial data if it's empty.
     */
    public void populateDatabase() {
        try (Connection conn = DriverManager.getConnection(jdbcURL)) {

            // Check if the Questions table is empty
            if (isTableEmpty(conn, "Questions")) {
                List<Question> initialQuestions = getInitialQuestions();
                String insertSQL = "INSERT INTO Questions (question, option_a, option_b, option_c, option_d, correct_answer, hint) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    for (Question q : initialQuestions) {
                        pstmt.setString(1, q.getQuestionText());
                        pstmt.setString(2, q.getOptionA());
                        pstmt.setString(3, q.getOptionB());
                        pstmt.setString(4, q.getOptionC());
                        pstmt.setString(5, q.getOptionD());
                        pstmt.setString(6, q.getCorrectAnswer());
                        pstmt.setString(7, q.getHint());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    System.out.println("Initial questions inserted successfully.");
                } catch (SQLException e) {
                    System.err.println("Error inserting initial questions.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Questions table is already populated.");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database during population.");
            e.printStackTrace();
        }
    }

    /**
     * Checks if a specific table exists in the database.
     *
     * @param conn      The database connection.
     * @param tableName The name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    private boolean doesTableExist(Connection conn, String tableName) {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking if table " + tableName + " exists.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a specific table is empty.
     *
     * @param conn      The database connection.
     * @param tableName The name of the table to check.
     * @return true if the table is empty, false otherwise.
     */
    private boolean isTableEmpty(Connection conn, String tableName) {
        String countSQL = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSQL)) {
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error counting records in table " + tableName + ".");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a list of initial questions to populate the database.
     *
     * @return List of Question objects.
     */
    private List<Question> getInitialQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What is the capital of France?",
                "Berlin",
                "London",
                "Paris",
                "Madrid",
                "Paris",
                "It's also known as the City of Light."
        ));

        questions.add(new Question(
                "Which planet is known as the Red Planet?",
                "Earth",
                "Mars",
                "Jupiter",
                "Venus",
                "Mars",
                "It's the fourth planet from the Sun."
        ));

        questions.add(new Question(
                "Who wrote the play 'Romeo and Juliet'?",
                "William Wordsworth",
                "William Shakespeare",
                "George Bernard Shaw",
                "Jane Austen",
                "William Shakespeare",
                "He is often called England's national poet."
        ));

        questions.add(new Question(
                "What is the largest ocean on Earth?",
                "Atlantic Ocean",
                "Indian Ocean",
                "Arctic Ocean",
                "Pacific Ocean",
                "Pacific Ocean",
                "It's larger than all the landmasses combined."
        ));

        questions.add(new Question(
                "Which element has the chemical symbol 'O'?",
                "Gold",
                "Oxygen",
                "Silver",
                "Iron",
                "Oxygen",
                "It's essential for human respiration."
        ));

        // Add more questions as needed

        return questions;
    }

    /**
     * Shuts down the Derby Embedded database explicitly.
     */
    public void shutdownDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby shut down normally.");
            } else {
                System.err.println("Derby did not shut down normally.");
                e.printStackTrace();
            }
        }
    }
}


