package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.Models.Room;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Dao.RoomDao;
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
        
        if (selectedCategory.getQuestions().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("No questions available for this category");
            alert.showAndWait();
        } else {
            MainApplication.getInstance().showPlayAlone();
            dialogStage.close();
        }

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

    private void validateRoomCreation() throws IllegalArgumentException {
        if ("Private".equals(roomType.getValue()) &&
                (roomCode.getText() == null || roomCode.getText().trim().isEmpty())) {
            throw new IllegalArgumentException("Room code is required for private rooms");
        }
        if (!roomCode.getText().matches("[0-9]+") && "Private".equals(roomType.getValue())) {
            throw new IllegalArgumentException("Room code must be a number");
        }
        if (playerCount.getSelectedToggle() == null) {
            throw new IllegalArgumentException("Please select number of players");
        }
    }

    private void handleCreateRoom() {
        try {
            validateRoomCreation();
            User currentUser = MainApplication.getInstance().getCurrentUser();
            String roomCodeValue = roomCode.getText();
            boolean isPrivate = "Private".equals(roomType.getValue());
            RadioButton selectedPlayerCount = (RadioButton) playerCount.getSelectedToggle();
            String maxPlayers = selectedPlayerCount != null ? selectedPlayerCount.getText() : "Dual";
            int maxPlayerInt = maxPlayers.equals("Dual") ? 2 : maxPlayers.equals("Tri-Battle") ? 3 : 4;
            int quizTime = (int) quizTimeSlider.getValue();

            System.out.println("Room Details:");
            System.out.println("Host: " + currentUser.getName());
            System.out.println("Room Type: " + (isPrivate ? "Private" : "Public"));
            System.out.println("Room Code: " + (isPrivate ? roomCodeValue : "N/A"));
            System.out.println("Max Players: " + maxPlayers);
            System.out.println("Quiz Time: " + quizTime + " minutes");
            System.out.println("Category: " + selectedCategory.getName());

            Room room = new Room();
            room.setHostId(currentUser.getId());
            room.setHostIp(currentUser.getUserIp()); // Local host for testing
            room.setCode(isPrivate ? roomCodeValue : null);
            room.setPrivate(isPrivate);
            room.setMaxPlayers(maxPlayerInt);
            room.setQuizTime(quizTime);

            RoomDao roomDao = new RoomDao();
            Room createdRoom = roomDao.createRoom(room);

            if (createdRoom == null) {
                throw new IllegalArgumentException("Failed to create room");
            }

            MainApplication.getInstance().showPlayMulti();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }

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