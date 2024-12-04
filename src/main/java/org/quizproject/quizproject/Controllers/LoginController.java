package org.quizproject.quizproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import org.quizproject.quizproject.Dao.DBconnection;
import org.quizproject.quizproject.Models.User;

import java.io.IOException;

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
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Login Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Welcome back! You have logged in successfully.");
                successAlert.showAndWait();
            } else {
                Alert failureAlert = new Alert(AlertType.ERROR);
                failureAlert.setTitle("Login Failed");
                failureAlert.setHeaderText(null);
                failureAlert.setContentText("Invalid email or password. Please try again.");
                failureAlert.showAndWait();
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
