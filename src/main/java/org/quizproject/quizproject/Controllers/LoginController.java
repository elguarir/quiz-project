package org.quizproject.quizproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Dao.AuthDao;
import org.quizproject.quizproject.Dao.DBconnection;
import org.quizproject.quizproject.Models.User;


public class LoginController {

    public TextField emailField;
    public PasswordField passwordField;

    private final AuthDao authDao = new AuthDao();

    @FXML
    public void initialize() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // hnaya mli katsed l application cansdo lconnection l db tahiya
            DBconnection.getInstance().closeConnection();
        }));
    }

    // Handle login button action
    public void handleLogin(ActionEvent event) {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please fill in all fields");
            return;
        }

        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            if (authDao.Login(user)) {
                MainApplication.getInstance().setCurrentUser(user);
                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome back!");
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Invalid credentials");
            }
        } catch (RuntimeException e) {
            showAlert(AlertType.ERROR, "System Error",
                    "An error occurred while processing your request: " + e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void redirectSignUpButtonAction(ActionEvent event) {
        MainApplication.getInstance().showSignUpScreen();
    }
}
