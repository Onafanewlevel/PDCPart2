/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.util;

import pdcpart2.model.Question;
import pdcpart2.model.GameResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 * DatabaseInitializer is responsible for setting up the Apache Derby Embedded
 * database. It creates the Questions and Game_Results tables if they don't
 * exist and populates them with initial data.
 *
 * @Author: Setefano Muller Tharuka Rodrigo
 */
public class DatabaseInitializer {

    private static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private String databasePath;
    private String jdbcURL;
    private static DatabaseInitializer instance;
    private Connection connection; // Add this field

    /**
     * Constructor to initialize the DatabaseInitializer with the specified
     * database path.
     *
     * @param databasePath The path to the Embedded Derby database.
     */
    private DatabaseInitializer(String databasePath) {
        this.databasePath = databasePath;
        this.jdbcURL = "jdbc:derby:" + this.databasePath + ";create=true";
    }

    /**
     * Singleton instance retrieval method.
     *
     * @param databasePath The path to the Embedded Derby database.
     * @return The singleton instance of DatabaseInitializer.
     */
    public static synchronized DatabaseInitializer getInstance(String databasePath) {
        if (instance == null) {
            instance = new DatabaseInitializer(databasePath);
            instance.initializeDatabase();
            instance.populateDatabase();
        }
        return instance;
    }

    /**
     * Initializes the database by creating the necessary tables if they don't
     * exist.
     */
    public void initializeDatabase() {
        try {
            // Load the Derby Embedded Driver
            Class.forName(DERBY_EMBEDDED_DRIVER);
            System.out.println("Derby Embedded Driver loaded successfully.");

            // Establish a connection and keep it open
            connection = DriverManager.getConnection(jdbcURL);
            System.out.println("Database connection established.");

            try (Statement stmt = connection.createStatement()) {

                // Create the Questions table if it doesn't exist
                if (!doesTableExist("QUESTIONS")) {
                    String createQuestionsTableSQL = "CREATE TABLE Questions ("
                            + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                            + "question VARCHAR(500) NOT NULL,"
                            + "option_a VARCHAR(255) NOT NULL,"
                            + "option_b VARCHAR(255) NOT NULL,"
                            + "option_c VARCHAR(255) NOT NULL,"
                            + "option_d VARCHAR(255) NOT NULL,"
                            + "correct_answer VARCHAR(255) NOT NULL,"
                            + "hint VARCHAR(500)"
                            + ")";
                    stmt.executeUpdate(createQuestionsTableSQL);
                    System.out.println("Questions table created successfully.");
                } else {
                    System.out.println("Questions table already exists.");
                }

                // Create the Game_Results table if it doesn't exist
                if (!doesTableExist("GAME_RESULTS")) {
                    String createGameResultsTableSQL = "CREATE TABLE Game_Results ("
                            + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                            + "player_name VARCHAR(255) NOT NULL,"
                            + "score INT NOT NULL,"
                            + "last_question_index INT NOT NULL,"
                            + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                            + ")";
                    stmt.executeUpdate(createGameResultsTableSQL);
                    System.out.println("Game_Results table created successfully.");
                } else {
                    System.out.println("Game_Results table already exists.");
                }

            } catch (SQLException e) {
                System.err.println("Error during table creation.");
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Derby Embedded Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error establishing database connection.");
            e.printStackTrace();
        }
    }

    /**
     * Populates the Questions table with initial data if it's empty.
     */
    public void populateDatabase() {
        // Populate Questions table if empty
        if (isTableEmpty("Questions")) {
            List<Question> initialQuestions = getInitialQuestions();
            String insertQuestionsSQL = "INSERT INTO Questions (question, option_a, option_b, option_c, option_d, correct_answer, hint) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = connection.prepareStatement(insertQuestionsSQL)) {
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

        // No initial data needed for Game_Results table
    }

    /**
     * Checks if a specific table exists in the database.
     *
     * @param tableName The name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    private boolean doesTableExist(String tableName) {
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName.toUpperCase(), null)) {
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
     * @param tableName The name of the table to check.
     * @return true if the table is empty, false otherwise.
     */
    private boolean isTableEmpty(String tableName) {
        String countSQL = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(countSQL)) {
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
                "What is a variable in programming?",
                "A fixed value",
                "A random value",
                "A changeable value",
                "A function",
                "A changeable value",
                "It stores information that can be changed."
        ));

        questions.add(new Question(
                "What does 'HTML' stand for?",
                "HyperText Markup Language",
                "HighText Machine Language",
                "HyperLoop Machine Language",
                "Home Tool Markup Language",
                "HyperText Markup Language",
                "It is used to create web pages."
        ));

        questions.add(new Question(
                "Which keyword is used to define a function in Python?",
                "func",
                "function",
                "def",
                "define",
                "def",
                "It tells Python to create a function."
        ));

        questions.add(new Question(
                "What does 'CSS' stand for?",
                "Cascading Style System",
                "Computer Style Sheets",
                "Cascading Style Sheets",
                "Colorful Style Sheets",
                "Cascading Style Sheets",
                "It is used for designing web pages."
        ));

        questions.add(new Question(
                "What is the use of a loop in programming?",
                "To perform a task only once",
                "To repeat a block of code",
                "To create an error",
                "To stop the code",
                "To repeat a block of code",
                "Useful for iterating over data."
        ));

        questions.add(new Question(
                "What does 'OOP' stand for?",
                "Object Over Process",
                "Object-Oriented Programming",
                "Ordered Object Programming",
                "Overridden Object Properties",
                "Object-Oriented Programming",
                "It is a paradigm based on classes and objects."
        ));

        questions.add(new Question(
                "Which of these is not a programming language?",
                "Python",
                "JavaScript",
                "HTML",
                "Java",
                "HTML",
                "One of these is a markup language only."
        ));

        questions.add(new Question(
                "What is the purpose of an 'if' statement?",
                "To loop code",
                "To declare variables",
                "To make decisions",
                "To terminate a program",
                "To make decisions",
                "Used for conditional execution."
        ));

        questions.add(new Question(
                "What is the extension of JavaScript files?",
                ".java",
                ".html",
                ".js",
                ".css",
                ".js",
                "JavaScript files use .js extension."
        ));

        questions.add(new Question(
                "Which of these is used to style a webpage?",
                "Python",
                "HTML",
                "JavaScript",
                "CSS",
                "CSS",
                "CSS helps in designing the visual part of the webpage."
        ));

        questions.add(new Question(
                "What does 'API' stand for?",
                "Application Performance Interface",
                "Application Programming Integration",
                "Application Programming Interface",
                "Automated Process Integration",
                "Application Programming Interface",
                "It allows different software applications to communicate with each other."
        ));

        questions.add(new Question(
                "Which operator is used for equality in JavaScript?",
                "==",
                "!=",
                "=",
                ":",
                "==",
                "Checks if two values are equal."
        ));

        questions.add(new Question(
                "What does 'SQL' stand for?",
                "Structured Quality Language",
                "Sequential Query Logic",
                "Structured Query Language",
                "Standard Query Layout",
                "Structured Query Language",
                "It is used for managing databases."
        ));

        questions.add(new Question(
                "What is an algorithm?",
                "A flowchart",
                "A diagram",
                "A step-by-step set of instructions to solve a problem",
                "A database",
                "A step-by-step set of instructions to solve a problem",
                "It is used to solve problems systematically."
        ));

        questions.add(new Question(
                "What is an IDE?",
                "Integrated Digital Environment",
                "Interactive Data Editors",
                "Integrated Development Environment",
                "Internal Debugging Editor",
                "Integrated Development Environment",
                "It's a software application for developers."
        ));

        return questions;
    }

    /**
     * Returns the current database connection.
     *
     * @return The Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Shuts down the Derby Embedded database explicitly.
     */
    public synchronized void shutdownDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
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
