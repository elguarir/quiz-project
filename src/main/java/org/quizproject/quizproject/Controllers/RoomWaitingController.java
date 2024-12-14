package org.quizproject.quizproject.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.QuestionDTO;
import org.quizproject.quizproject.Models.Room;
import org.quizproject.quizproject.Models.User;
import org.quizproject.quizproject.Dao.RoomParticipantDao;
import org.quizproject.quizproject.Dao.RoomQuestionDao;
import org.quizproject.quizproject.Dao.UserDao;
import org.quizproject.quizproject.Dao.RoomDao;
import org.quizproject.quizproject.Sockets.QuizClient;
import org.quizproject.quizproject.Sockets.QuizServer;
import org.quizproject.quizproject.Sockets.SocketMessage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

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
    @FXML
    private Button copyCodeButton;

    private Room currentRoom;
    private Timer refreshTimer;
    private RoomParticipantDao participantDao = new RoomParticipantDao();
    private UserDao userDao = new UserDao();
    private final RoomDao roomDao = new RoomDao();
    private QuizServer quizServer;
    private QuizClient quizClient;
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = MainApplication.getInstance().getCurrentUser();
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

        // Handle visibility of privacy badges and code button
        if (privateBadge != null && publicBadge != null && copyCodeButton != null) {
            boolean isPrivate = room.isPrivate();
            privateBadge.setVisible(isPrivate);
            publicBadge.setVisible(!isPrivate);
            copyCodeButton.setVisible(isPrivate && currentUser.getId() == room.getHostId());
            
            if (isPrivate && currentUser.getId() == room.getHostId()) {
                copyCodeButton.setOnAction(e -> {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(room.getCode());
                    clipboard.setContent(content);
                    
                    // Show feedback
                    String originalText = copyCodeButton.getText();
                    copyCodeButton.setText("Copied!");
                    copyCodeButton.setStyle("-fx-background-color: #d4f571; -fx-border-color: #d9d9d9;");
                    
                    // Reset after 2 seconds
                    Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(2),
                        ae -> {
                            copyCodeButton.setText(originalText);
                            copyCodeButton.setStyle("-fx-background-color: #f1f2f0; -fx-border-color: #d9d9d9;");
                        }));
                    timeline.play();
                });
            }
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

        // Remove timer-based refresh
        // startRefreshTimer();

        // Instead, setup socket connection
        setupSocketConnection(room);

        // Initial refresh
        refreshParticipants();
    }

    private void refreshParticipants() {
        if (participantsGrid == null)
            return;
        List<User> participants = participantDao.getRoomParticipants(currentRoom.getId());
        updateParticipantsUI(participants);
    }

    @FXML
    private void setupSocketConnection(Room room) {
        currentUser = MainApplication.getInstance().getCurrentUser();
        
        if (currentUser.getId() == room.getHostId()) {
            // Load questions for the room
            List<Question> questions = new RoomQuestionDao().getRoomQuestionsInOrder(room.getId());
            // Host: create and start server with questions
            quizServer = new QuizServer(String.valueOf(room.getId()), 8191, questions);
            quizServer.start();
            startClientConnection(room);
        } else {
            startClientConnection(room);
        }
    }

    private void startClientConnection(Room room) {
        quizClient = new QuizClient(String.valueOf(currentUser.getId()), this::handleSocketMessage);
        quizClient.connect(room.getHostIp(), 8191, (int)room.getId());
    }

    private void handleSocketMessage(SocketMessage message) {
        Platform.runLater(() -> {
            switch (message.getType()) {
                case PLAYER_LIST:
                    @SuppressWarnings("unchecked")
                    List<String> playerIds = (List<String>) message.getPayload();
                    List<User> participants = playerIds.stream()
                            .map(id -> userDao.getUserById(Long.parseLong(id)))
                            .collect(Collectors.toList());
                    updateParticipantsUI(participants);
                    break;
                case PLAYER_JOINED:
                    // Just wait for PLAYER_LIST update
                    break;
                case PLAYER_LEFT:
                    // Just wait for PLAYER_LIST update
                    break;
                case START_QUIZ:
                    MainApplication.getInstance().showPlayMulti(currentRoom);
                    break;
            }
        });
    }

    private void updateParticipantsUI(List<User> participants) {
        if (participantsGrid == null)
            return;

        participantsGrid.getChildren().clear();

        if (participantsLabel != null) {
            participantsLabel.setText(participants.size() + "/" + currentRoom.getMaxPlayers() + " joined (Waiting)");
        }

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

        // Load avatar image
        String avatarUrl = participant.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            avatarView.setImage(new Image(avatarUrl));
        } else {
            avatarView.setImage(new Image(
                    getClass().getResource("/org/quizproject/quizproject/Assets/avatar.png").toExternalForm()));
        }

        Label nameLabel = new Label(participant.getName() + " #" + participant.getId());
        nameLabel.setPrefHeight(48.0);
        nameLabel.setPrefWidth(250.0);
        nameLabel.setFont(new Font(24.0));
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        hbox.getChildren().addAll(avatarView, nameLabel);
        pane.getChildren().add(hbox);

        return pane;
    }

    @FXML
    private void handleLeave() {
        if (quizClient != null) {
            quizClient.sendMessage(new SocketMessage(
                    SocketMessage.MessageType.LEAVE_ROOM,
                    currentRoom.getCode(),
                    String.valueOf(currentUser.getId())));
        }

        if (quizServer != null) {
            quizServer.stop();
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
        if (quizServer != null) {
            quizServer.startQuiz();
        }
        
        // Just send START_QUIZ signal without questions
        quizClient.sendMessage(new SocketMessage(
            SocketMessage.MessageType.START_QUIZ,
            currentRoom.getCode(),
            null
        ));
        MainApplication.getInstance().showPlayMulti(currentRoom);
    }

    public void cleanup() {
        if (quizServer != null) {
            quizServer.stop();
        }
        if (quizClient != null) {
            quizClient.sendMessage(new SocketMessage(
                    SocketMessage.MessageType.LEAVE_ROOM,
                    currentRoom.getCode(),
                    String.valueOf(currentUser.getId())));
        }
    }
}
