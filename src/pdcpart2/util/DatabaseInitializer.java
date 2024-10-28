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
 * DatabaseInitializer is responsible for setting up the Apache Derby Embedded
 * database. It creates the Questions table if it doesn't exist and populates it
 * with initial data.
 *
 * Usage: DatabaseInitializer initializer = new
 * DatabaseInitializer("QuestionsDB"); initializer.initializeDatabase();
 * initializer.populateDatabase();
 */
public class DatabaseInitializer {

    private static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private String databasePath;
    private String jdbcURL;

    /**
     * Constructor to initialize the DatabaseInitializer with the specified
     * database path.
     *
     * @param databasePath The path to the Embedded Derby database.
     */
    public DatabaseInitializer(String databasePath) {
        this.databasePath = databasePath;
        this.jdbcURL = "jdbc:derby:" + this.databasePath + ";create=true";
    }

    /**
     * Initializes the database by creating the Questions table if it doesn't
     * exist.
     */
    public void initializeDatabase() {
        try {
            // Load the Derby Embedded Driver
            Class.forName(DERBY_EMBEDDED_DRIVER);
            System.out.println("Derby Embedded Driver loaded successfully.");

            try ( Connection conn = DriverManager.getConnection(jdbcURL);  Statement stmt = conn.createStatement()) {

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
        try ( Connection conn = DriverManager.getConnection(jdbcURL)) {

            // Check if the Questions table is empty
            if (isTableEmpty(conn, "Questions")) {
                List<Question> initialQuestions = getInitialQuestions();
                String insertSQL = "INSERT INTO Questions (question, option_a, option_b, option_c, option_d, correct_answer, hint) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try ( PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
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
     * @param conn The database connection.
     * @param tableName The name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    private boolean doesTableExist(Connection conn, String tableName) {
        try ( ResultSet rs = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null)) {
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
     * @param conn The database connection.
     * @param tableName The name of the table to check.
     * @return true if the table is empty, false otherwise.
     */
    private boolean isTableEmpty(Connection conn, String tableName) {
        String countSQL = "SELECT COUNT(*) FROM " + tableName;
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(countSQL)) {
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
                "A funtion",
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
                "What is the purpose of an 'if' statement??",
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
                "Which of these is used to style a webpage??",
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
                "What does 'SQL' stand for??",
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
