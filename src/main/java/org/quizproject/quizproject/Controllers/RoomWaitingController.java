package org.quizproject.quizproject.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.Room;
import org.quizproject.quizproject.Models.User;
import org.quizproject.quizproject.Dao.RoomParticipantDao;
import org.quizproject.quizproject.Dao.UserDao;
import org.quizproject.quizproject.Dao.RoomDao;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RoomWaitingController {
    @FXML
    private Label quizCreatedBy;
    @FXML
    private Label quizTime;
    @FXML
    private GridPane participantsGrid;
    @FXML
    private Button startButton;
    @FXML
    private Button leaveButton;
    @FXML
    private Label roomIdLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Pane privateBadge;
    @FXML
    private Pane publicBadge;
    @FXML
    private Label currentUserLabel;
    @FXML
    private ImageView avatarImage;

    private Room currentRoom;
    private Timer refreshTimer;
    private RoomParticipantDao participantDao = new RoomParticipantDao();
    private UserDao userDao = new UserDao();
    private final RoomDao roomDao = new RoomDao();

    @FXML
    public void initialize() {
        User currentUser = MainApplication.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUserLabel != null) {
                currentUserLabel.setText(currentUser.getName());
            }
            
            // Load avatar image
            if (avatarImage != null) {
                String avatarUrl = currentUser.getAvatar();
                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                    avatarImage.setImage(new Image(avatarUrl));
                }
            }
        }

        if (leaveButton != null) {
            leaveButton.setOnAction(e -> handleLeave());
        }
        if (startButton != null) {
            startButton.setOnAction(e -> handleStart());
            startButton.setVisible(false);
        }
    }

    public void initData(Room room) {
        this.currentRoom = room;

        if (participantsGrid == null) {
            System.err.println("Warning: participantsGrid is null");
            return;
        }

        // Setup basic room info
        if (roomIdLabel != null) {
            roomIdLabel.setText("Room ID #" + room.getId());
        }
        if (quizTime != null) {
            quizTime.setText("Time: " + room.getQuizTime() + " minutes");
        }
        
        // Handle visibility of privacy badges
        if (privateBadge != null && publicBadge != null) {
            boolean isPrivate = room.isPrivate();
            privateBadge.setVisible(isPrivate);
            publicBadge.setVisible(!isPrivate);
        }

        User host = userDao.getUserById(room.getHostId());
        if (quizCreatedBy != null && host != null) {
            quizCreatedBy.setText("Created by: " + host.getName());
        }

        // Show start button only to host
        User currentUser = MainApplication.getInstance().getCurrentUser();
        if (startButton != null && currentUser != null) {
            startButton.setVisible(currentUser.getId() == room.getHostId());
        }

        // Start periodic refresh
        startRefreshTimer();

        // Initial refresh
        refreshParticipants();
    }

    private void startRefreshTimer() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refreshParticipants());
            }
        }, 0, 2000); // Refresh every 2 seconds
    }

    private void refreshParticipants() {
        if (participantsGrid == null) {
            return;
        }

        List<User> participants = participantDao.getRoomParticipants(currentRoom.getId());

        if (participantsLabel != null) {
            participantsLabel.setText(participants.size() + "/" + currentRoom.getMaxPlayers() + " joined (Waiting)");
        }

        participantsGrid.getChildren().clear();

        // Add participants to grid
        for (int i = 0; i < participants.size(); i++) {
            User participant = participants.get(i);
            AnchorPane participantPane = createParticipantPane(participant);
            participantsGrid.add(participantPane, i % 2, i / 2);
        }
    }

    private AnchorPane createParticipantPane(User participant) {
        AnchorPane pane = new AnchorPane();
        pane.setPrefHeight(200.0);
        pane.setPrefWidth(200.0);
        pane.setStyle("-fx-border-color: #d9d9d9; -fx-background-radius: 10px; -fx-border-radius: 10px; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0); "
                + "-fx-background-color: white;");

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setLayoutX(15.0);
        hbox.setLayoutY(24.0);
        hbox.setPrefHeight(84.0);
        hbox.setPrefWidth(390.0);
        hbox.setSpacing(15.0);
        hbox.setPadding(new Insets(15.0));

        ImageView avatarView = new ImageView();
        avatarView.setFitHeight(48.0);
        avatarView.setFitWidth(48.0);
        avatarView.setPickOnBounds(true);
        avatarView.setPreserveRatio(true);
        avatarView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 0);");

        // Load avatar image from URL
        String avatarUrl = participant.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            try {
                Image avatarImage = new Image(avatarUrl, true);
                avatarView.setImage(avatarImage);
            } catch (Exception e) {
                Image defaultAvatar = new Image(
                        getClass().getResource("/org/quizproject/quizproject/Assets/avatar.png").toExternalForm());
                avatarView.setImage(defaultAvatar);
            }
        } else {
            Image defaultAvatar = new Image(
                    getClass().getResource("/org/quizproject/quizproject/Assets/avatar.png").toExternalForm());
            avatarView.setImage(defaultAvatar);
        }

        Label nameLabel = new Label(participant.getName() + " #" + participant.getId());
        nameLabel.setPrefHeight(48.0);
        nameLabel.setPrefWidth(250.0);
        nameLabel.setFont(new Font(24.0));
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        hbox.getChildren().addAll(avatarView, nameLabel);
        pane.getChildren().add(hbox);
        pane.setStyle("-fx-border-color: #d9d9d9; -fx-background-radius: 10px; "
                + "-fx-border-radius: 10px;"
                + "-fx-background-color: #f8f9fa;");

        return pane;
    }

    @FXML
    private void handleLeave() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }

        User currentUser = MainApplication.getInstance().getCurrentUser();
        
        if (currentUser.getId() == currentRoom.getHostId()) {
            // Host is leaving, delete the room
            roomDao.deleteRoom(currentRoom.getId());
        } else {
            // Regular participant leaving
            participantDao.removeParticipant(currentRoom.getId(), currentUser.getId());
        }
        
        MainApplication.getInstance().showHomeScreen();
    }

    @FXML
    private void handleStart() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
        MainApplication.getInstance().showPlayMulti();
    }

    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
    }
}
