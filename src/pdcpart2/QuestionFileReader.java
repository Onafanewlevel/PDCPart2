/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603projectpart2;

/**
 *
 * @author setefanomuller
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionFileReader {
    private List<Question> questions = new ArrayList<>();

    public QuestionFileReader(String fileName) {
        loadQuestions(fileName);
    }

    // Load questions from file
    private void loadQuestions(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String questionText = line;
                String optionA = br.readLine();
                String optionB = br.readLine();
                String optionC = br.readLine();
                String optionD = br.readLine();
                String correctAnswer = br.readLine(); // Correct answer
                String hint = br.readLine(); // Optional hint line
                questions.add(new Question(questionText, optionA, optionB, optionC, optionD, correctAnswer, hint));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

