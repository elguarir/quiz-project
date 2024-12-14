package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import org.quizproject.quizproject.Dao.CategoryDao;
import org.quizproject.quizproject.Dao.RoomDao;
import org.quizproject.quizproject.Dao.RoomParticipantDao;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.Models.Room;
import org.quizproject.quizproject.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.util.Timer;
import java.util.TimerTask;

import java.io.IOException;
import java.util.List;

public class HomeController {
    @FXML
    private Label userNameLabel;
    @FXML
    private GridPane categoryGrid;
    @FXML
    private ImageView avatarImage;
    @FXML
    private GridPane roomsGrid;
    @FXML
    private TextField roomCodeField;
    @FXML
    private Button joinByCodeButton;

    private final CategoryDao categoryDao = new CategoryDao();
    private final RoomDao roomDao = new RoomDao(); // Add this line
    private final RoomParticipantDao participantDao = new RoomParticipantDao();
    private Timer roomRefreshTimer;

    @FXML
    public void initialize() {
        // Set the logged-in user's name in the navbar
        User currentUser = MainApplication.getInstance().getCurrentUser();
        userNameLabel.setText(currentUser.getName());

        // Load avatar image
        String avatarUrl = currentUser.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            avatarImage.setImage(new Image(avatarUrl));
        }

        loadCategories();
        loadPublicRooms();
        startRoomRefreshTimer();

        // Add event handler for join by code button
        joinByCodeButton.setOnAction(event -> handleJoinByCode());
    }

    private void startRoomRefreshTimer() {
        roomRefreshTimer = new Timer();
        roomRefreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> loadPublicRooms());
            }
        }, 5000, 5000); // Refresh every 5 seconds
    }

    private void loadCategories() {
        List<Category> categories = categoryDao.getAllCategories();
        int column = 0;
        int row = 0;

        for (Category category : categories) {
            HBox categoryBox = createCategoryBox(category);

            // Set fixed size for the box
            categoryBox.setMinHeight(131.0);
            categoryBox.setMaxHeight(131.0);
            categoryBox.setMinWidth(376.0);
            categoryBox.setMaxWidth(376.0);

            categoryGrid.add(categoryBox, column, row);

            while (categoryGrid.getRowConstraints().size() <= row) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(131.0);
                rowConstraints.setPrefHeight(131.0);
                categoryGrid.getRowConstraints().add(rowConstraints);
            }

            column++;
            if (column > 1) { // Move to next row after 2 columns
                column = 0;
                row++;
            }
        }
    }

    private void loadPublicRooms() {
        roomsGrid.getChildren().clear();
        List<Room> publicRooms = roomDao.getPublicRooms();

        if (publicRooms.isEmpty()) {
            roomsGrid.add(createNoRoomsPlaceholder(), 0, 0, 2, 1); // Span across both columns
            return;
        }

        int column = 0;
        int row = 0;

        for (Room room : publicRooms) {
            HBox roomBox = createRoomBox(room);
            roomsGrid.add(roomBox, column, row);

            column++;
            if (column > 1) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createNoRoomsPlaceholder() {
        VBox placeholder = new VBox();
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setPrefHeight(100.0);
        placeholder.setPrefWidth(776.0);
        placeholder.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ddd;");
        placeholder.setPadding(new Insets(20));

        Label messageLabel = new Label("No rooms available at the moment");
        messageLabel.setFont(new Font("System", 14));
        messageLabel.setTextFill(javafx.scene.paint.Color.rgb(128, 128, 128));

        placeholder.getChildren().add(messageLabel);
        return placeholder;
    }

    private HBox createCategoryBox(Category category) {
        HBox box = new HBox();
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.setPrefHeight(131.0);
        box.setPrefWidth(376.0);
        box.setMinHeight(131.0);
        box.setMaxHeight(131.0);
        box.setStyle("-fx-background-color: #333; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label label = new Label(category.getName());
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label.setPrefHeight(116.0);
        label.setPrefWidth(373.0);
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new Font("System Bold", 29.0));

        box.getChildren().add(label);

        // Add click event handler
        box.setOnMouseClicked(event -> handleCategoryClick(category));

        return box;
    }

    private HBox createRoomBox(Room room) {
        HBox box = new HBox();
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.setPrefHeight(100.0);
        box.setPrefWidth(376.0);
        box.setStyle(
                "-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ddd; -fx-spacing: 10;");
        box.setPadding(new Insets(10));

        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(276);

        Label hostLabel = new Label("Host: " + room.getHostName());
        hostLabel.setFont(new Font("System Bold", 14));

        Label playersLabel = new Label("Max Players: " + room.getMaxPlayers());
        Label timeLabel = new Label("Quiz Time: " + room.getQuizTime() + " min");

        infoBox.getChildren().addAll(hostLabel, playersLabel, timeLabel);

        Button joinButton = new Button("Join Room");
        joinButton.setStyle("-fx-background-color: #97d6f7; -fx-text-fill: white; -fx-font-weight: bold;");
        joinButton.setOnAction(e -> handleJoinRoom(room));

        box.getChildren().addAll(infoBox, joinButton);

        return box;
    }

    private void handleCategoryClick(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/quizproject/quizproject/Main/create-quiz.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Quiz");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoryGrid.getScene().getWindow());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            CreateQuizController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategory(category);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleJoinRoom(Room room) {
        User currentUser = MainApplication.getInstance().getCurrentUser();

        // Check if room is full
        int currentParticipants = participantDao.getParticipantCount(room.getId());
        if (currentParticipants >= room.getMaxPlayers()) {
            showAlert("Room Full", "This room has reached its maximum capacity.");
            return;
        }

        // Check if user is already in the room
        if (participantDao.isUserInRoom(room.getId(), currentUser.getId())) {
            showAlert("Already Joined", "You are already in this room.");
            return;
        }

        // Add user to room
        if (participantDao.addParticipant(room.getId(), currentUser.getId(), currentUser.getUserIp())) {
            MainApplication.getInstance().showWaitingRoom(room);
        } else {
            showAlert("Error", "Failed to join the room. Please try again.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleJoinByCode() {
        String code = roomCodeField.getText().trim();
        System.out.println("Joining room with code: " + code);
        if (!code.isEmpty()) {
            Room room = roomDao.getRoomByCode(code);
            System.out.println("Room: " + room);
            if (room != null) {
                handleJoinRoom(room);
            } else {
                // Show error - room not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Room Not Found");
                alert.setContentText("No room exists with the provided code.");
                alert.showAndWait();
            }
        }
    }

    public void stop() {
        if (roomRefreshTimer != null) {
            roomRefreshTimer.cancel();
        }
    }
}