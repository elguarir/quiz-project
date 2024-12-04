package org.quizproject.quizproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Dao.AuthDao;
import org.quizproject.quizproject.Models.User;

public class SignUpController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private final AuthDao authDao = new AuthDao();

    @FXML
    public void handleSignUp(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Passwords do not match!");
            return;
        }

        // Email validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Invalid email format!");
            return;
        }

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            authDao.signUp(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
            MainApplication.getInstance().showLoginScreen();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", 
                e.getMessage().contains("Duplicate entry") ? 
                "Email already exists!" : "Registration failed. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    public void redirectLoginButtonAction(ActionEvent event) {
        MainApplication.getInstance().showLoginScreen();
    }
    
}