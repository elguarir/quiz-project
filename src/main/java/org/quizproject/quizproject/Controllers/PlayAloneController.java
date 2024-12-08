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

    private Category category;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private QuestionDao questionDao = new QuestionDao();

    private Timeline timeline;
    private int timeRemaining = 120;

    

    @FXML
    public void initialize() {
        startTimer();  // Start the countdown timer
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
            onFinishButtonClicked(null);  // Trigger quiz end
        }
    }

    // Load questions for a given category
    private void loadQuestions() {
        Category category = CreateQuizController.getCategory();
        System.out.println("Selected CAT " + category.getName());
        questions = questionDao.getQuestionsByCategory(category.getId());
        if (questions.isEmpty()) {
            System.out.println("No questions found for this category.");
            nextButton.setDisable(true);
            finishButton.setDisable(true);// Disable "Next" button if no questions exist
        }
    }

    // Display the current question and its options
    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            QstNum.setText("Question "+question.getId());
            labelQuestionText.setText(question.getContent());

            List<Option> options = question.getOptions();
            labelAnswer1.setText(options.get(0).getContent());
            labelAnswer2.setText(options.get(1).getContent());
            labelAnswer3.setText(options.get(2).getContent());
            labelAnswer4.setText(options.get(3).getContent());
        }
    }

    // Handle click event on 'Next' button
    @FXML
    public void onNextButtonClicked(ActionEvent event) {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion();  // Update the display
        } else {
            nextButton.setDisable(true);  // Disable the next button when there are no more questions
            System.out.println("End of questions.");
            timeline.stop();

        }
    }

    // Handle click event on 'Finish' button
    @FXML
    public void onFinishButtonClicked(ActionEvent event) {
        // Create a custom dialog
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Confirm Finish");

        // Create a message label
        Label message = new Label("Are you sure you want to finish?\nAll unsaved progress will be lost.");

        // Create "Cancel" and "OK" buttons
        Button cancelButton = new Button("Cancel");
        Button okButton = new Button("OK");

        // Add button actions
        cancelButton.setOnAction(e -> {
            dialog.close(); // Close dialog and cancel action
            System.out.println("Quiz not finished");
        });

        okButton.setOnAction(e -> {
            dialog.close(); // Close dialog
            finishButton.setDisable(true);
            nextButton.setDisable(true);

            timeline.stop(); // Stop the timer
            MainApplication.getInstance().showHomeScreen();
            // Additional actions like saving quiz results can be added here
        });

        // Customize button layout: Cancel on the left, OK on the right
        HBox buttonLayout = new HBox(10, cancelButton, okButton);
        buttonLayout.setStyle("-fx-alignment: center-right;");

        // Create the layout
        VBox layout = new VBox(10, message, buttonLayout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Set up the dialog scene
        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        // Show the dialog
        dialog.showAndWait();
    }


}
