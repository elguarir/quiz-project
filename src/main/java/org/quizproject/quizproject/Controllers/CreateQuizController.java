package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.Room;

import java.util.List;
import java.util.stream.Collectors;

import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Dao.QuestionDao;
import org.quizproject.quizproject.Dao.RoomDao;
import org.quizproject.quizproject.Dao.RoomParticipantDao;
import org.quizproject.quizproject.Dao.RoomQuestionDao;
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
    private Label roomCodeLabel;
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
        // Validate without loading all questions
        QuestionDao questionDao = new QuestionDao();
        int questionCount = questionDao.getQuestionCount(selectedCategory.getId());

        if (questionCount == 0) {
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
        if (isPrivate) {
            roomCodeLabel.setText("Room code will be generated automatically");
            roomCodeLabel.setVisible(true);
        } else {
            roomCodeLabel.setVisible(false);
        }
    }

    private void validateRoomCreation() throws IllegalArgumentException {
        if (playerCount.getSelectedToggle() == null) {
            throw new IllegalArgumentException("Please select number of players");
        }
    }

    private void handleCreateRoom() {
        try {
            validateRoomCreation();
            User currentUser = MainApplication.getInstance().getCurrentUser();

            // Create room with all necessary data at once
            Room room = createRoomObject(currentUser);
            RoomDao roomDao = new RoomDao();
            Room createdRoom = roomDao.createRoom(room);

            if (createdRoom == null) {
                throw new IllegalArgumentException("Failed to create room");
            }

            // Get questions with options in a single query
            QuestionDao questionDao = new QuestionDao();
            List<Question> randomQuestions = questionDao.getRandomQuestionsWithOptions(selectedCategory.getId(), 10);

            if (randomQuestions.isEmpty()) {
                throw new IllegalArgumentException("No questions available for this category");
            }

            // Batch insert room questions
            RoomQuestionDao roomQuestionDao = new RoomQuestionDao();
            roomQuestionDao.saveRoomQuestions(createdRoom.getId(),
                    randomQuestions.stream().map(Question::getId).collect(Collectors.toList()));

            // Add host as participant and show waiting room
            RoomParticipantDao participantDao = new RoomParticipantDao();
            participantDao.addParticipant(createdRoom.getId(), currentUser.getId(), User.getUserIp());

            MainApplication.getInstance().showWaitingRoom(createdRoom);
            dialogStage.close();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private Room createRoomObject(User currentUser) {
        Room room = new Room();
        room.setHostId(currentUser.getId());
        room.setHostIp(User.getUserIp());
        room.setPrivate("Private".equals(roomType.getValue()));

        RadioButton selectedPlayerCount = (RadioButton) playerCount.getSelectedToggle();
        String maxPlayers = selectedPlayerCount != null ? selectedPlayerCount.getText() : "Dual";
        room.setMaxPlayers(maxPlayers.equals("Dual") ? 2 : maxPlayers.equals("Tri-Battle") ? 3 : 4);

        room.setQuizTime((int) quizTimeSlider.getValue());
        if (room.isPrivate()) {
            room.setCode(room.generateCode());
        }
        return room;
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