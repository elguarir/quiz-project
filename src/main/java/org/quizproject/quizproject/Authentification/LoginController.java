package org.quizproject.quizproject.Authentification;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.quizproject.quizproject.Dao.AuthDao;
import org.quizproject.quizproject.Model.Utilisateur;

import java.io.IOException;

public class LoginController {


    public TextField emailField; // Linked to the email input field in the login form
    public PasswordField passwordField; // Linked to the password input field in the login form

    private final AuthDao authDao = new AuthDao();

    // Handle login button action
    public void handleLogin(ActionEvent event) {
        // Fetch input from the fields
        String email = emailField.getText();
        String password = passwordField.getText();

        // Create a user object
        Utilisateur user = new Utilisateur();
        user.setEmail(email);
        user.setPassword(password);

        // Authenticate user
        if (authDao.Login(user)) {
            // Display success alert
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Login Successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Welcome, " + user.getEmail() + "! You have logged in successfully.");
            successAlert.showAndWait();
        } else {
            // Display failure alert
            Alert failureAlert = new Alert(AlertType.ERROR);
            failureAlert.setTitle("Login Failed");
            failureAlert.setHeaderText(null);
            failureAlert.setContentText("Invalid email or password. Please try again.");
            failureAlert.showAndWait();
        }
    }

    // Handle redirect to SignUp page
    public void redirectSignUpButtonAction(ActionEvent event) {
        try {
            // Load the SignUp view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("Registration Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
