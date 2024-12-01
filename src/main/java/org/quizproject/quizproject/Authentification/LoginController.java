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
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please fill in all fields");
            return;
        }

        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            Utilisateur user = new Utilisateur();
            user.setEmail(email);
            user.setPassword(password);

            if (authDao.Login(user)) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("quizPage-view.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root, 900, 700));
                    stage.setTitle("Registration Page");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Alert failureAlert = new Alert(AlertType.ERROR);
                failureAlert.setTitle("Login Failed");
                failureAlert.setHeaderText(null);
                failureAlert.setContentText("Invalid email or password. Please try again.");
                failureAlert.showAndWait();
            }
        } catch (RuntimeException e) {
            showAlert(AlertType.ERROR, "System Error", "An error occurred while processing your request: " + e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Handle redirect to SignUp page
    public void redirectSignUpButtonAction(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("Registration Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
