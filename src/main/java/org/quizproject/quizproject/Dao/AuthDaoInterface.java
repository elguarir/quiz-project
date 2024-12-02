package org.quizproject.quizproject.Dao;
import org.quizproject.quizproject.Models.User;

public interface AuthDaoInterface {
    void signUp(User user);
    boolean Login(User user);
}
