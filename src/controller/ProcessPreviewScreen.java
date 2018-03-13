package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Datastore;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProcessPreviewScreen implements Initializable {

    public void setImageAfterDetect(Image img) {

        this.imageAfterDetect.setImage(img);
    }



    @FXML
    private Button koniec;
    @FXML
    private ImageView imageAfterDetect;
    private Scene scene;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onClickKoniec(ActionEvent actionEvent) {
//        koniec.setOnAction(e -> Platform.exit());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Koniec programu");
        alert.setHeaderText("Czy na pewno chcesz zakoĹ„czyÄ‡ program?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            } else {
                alert.close();
            }
        });

    }

    public void onClickOtworzFolder(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().open(new File(Datastore.getInstance().getDirectoryPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickWgrajNowe(ActionEvent actionEvent) throws IOException {

        Pane fmxlLoader = FXMLLoader.load(getClass().getResource("/view/ChooseFileScreen.fxml"));
        // Parent window = (AnchorPane) fmxlLoader.load();
        this.scene = new Scene(fmxlLoader);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(this.scene);

        stage.show();
    }

    public void otworzZdjecie(ActionEvent actionEvent) {
        try {
//            File f = new File("c:\\temp\\test.bmp");
//            Desktop dt = Desktop.getDesktop();
//            dt.open(f);

            Desktop.getDesktop().open(new File(Datastore.getInstance().getDirectoryPath()+"\\twarzePoDetekcji.png"));
        }catch(IOException e){
            System.out.println("controller error otworzZdęcie: " +e.getMessage());
        }
    }


}
