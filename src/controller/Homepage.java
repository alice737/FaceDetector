package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Homepage implements Initializable {
    private static ChooseFileScreen con;
    Scene scene;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onClickDalej(ActionEvent actionEvent) throws IOException {

        Pane fmxlLoader = FXMLLoader.load(getClass().getResource("/view/ChooseFileScreen.fxml"));
        this.scene = new Scene(fmxlLoader);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(this.scene);
        stage.show();

    }

}



