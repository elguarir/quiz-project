package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import org.quizproject.quizproject.Dao.CategoryDao;
import org.quizproject.quizproject.MainApplication;
import org.quizproject.quizproject.Models.Category;
import org.quizproject.quizproject.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HomeController {
    @FXML
    private Label userNameLabel;
    @FXML
    private GridPane categoryGrid;
    @FXML
    private ImageView avatarImage;

    private final CategoryDao categoryDao = new CategoryDao();

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
            
            // Add row constraints dynamically
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

}