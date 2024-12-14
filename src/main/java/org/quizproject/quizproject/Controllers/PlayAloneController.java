package org.quizproject.quizproject.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.quizproject.quizproject.Dao.QuestionDao;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.Models.Option;
import org.quizproject.quizproject.Models.User;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

import java.util.List;

public class PlayAloneController {

    @FXML
    private Label QstNum;

    @FXML
    private Label labelQuestionText;
    @FXML
    private Label labelAnswer1;
    @FXML
    private Label labelAnswer2;
    @FXML
    private Label labelAnswer3;
    @FXML
    private Label labelAnswer4;
    @FXML
    private Label timer;
    @FXML
    private Button nextButton;
    @FXML
    private Button finishButton;

    @FXML
    private Label username;

    @FXML
    private ImageView userLogo;

    @FXML
    private GridPane answersGrid;

    private Category category;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private QuestionDao questionDao = new QuestionDao();

    private Timeline timeline;
    private int timeRemaining = 120;

    private Option selectedOption;
    private Pane selectedPane;

    private int correctAnswers = 0;
    private int initialTime = 120;

    private static final int TOTAL_QUESTIONS = 20;

    @FXML
    public void initialize() {
        startTimer(); // Start the countdown timer
        loadQuestions();
        displayQuestion();

        User currentUser = MainApplication.getInstance().getCurrentUser();
        username.setText(currentUser.getName());
        String avatarUrl = currentUser.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            userLogo.setImage(new Image(avatarUrl));
        }
    }

    // Initialize and start the timer
    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Update the timer label every second
    private void updateTimer() {
        if (timeRemaining > 0) {
            timeRemaining--;
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            timer.setText(String.format("%02d:%02d Min left", minutes, seconds));
        } else {
            timeline.stop();
            timer.setText("00:00");
            finishButton.setDisable(true);
            nextButton.setDisable(true);
            int timeTaken = initialTime - timeRemaining;
            MainApplication.getInstance().showQuizResults(correctAnswers, questions.size(), timeTaken);
        }
    }

    // Load questions for a given category
    private void loadQuestions() {
        Category category = CreateQuizController.getCategory();
        System.out.println("Selected CAT " + category.getName());
        
        // Get exactly 20 random questions instead of paginating
        questions = questionDao.getRandomQuestionsByCategory(category.getId(), TOTAL_QUESTIONS);
        
        if (questions.isEmpty()) {
            System.out.println("No questions found for this category.");
            nextButton.setDisable(true);
            finishButton.setDisable(true);
        } else {
            System.out.println("Loaded " + questions.size() + " questions");
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            QstNum.setText("Question " + (currentQuestionIndex + 1) + "/" + TOTAL_QUESTIONS);
            labelQuestionText.setText(question.getContent());

            // Set next button text based on question position
            if (currentQuestionIndex == questions.size() - 1) {
                nextButton.setText("Show Results");
            } else {
                nextButton.setText("Next");
            }

            answersGrid.getChildren().clear();
            selectedOption = null;
            selectedPane = null;

            List<Option> options = question.getOptions();

            for (int i = 0; i < options.size(); i++) {
                Option option = options.get(i);
                Pane optionPane = createOptionPane(option);

                answersGrid.add(optionPane, i % 2, i / 2);
            }
        }
    }

    private Pane createOptionPane(Option option) {
        Pane pane = new Pane();
        pane.setPrefHeight(200.0);
        pane.setPrefWidth(200.0);
        pane.setStyle("-fx-background-color: #404040; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Label label = new Label(option.getContent());
        label.setAlignment(Pos.CENTER);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setWrapText(true);
        label.setFont(new Font("System Bold", 16.0));
        label.setPrefWidth(330.0);
        label.setPrefHeight(101.0);
        label.setLayoutX(17.0);
        label.setLayoutY(13.0);

        pane.getChildren().add(label);

        pane.setOnMouseClicked(e -> handleOptionSelection(option, pane));

        return pane;
    }

    private void handleOptionSelection(Option option, Pane pane) {
        if (selectedPane != null) {
            selectedPane
                    .setStyle("-fx-background-color: #404040; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        }

        selectedOption = option;
        selectedPane = pane;
        pane.setStyle("-fx-background-color: #97d6f7; -fx-background-radius: 10px; -fx-border-radius: 10px;");
    }

    @FXML
    public void onNextButtonClicked(ActionEvent event) {
        if (selectedOption != null) {
            if (selectedOption.isCorrect()) {
                correctAnswers++;
            }
        }

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion();
        } else {
            nextButton.setDisable(true);
            timeline.stop();
            int timeTaken = initialTime - timeRemaining;
            MainApplication.getInstance().showQuizResults(correctAnswers, questions.size(), timeTaken);
        }
    }

    // Handle click event on 'Finish' button
    @FXML
    public void onFinishButtonClicked(ActionEvent event) {
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Confirm Finish");

        Label message = new Label("Are you sure you want to finish?\nAll unsaved progress will be lost.");

        Button cancelButton = new Button("Cancel");
        Button okButton = new Button("OK");

        cancelButton.setOnAction(e -> {
            dialog.close(); // Close dialog and cancel action
            System.out.println("Quiz not finished");
        });

        okButton.setOnAction(e -> {
            dialog.close(); // Close dialog
            finishButton.setDisable(true);
            nextButton.setDisable(true);

            timeline.stop(); // Stop the timer
            int timeTaken = initialTime - timeRemaining;
            MainApplication.getInstance().showQuizResults(correctAnswers, questions.size(), timeTaken);
        });

        HBox buttonLayout = new HBox(10, cancelButton, okButton);
        buttonLayout.setStyle("-fx-alignment: center-right;");

        VBox layout = new VBox(10, message, buttonLayout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        dialog.showAndWait();
    }

}
