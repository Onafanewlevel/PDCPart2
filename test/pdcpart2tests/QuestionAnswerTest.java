package pdcpart2tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import pdcpart2.util.DatabaseInitializer;
import pdcpart2.util.QuestionDatabaseLoader;
import pdcpart2.model.Question;

import java.io.File;
import java.util.List;
import java.sql.SQLException;

/**
 * Test suite for verifying the correctness of answers in loaded questions.
 *
 * This class contains unit tests to ensure that:
 * 1. The correct answers are recognized as correct.
 * 2. Incorrect answers are identified appropriately.
 *
 * Author: [Your Name]
 */
public class QuestionAnswerTest {

    private static final String TEST_DATABASE_PATH = "TestQuestionsDB";

    private DatabaseInitializer dbInitializer;
    private QuestionDatabaseLoader questionLoader;

    /**
     * Setup method to initialize and populate the database before each test.
     */
    @Before
    public void setUp() {
        // Initialize the DatabaseInitializer
        dbInitializer = new DatabaseInitializer(TEST_DATABASE_PATH);
        dbInitializer.initializeDatabase();
        dbInitializer.populateDatabase();

        // Load questions using QuestionDatabaseLoader
        questionLoader = new QuestionDatabaseLoader(TEST_DATABASE_PATH);
    }

    /**
     * Teardown method to shut down the database and delete the test database directory after all tests.
     */
    @After
    public void tearDown() {
        // Shutdown the database to release resources
        dbInitializer.shutdownDatabase();

        // Delete the test database directory to ensure a clean state
        deleteTestDatabase();
    }

    /**
     * Helper method to delete the test database directory recursively.
     */
    private void deleteTestDatabase() {
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
    private void deleteDirectoryRecursively(File file) {
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
     * Test to verify that the DatabaseInitializer instance is created successfully.
     */
    @Test
    public void testGetInstance() {
        assertNotNull("DatabaseInitializer instance should not be null.", dbInitializer);
    }

    /**
     * Test to verify that selecting the correct answer is recognized as correct.
     */
    @Test
    public void testCorrectAnswerEvaluation() {
        List<Question> questions = questionLoader.getQuestions();

        // Assert that questions are loaded
        assertNotNull("Questions list should not be null.", questions);
        assertFalse("Questions list should not be empty.", questions.isEmpty());

        // Select the first question for testing
        Question firstQuestion = questions.get(0);

        // Get the correct answer
        String correctAnswer = firstQuestion.getCorrectAnswer();

        // Evaluate the correct answer
        boolean isCorrect = evaluateAnswer(firstQuestion, correctAnswer);
        assertTrue("The selected answer should be correct.", isCorrect);
    }

    /**
     * Test to verify that selecting an incorrect answer is identified as wrong.
     */
    @Test
    public void testIncorrectAnswerEvaluation() {
        List<Question> questions = questionLoader.getQuestions();

        // Assert that questions are loaded
        assertNotNull("Questions list should not be null.", questions);
        assertFalse("Questions list should not be empty.", questions.isEmpty());

        // Select the first question for testing
        Question firstQuestion = questions.get(0);

        // Get an incorrect answer
        String incorrectAnswer = getIncorrectAnswer(firstQuestion);

        // Ensure that the incorrectAnswer is indeed not the correct one
        assertNotEquals("Incorrect answer should not match the correct answer.",
                incorrectAnswer, firstQuestion.getCorrectAnswer());

        // Evaluate the incorrect answer
        boolean isIncorrect = evaluateAnswer(firstQuestion, incorrectAnswer);
        assertFalse("The selected answer should be wrong.", isIncorrect);
    }

    /**
     * Helper method to evaluate an answer against the correct answer.
     *
     * @param question       The Question object.
     * @param selectedAnswer The answer selected by the user.
     * @return true if the selected answer is correct, false otherwise.
     */
    private boolean evaluateAnswer(Question question, String selectedAnswer) {
        return selectedAnswer.equals(question.getCorrectAnswer());
    }

    /**
     * Helper method to retrieve an incorrect answer from the question options.
     * Assumes that there is at least one incorrect option.
     *
     * @param question The Question object.
     * @return An incorrect answer option.
     */
    private String getIncorrectAnswer(Question question) {
        // Retrieve all options
        String optionA = question.getOptionA();
        String optionB = question.getOptionB();
        String optionC = question.getOptionC();
        String optionD = question.getOptionD();
        String correct = question.getCorrectAnswer();

        // Find the first option that is not the correct answer
        if (!optionA.equals(correct)) {
            return optionA;
        }
        if (!optionB.equals(correct)) {
            return optionB;
        }
        if (!optionC.equals(correct)) {
            return optionC;
        }
        if (!optionD.equals(correct)) {
            return optionD;
        }

        // In case all options are the same (which shouldn't happen), return the correct answer
        return correct;
    }
}
