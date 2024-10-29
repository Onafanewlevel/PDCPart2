package pdcpart2tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import pdcpart2.util.DatabaseInitializer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Enhanced Test suite for the DatabaseInitializer class.
 *
 * This class contains unit tests to verify the basic functionalities of the DatabaseInitializer,
 * including instantiation and establishing a database connection.
 * It also ensures proper resource management by initializing the database before tests
 * and cleaning up after tests by shutting down and deleting the test database.
 *
 * Author: [Your Name]
 */
public class DatabaseInitializerTest {

    private static final String TEST_DATABASE_PATH = "TestQuestionsDB";
    private static final String JDBC_URL = "jdbc:derby:" + TEST_DATABASE_PATH + ";create=true";

    private static DatabaseInitializer dbInitializer;

    /**
     * Setup method to initialize and populate the database once before all tests.
     * This method must be static as per JUnit 4 requirements.
     */
    @BeforeClass
    public static void setUpClass() {
        try {
            // Initialize the DatabaseInitializer
            dbInitializer = DatabaseInitializer.getInstance(TEST_DATABASE_PATH);
        } catch (Exception e) { // Replace with specific exceptions if DatabaseInitializer throws them
            fail("Setup failed: " + e.getMessage());
        }
    }

    /**
     * Teardown method to shut down the database and delete the test database directory after all tests.
     * This method must be static as per JUnit 4 requirements.
     */
    @AfterClass
    public static void tearDownClass() {
        if (dbInitializer != null) {
            dbInitializer.shutdownDatabase();
        }

        // Delete the test database directory to ensure a clean state
        deleteTestDatabase();
    }

    /**
     * Helper method to delete the test database directory recursively.
     */
    private static void deleteTestDatabase() {
        File dbDir = new File(TEST_DATABASE_PATH);
        if (dbDir.exists()) {
            deleteDirectoryRecursively(dbDir);
        }
    }

    /**
     * Recursively deletes a directory and its contents.
     *
     * @param file The directory or file to delete.
     */
    private static void deleteDirectoryRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) { // Prevent NullPointerException
                for (File child : children) {
                    deleteDirectoryRecursively(child);
                }
            }
        }
        if (!file.delete()) {
            System.err.println("Failed to delete: " + file.getAbsolutePath());
        }
    }

    /**
     * Test that an instance of DatabaseInitializer can be created successfully.
     */
    @Test
    public void testGetInstance() {
        assertNotNull("DatabaseInitializer instance should not be null.", dbInitializer);
    }

    /**
     * Test that a connection to the database can be established successfully.
     */
    @Test
    public void testConnection() {
        Connection conn = null;
        try {
            // Attempt to establish a connection
            conn = DriverManager.getConnection(JDBC_URL);
            assertNotNull("Connection should be established successfully.", conn);
            assertFalse("Connection should not be closed.", conn.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred while attempting to connect: " + e.getMessage());
        } finally {
            // Close the connection if it was established
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close the connection: " + e.getMessage());
                }
            }
        }
    }
}
