package org.quizproject.quizproject.Main;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Home extends VBox {
    private Label titleLabel;
    private Label welcomeLabel;

    public Home() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #f0f0f0;");

        titleLabel = new Label("Quiz Application");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        welcomeLabel = new Label("Welcome to the Quiz Application!");
        welcomeLabel.setFont(Font.font("Arial", 18));
        welcomeLabel.setStyle("-fx-text-fill: #34495e;");

        this.getChildren().addAll(titleLabel, welcomeLabel);
    }
}
