package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.User;

public class CreateQuizController {
    @FXML
    private BorderPane buttonsWrapper;
    @FXML
    private BorderPane optionsWrapper;
    @FXML
    private HBox roomCodeWrapper;
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private ComboBox<String> roomType;
    @FXML
    private PasswordField roomCode;
    @FXML
    private Slider quizTimeSlider;
    @FXML
    private Label timeLabel;
    @FXML
    private Button createButton;
    @FXML
    private ToggleGroup playerCount;
    @FXML
    private TextField quizTitleField;
    @FXML
    private TextArea quizDescriptionField;
    private static Category selectedCategory;

    private Stage dialogStage;

    @FXML
    public void initialize() {
        if (buttonsWrapper != null) {
            buttonsWrapper.setVisible(true);
        }
        if (optionsWrapper != null) {
            optionsWrapper.setVisible(false);
        }

        if (roomType != null) {
            roomType.getItems().addAll("Public", "Private");
            roomType.setValue("Public");
            roomType.setOnAction(e -> handleRoomTypeChange());
        }

        if (singlePlayerButton != null) {
            singlePlayerButton.setOnAction(e -> handleSinglePlayer());
        }
        if (multiPlayerButton != null) {
            multiPlayerButton.setOnAction(e -> handleMultiPlayer());
        }
        if (quizTimeSlider != null) {
            quizTimeSlider.valueProperty().addListener(
                    (obs, old, newVal) -> timeLabel.setText(String.format("%.0f minutes", newVal.doubleValue())));
        }
        if (createButton != null) {
            createButton.setOnAction(e -> handleCreateRoom());
        }
    }

    public static void setCategory(Category category) {
        selectedCategory = category;
    }

    public static Category getCategory() {
        return selectedCategory;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void handleSinglePlayer() {

        MainApplication.getInstance().showPlayAlone();
        dialogStage.close();
    }

    private void handleMultiPlayer() {
        buttonsWrapper.setVisible(false);
        optionsWrapper.setVisible(true);
    }

    private void handleRoomTypeChange() {
        boolean isPrivate = "Private".equals(roomType.getValue());
        roomCodeWrapper.setVisible(isPrivate);
        roomCode.setDisable(!isPrivate);
    }

    private void handleCreateRoom() {
        // Validate inputs
        if ("Private".equals(roomType.getValue()) &&
                (roomCode.getText() == null || roomCode.getText().trim().isEmpty())) {
            showError("Room code is required for private rooms");
            return;
        }

        // TODO: Create room in database and start game session
        dialogStage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}