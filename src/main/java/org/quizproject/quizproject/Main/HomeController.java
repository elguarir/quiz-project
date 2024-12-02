package org.quizproject.quizproject.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.Models.User;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Label userNameLabel;
    @FXML
    private ImageView userAvatar;
    @FXML
    private GridPane categoryGrid;

    // Mock user data
    private final User currentUser = new User() {
        {
            setName("John Doe");
            setAvatar("default-avatar.png");
        }
    };

    // Mock categories
    private final List<Category> categories = Arrays.asList(
            new Category(1, "Science", randomColor()),
            new Category(2, "History", randomColor()),
            new Category(3, "Mathematics", randomColor()),
            new Category(4, "Geography", randomColor()),
            new Category(5, "Literature", randomColor()),
            new Category(6, "Sports", randomColor()));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupUserInfo();
        setupCategoryGrid();
    }

    private void setupUserInfo() {
        userNameLabel.setText(currentUser.getName());
        // Set avatar image - implementation depends on how you store avatars
    }

    private void setupCategoryGrid() {
        int column = 0;
        int row = 0;

        for (Category category : categories) {
            VBox card = createCategoryCard(category);
            categoryGrid.add(card, column, row);

            column++;
            if (column > 2) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createCategoryCard(Category category) {
        VBox card = new VBox();
        card.setPrefSize(250, 150);
        card.setAlignment(Pos.CENTER);

        Color color = category.getBackgroundColor();
        String backgroundColor = String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 10; -fx-cursor: hand;",
                backgroundColor));

        Label nameLabel = new Label(category.getName());
        nameLabel.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");

        card.getChildren().add(nameLabel);
        card.setOnMouseClicked(e -> onCategorySelected(category));

        return card;
    }

    private void onCategorySelected(Category category) {
        // TODO: Implement category selection logic
        System.out.println("Selected category: " + category.getName());
    }

    @FXML
    private void onCreateRoomClicked() {
        // TODO: Implement room creation logic
    }

    private Color randomColor() {
        Random random = new Random();
        return Color.rgb(
                random.nextInt(200) + 55,
                random.nextInt(200) + 55,
                random.nextInt(200) + 55);
    }
}