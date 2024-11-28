package org.quizproject.quizproject.Authentification;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.stage.Stage;
import org.quizproject.quizproject.Dao.AuthDao;
import org.quizproject.quizproject.Models.User;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField nameField;  // Changed from fullnameField

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    private AuthDao authDao = new AuthDao();

    @FXML
    public void handleSignUp(ActionEvent event) {
        // Retrieve form data
        String name = nameField.getText();  // Changed from fullname
        String email = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Passwords do not match!");
            return;
        }

        // Create a new User object
        User user = new User();
        user.setName(name);  // Changed from setFullname
        user.setEmail(email);
        user.setPassword(password);

        // Save the user in the database
        try {
            authDao.signUp(user);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not register the user.");
        }
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void redirectLoginButtonAction(ActionEvent event) {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
