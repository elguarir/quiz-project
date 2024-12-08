
package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.quizproject.quizproject.MainApplication;

public class QuizResultsController {
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label percentageLabel;


    public void setResults(int correctAnswers, int totalQuestions, int timeTaken) {
        scoreLabel.setText(String.format("Your Score: %d/%d", correctAnswers, totalQuestions));
        timeLabel.setText(String.format("Time Taken: %d:%02d", timeTaken / 60, timeTaken % 60));
        double percentage = (double) correctAnswers / totalQuestions * 100;
        percentageLabel.setText(String.format("Percentage: %.1f%%", percentage));
    }

    @FXML
    private void onTryAgainClicked() {
        MainApplication.getInstance().showPlayAlone();
    }

    @FXML
    private void onHomeClicked() {
        MainApplication.getInstance().showHomeScreen();
    }
}