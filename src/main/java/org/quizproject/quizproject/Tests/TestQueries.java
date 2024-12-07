package org.quizproject.quizproject.Tests;

import org.quizproject.quizproject.Dao.QuestionDao;

public class TestQueries {

    public static void main(String[] args) {
        QuestionDao dao = new QuestionDao();
        var questions = dao.getOptionsByQuestionId(1);
        questions.stream().forEach(System.out::println);
    }
}
