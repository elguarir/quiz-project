package org.quizproject.quizproject.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

    public class QuizPageController {

        public void handleButtonClick() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/quizproject/quizproject/RoomOptions.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Room Options");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

