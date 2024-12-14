package org.quizproject.quizproject.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.quizproject.quizproject.Dao.QuestionDao;
import org.quizproject.quizproject.Dao.RoomParticipantDao;
import org.quizproject.quizproject.Dao.RoomQuestionDao;
import org.quizproject.quizproject.Dao.UserDao;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.*;
import org.quizproject.quizproject.Sockets.QuizClient;
import org.quizproject.quizproject.Sockets.SocketMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiplayerController {
    @FXML private Label QstNum;
    @FXML private Label labelQuestionText;
    @FXML private GridPane answersGrid;
    @FXML private Label timer;
    @FXML private Button nextButton;
    @FXML private Button finishButton;
    @FXML private Label username;
    @FXML private ImageView userLogo;
    @FXML private VBox progressVBox;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private QuestionDao questionDao = new QuestionDao();
    private Timeline timeline;
    private int timeRemaining;
    private Option selectedOption;
    private Pane selectedPane;
    private int correctAnswers = 0;
    private QuizClient quizClient; // Changed from Client to QuizClient
    private Map<String, Integer> playerProgress = new HashMap<>();
    private Room currentRoom;
    private QuizProgress localProgress;

    @FXML
    public void initialize() {
        User currentUser = MainApplication.getInstance().getCurrentUser();
        if (username != null) {
            username.setText(currentUser.getName());
        }
        
        if (currentUser.getAvatar() != null && userLogo != null) {
            userLogo.setImage(new Image(currentUser.getAvatar()));
        }
        
        // Disable next button initially
        if (nextButton != null) {
            nextButton.setDisable(true);
        }
        
        // Initially hide finish button
        if (finishButton != null) {
            finishButton.setVisible(false);
        }
    }

    public void initializeRoom() {
        setupSocketConnection();
        
        // Initialize progress tracking for all participants first
        RoomParticipantDao participantDao = new RoomParticipantDao();
        List<User> participants = participantDao.getRoomParticipants(currentRoom.getId());
        
        System.out.println("Initializing progress for " + participants.size() + " participants");
        for (User participant : participants) {
            playerProgress.put(String.valueOf(participant.getId()), 0);
        }
        
        // Then load questions and start timer
        loadQuestions();
        startTimer();
        
        // Initial progress UI update
        updateProgressUI(playerProgress);
        
        // If host, broadcast initial progress
        if (MainApplication.getInstance().getCurrentUser().getId() == currentRoom.getHostId()) {
            quizClient.sendMessage(new SocketMessage(
                SocketMessage.MessageType.PROGRESS_UPDATE,
                currentRoom.getCode(),
                playerProgress
            ));
        }
    }

    private void setupSocketConnection() {
        quizClient = new QuizClient(
            String.valueOf(MainApplication.getInstance().getCurrentUser().getId()),
            this::handleSocketMessage
        );
        quizClient.connect(currentRoom.getHostIp(), 8191, (int)currentRoom.getId());
    }

    private void handleSocketMessage(SocketMessage message) {
        Platform.runLater(() -> {
            switch (message.getType()) {
                case START_QUIZ:
                    // No need to handle received questions, just start the quiz if not started
                    if (questions == null) {
                        loadQuestions();
                    }
                    break;
                case PROGRESS_UPDATE:
                    @SuppressWarnings("unchecked")
                    Map<String, Integer> receivedProgress = (Map<String, Integer>) message.getPayload();
                    System.out.println("Received progress update: " + receivedProgress);
                    // Update local progress with received data
                    playerProgress.clear();
                    playerProgress.putAll(receivedProgress);
                    updateProgressUI(playerProgress);
                    break;
                case QUIZ_COMPLETED:
                    handleQuizCompleted((String) message.getPayload());
                    break;
            }
        });
    }

    private void startTimer() {
        timeRemaining = currentRoom.getQuizTime() * 60;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimer() {
        if (timeRemaining > 0) {
            timeRemaining--;
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            timer.setText(String.format("%02d:%02d Min left", minutes, seconds));
        } else {
            finishQuiz();
        }
    }

    private void loadQuestions() {
        try {
            RoomQuestionDao roomQuestionDao = new RoomQuestionDao();
            questions = roomQuestionDao.getRoomQuestionsInOrder(currentRoom.getId());
            
            if (questions == null || questions.isEmpty()) {
                System.err.println("No questions loaded for room " + currentRoom.getId());
                return;
            }

            System.out.println("Loaded " + questions.size() + " questions");
            
            // Validate that all questions have options
            boolean allQuestionsValid = questions.stream()
                .allMatch(q -> q.getOptions() != null && !q.getOptions().isEmpty());
                
            if (!allQuestionsValid) {
                System.err.println("Some questions are missing options!");
                return;
            }
            
            // Initialize progress tracking
            localProgress = new QuizProgress(questions.size());
            currentQuestionIndex = 0;
            displayQuestion();
            
            // Only host needs to send START_QUIZ message
            if (MainApplication.getInstance().getCurrentUser().getId() == currentRoom.getHostId()) {
                quizClient.sendMessage(new SocketMessage(
                    SocketMessage.MessageType.START_QUIZ,
                    currentRoom.getCode(),
                    null  // No need to send questions anymore
                ));
            }
        } catch (Exception e) {
            System.err.println("Error loading questions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayQuestion() {
        if (answersGrid == null || questions == null || currentQuestionIndex >= questions.size()) {
            System.out.println("Cannot display question: " + 
                             (answersGrid == null ? "grid is null" : 
                              questions == null ? "questions is null" : 
                              "index out of bounds"));
            return;
        }

        Platform.runLater(() -> {
            try {
                answersGrid.getChildren().clear();
                Question question = questions.get(currentQuestionIndex);
                
                if (question.getOptions() == null || question.getOptions().isEmpty()) {
                    System.err.println("No options found for question: " + question.getContent());
                    return;
                }

                System.out.println("Displaying question " + (currentQuestionIndex + 1) + ": " + question.getContent());

                // Update question display
                if (QstNum != null) {
                    QstNum.setText("Question " + (currentQuestionIndex + 1));
                }
                if (labelQuestionText != null) {
                    labelQuestionText.setText(question.getContent());
                }

                // Display options
                List<Option> options = question.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    Option option = options.get(i);
                    VBox optionPane = createOptionPane(option);
                    answersGrid.add(optionPane, i % 2, i / 2);
                }

                // Reset selection state
                selectedOption = null;
                selectedPane = null;
                if (nextButton != null) {
                    nextButton.setDisable(false); // Enable next button for skipping
                }

                // Update progress
                updateProgress();
            } catch (Exception e) {
                System.err.println("Error displaying question: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private VBox createOptionPane(Option option) {
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setPrefHeight(90.0);
        pane.setPrefWidth(270.0);
        pane.setStyle("-fx-background-color: #404040; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        pane.setPadding(new Insets(10));

        Label label = new Label(option.getContent());
        label.setAlignment(Pos.CENTER);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setWrapText(true);
        label.setFont(new Font("System Bold", 16.0));
        label.setPrefWidth(250.0);
        label.setPrefHeight(80.0);
        
        pane.getChildren().add(label);
        pane.setOnMouseClicked(e -> handleOptionSelection(option, pane));
        
        return pane;
    }

    private void handleOptionSelection(Option option, Pane pane) {
        if (selectedPane != null) {
            selectedPane.setStyle("-fx-background-color: #404040; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        }

        selectedOption = option;
        selectedPane = pane;
        pane.setStyle("-fx-background-color: #97d6f7; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        
        // Enable next button when an option is selected
        if (nextButton != null) {
            nextButton.setDisable(false);
            System.out.println("Next button enabled");
        }
    }

    private void updateProgress() {
        if (localProgress != null && questions != null) {
            String userId = String.valueOf(MainApplication.getInstance().getCurrentUser().getId());
            int progress = currentQuestionIndex;
            
            // Update local progress
            playerProgress.put(userId, progress);
            
            // Always broadcast progress to all players
            System.out.println("Broadcasting progress update: " + playerProgress);
            quizClient.sendMessage(new SocketMessage(
                SocketMessage.MessageType.PROGRESS_UPDATE,
                currentRoom.getCode(),
                new HashMap<>(playerProgress) // Send a copy of the progress map
            ));
            
            updateProgressUI(playerProgress);
        }
    }

    private void updateProgressUI(Map<String, Integer> progress) {
        if (progressVBox == null) return;

        Platform.runLater(() -> {
            try {
                progressVBox.getChildren().clear();

                // Get participants and sort them by progress
                List<User> participants = new RoomParticipantDao()
                    .getRoomParticipants(currentRoom.getId());

                // Sort participants by progress (completed first, then by question number)
                participants.sort((p1, p2) -> {
                    String id1 = String.valueOf(p1.getId());
                    String id2 = String.valueOf(p2.getId());
                    
                    int progress1 = progress.getOrDefault(id1, 0);
                    int progress2 = progress.getOrDefault(id2, 0);
                    
                    // Handle completed (-1) cases
                    if (progress1 == -1 && progress2 != -1) return -1;
                    if (progress1 != -1 && progress2 == -1) return 1;
                    if (progress1 == -1 && progress2 == -1) return 0;
                    
                    // Sort by progress (highest first)
                    return Integer.compare(progress2, progress1);
                });

                // Add sorted participants to UI
                for (User participant : participants) {
                    String userId = String.valueOf(participant.getId());
                    int questionNumber = progress.getOrDefault(userId, 0);
                    HBox playerProgress = createProgressBox(participant, questionNumber);
                    if (playerProgress != null) {
                        progressVBox.getChildren().add(playerProgress);
                    }
                }

                System.out.println("Updated progress UI with " + participants.size() + 
                                 " players (sorted by progress)");
            } catch (Exception e) {
                System.err.println("Error updating progress UI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private HBox createProgressBox(User user, int questionNumber) {
        HBox progressBox = new HBox();
        progressBox.setAlignment(Pos.CENTER_LEFT);
        progressBox.setPrefHeight(47.0);
        progressBox.setPrefWidth(215.0);
        progressBox.setSpacing(10.0);
        progressBox.setStyle("-fx-border-color: #d9d9d9; -fx-background-radius: 5px; -fx-border-radius: 5px;");
        progressBox.setPadding(new Insets(5));

        // Avatar
        ImageView avatarView = new ImageView();
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            avatarView.setImage(new Image(user.getAvatar()));
        } else {
            avatarView.setImage(new Image(
                getClass().getResource("/org/quizproject/quizproject/Assets/avatar.png").toExternalForm()));
        }
        avatarView.setFitHeight(24.0);
        avatarView.setFitWidth(24.0);

        // Username with host indicator
        String nameText = user.getName();
        if (user.getId() == currentRoom.getHostId()) {
            nameText += " (Host)";
        }
        Label usernameLabel = new Label(nameText);
        usernameLabel.setPrefWidth(114.0);
        usernameLabel.setTextFill(javafx.scene.paint.Color.valueOf("#696767"));
        usernameLabel.setFont(new Font("System Italic", 12.0));

        // Progress indicator
        String progressText;
        if (questionNumber == -1) {
            progressText = "Completed";
        } else if (questions == null) {
            progressText = "Waiting...";
        } else {
            progressText = (questionNumber + 1) + "/" + questions.size();
        }
        
        Label progressLabel = new Label(progressText);
        progressLabel.setTextFill(javafx.scene.paint.Color.valueOf("#696767"));
        progressLabel.setFont(new Font("System Italic", 12.0));

        progressBox.getChildren().addAll(avatarView, usernameLabel, progressLabel);
        return progressBox;
    }


    @FXML
    public void onNextButtonClicked(ActionEvent event) {
        if (questions == null || currentQuestionIndex >= questions.size() - 1) {
            return;
        }

        // Check if an option was selected and update score
        if (selectedOption != null && selectedOption.isCorrect()) {
            correctAnswers++;
            if (localProgress != null) {
                localProgress.updateScore(
                    String.valueOf(MainApplication.getInstance().getCurrentUser().getId()),
                    correctAnswers
                );
            }
        }
        
        // Move to next question
        currentQuestionIndex++;
        selectedOption = null;
        selectedPane = null;
        nextButton.setDisable(false); // Keep enabled for skipping
        displayQuestion();
        
        // Send progress update immediately after moving to next question
        updateProgress();
        
        // If last question, show finish button
        if (currentQuestionIndex >= questions.size() - 1) {
            if (finishButton != null) {
                finishButton.setVisible(true);
            }
            if (nextButton != null) {
                nextButton.setVisible(false);
            }
        }
    }

    @FXML
    public void onFinishButtonClicked(ActionEvent event) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Confirm Finish");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label message = new Label("Are you sure you want to finish?\nAll unsaved progress will be lost.");
        Button cancelButton = new Button("Cancel");
        Button okButton = new Button("OK");

        HBox buttons = new HBox(10, cancelButton, okButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        cancelButton.setOnAction(e -> dialog.close());
        okButton.setOnAction(e -> {
            dialog.close();
            finishQuiz();
        });

        layout.getChildren().addAll(message, buttons);
        dialog.setScene(new Scene(layout));
        dialog.showAndWait();
    }

    private void finishQuiz() {
        timeline.stop();
        quizClient.sendMessage(new SocketMessage(
            SocketMessage.MessageType.QUIZ_COMPLETED,
            currentRoom.getCode(),
            String.valueOf(MainApplication.getInstance().getCurrentUser().getId())
        ));
        MainApplication.getInstance().showQuizResults(correctAnswers, questions.size(), 
            currentRoom.getQuizTime() * 60 - timeRemaining);
    }

    private void handleQuizCompleted(String userId) {
        // Handle when other players complete the quiz
        playerProgress.put(userId, -1); // -1 indicates completed
        updateProgressUI(playerProgress);
    }

    public void cleanup() {
        if (timeline != null) {
            timeline.stop();
        }
        if (quizClient != null) {
            quizClient.sendMessage(new SocketMessage(
                SocketMessage.MessageType.LEAVE_ROOM,
                currentRoom.getCode(),
                String.valueOf(MainApplication.getInstance().getCurrentUser().getId())
            ));
        }
    }

    public void setRoom(Room room) {
        this.currentRoom = room;
        initializeRoom();
    }

    @Override
    protected void finalize() {
        cleanup();
    }
}
