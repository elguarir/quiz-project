package org.quizproject.quizproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.quizproject.quizproject.Controllers.QuizResultsController;
import org.quizproject.quizproject.Controllers.RoomWaitingController;
import org.quizproject.quizproject.Models.Room;
import org.quizproject.quizproject.Models.User;

public class MainApplication extends Application {
    private static MainApplication instance;
    private Stage primaryStage;
    private User currentUser;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage stage) {
        instance = this;
        this.primaryStage = stage;
        this.primaryStage.setResizable(false);
        showLoginScreen();
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public void showLoginScreen() {
        loadScreen("Authentification/login-view.fxml", "Quizzy - Login");
    }

    public void showPlayAlone() {
        loadScreen("Main/room-questions-alone.fxml", "Quizzy - Quiz Alone");
    }

    public void showPlayMulti() {
        loadScreen("Main/room-questions.fxml", "Quizzy - Quiz Multiplayer");
    }

    public void showSignUpScreen() {
        loadScreen("Authentification/signUp-view.fxml", "Quizzy - Sign Up");
    }

    public void showHomeScreen() {
        loadScreen("Main/home-view.fxml", "Quizzy - Home");
    }

    public void showQuizResults(int correctAnswers, int totalQuestions, int timeTaken) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/quizproject/quizproject/Main/quiz-results.fxml"));
            Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
            QuizResultsController controller = loader.getController();
            controller.setResults(correctAnswers, totalQuestions, timeTaken);
            primaryStage.setTitle("Quizzy - Results");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWaitingRoom(Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/quizproject/quizproject/Main/room-waiting.fxml"));
            Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
            RoomWaitingController controller = loader.getController();
            controller.initData(room);
            primaryStage.setTitle("Quizzy - Waiting Room");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/quizproject/quizproject/" + fxmlPath));
            Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}