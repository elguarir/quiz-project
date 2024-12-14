package org.quizproject.quizproject.Tests;

import org.quizproject.quizproject.Dao.CategoryDao;
import org.quizproject.quizproject.Dao.QuestionDao;
import org.quizproject.quizproject.Dao.RoomQuestionDao;
import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.Option;

import java.util.List;

public class TestQueries {
    public static void main(String[] args) {
        testRoomQuestions(27); // Replace with your room ID
    }

    private static void testRoomQuestions(long roomId) {
        System.out.println("Testing room questions query for room " + roomId);
        RoomQuestionDao dao = new RoomQuestionDao();
        List<Question> questions = dao.getRoomQuestionsInOrder(roomId);
        
        if (questions == null || questions.isEmpty()) {
            System.out.println("No questions found for room " + roomId);
            return;
        }

        System.out.println("\nFound " + questions.size() + " questions:");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + q.getContent());
            System.out.println("ID: " + q.getId());
            System.out.println("Category: " + q.getCategoryId());
            System.out.println("Difficulty: " + q.getDifficulty());
            
            List<Option> options = q.getOptions();
            if (options != null && !options.isEmpty()) {
                System.out.println("Options:");
                for (Option o : options) {
                    System.out.println("- " + o.getContent() + 
                                     " (Correct: " + o.isCorrect() + ")");
                }
            } else {
                System.out.println("No options found for this question!");
            }
        }
    }

    // Keep the original test method
    private static void testCategoryQuestions() {
        var dao = new QuestionDao();
        var categories = dao.getQuestionsByCategory(1);
        categories.stream().forEach(System.out::println);
    }
}
