package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class RoomOptionsController {

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField codeTextField;

    @FXML
    private ComboBox<String> playersComboBox;

    @FXML
    private TextField quizTimeTextField;

    @FXML
    private HBox codeHBox;

    @FXML
    public void initialize() {
        // Initialize combo box values
        roomTypeComboBox.getItems().addAll("Public", "Private");
        roomTypeComboBox.setOnAction(event -> toggleCodeVisibility());
        playersComboBox.getItems().addAll("Duo", "Trio", "Squad");
        quizTimeTextField.setText("10"); // Default quiz time
    }

    private void toggleCodeVisibility() {
        String selectedType = roomTypeComboBox.getValue();
        codeHBox.setVisible("Private".equals(selectedType));
    }

    @FXML
    private void handlePlayAlone() {
        System.out.println("Play Alone button clicked");
    }

    @FXML
    private void handleCreate() {
        System.out.println("Create button clicked");
        System.out.println("Room Type: " + roomTypeComboBox.getValue());
        System.out.println("Code: " + codeTextField.getText());
        System.out.println("Number of Players: " + playersComboBox.getValue());
        System.out.println("Quiz Time: " + quizTimeTextField.getText());
    }
}
