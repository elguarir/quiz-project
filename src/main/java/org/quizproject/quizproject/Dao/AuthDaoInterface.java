package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Model.Utilisateur;

public interface AuthDaoInterface {
    void signUp(Utilisateur user);
    boolean Login(Utilisateur user);
}
