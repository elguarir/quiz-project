package org.quizproject.quizproject.Tests;

import org.quizproject.quizproject.Dao.CategoryDao;
import org.quizproject.quizproject.Dao.QuestionDao;

public class TestQueries {

    public static void main(String[] args) {
        var dao = new QuestionDao();
        var categories = dao.getQuestionsByCategory(1);
        categories.stream().forEach(System.out::println);
        // questions.stream().limit(2).forEach(question -> {
        //     System.out.println("- Question: " + question.getContent());
        //     System.out.println("- OPTIONS");
        //     question.getOptions().stream()
        //             .forEach(op -> {
        //                 System.out.println("- " + op.getId() + ": " + op.getContent());
        //             });
        // });
    }
}
