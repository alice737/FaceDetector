package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Client extends Application {

    private static Stage primaryStageObj;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStageObj = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Homepage.fxml"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Face Detector");
        // primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/plug.png").toString()));
        Scene mainScene = new Scene(root, 600, 600);
        mainScene.setRoot(root);
        primaryStage.setResizable(true);
        primaryStage.setScene(mainScene);
        primaryStage.show();
      primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Koniec programu");
            alert.setHeaderText("Czy na pewno chcesz zakonczyc program?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Platform.exit();
                } else {
                    we.consume();
                }
            });
        });
    }
}
