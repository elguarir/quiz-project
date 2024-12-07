
package org.quizproject.quizproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.quizproject.quizproject.Models.Category;

public class CreateQuizController {
    @FXML
    private TextField quizTitleField;
    @FXML
    private TextArea quizDescriptionField;

    private Category selectedCategory;
    private Stage dialogStage;

    public void setCategory(Category category) {
        this.selectedCategory = category;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleCreateQuiz() {
        String title = quizTitleField.getText();
        String description = quizDescriptionField.getText();
        
        // TODO: Implement quiz creation logic
        
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}