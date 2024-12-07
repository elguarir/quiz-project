package org.quizproject.quizproject.Tests;

import org.quizproject.quizproject.Dao.QuestionDao;

public class TestQueries {

    public static void main(String[] args) {
        QuestionDao dao = new QuestionDao();
        var questions = dao.getQuestionsByCategory(1);
        System.out.println(questions.size());

    }
}
