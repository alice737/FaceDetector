package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Datastore;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseFileScreen implements Initializable {

    Scene scene;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickDalej(ActionEvent actionEvent) throws IOException {


        Pane fmxlLoader = FXMLLoader.load(getClass().getResource("/view/ProcessPreviewScreen.fxml"));
        this.scene = new Scene(fmxlLoader);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(this.scene);

        stage.show();
    }

    public void onClickWybierzPliki(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz zdjęcia");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki Obrazów", "*.png", "*.jpg"));

        List<File> files = fileChooser.showOpenMultipleDialog(null);
        for (File file : files) {
            System.out.println(file.getPath());
        }

        Datastore.getInstance().setFiles(files);
    }


    public void onClickWybierzMiejsceZapisu(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz miejsce do zapisu");
        File outputFolder = directoryChooser.showDialog(null);

        System.out.println(outputFolder.getAbsolutePath());

        Datastore.getInstance().setDirectoryPath(outputFolder.getAbsolutePath());
        try {

            Socket socket = new Socket("192.168.1.100", 9090);

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
//sciezka do wybranego zdjecia
            String wybraneZdjecie = Datastore.getInstance().getFiles().get(0).toString();
            System.out.println(wybraneZdjecie);

            BufferedImage image = ImageIO.read(new File(wybraneZdjecie));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(8).putInt(byteArrayOutputStream.size()).array();
            output.write(size);
            output.write(byteArrayOutputStream.toByteArray());
            output.flush();
            System.out.println("Flushed: " + System.currentTimeMillis());
            System.out.println("Reading: " + System.currentTimeMillis());

            byte[] sizeAr = new byte[8];
            input.read(sizeAr);
            int size2 = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size2];
            input.read(imageAr);
            BufferedImage image2 = ImageIO.read(new ByteArrayInputStream(imageAr));
            //     System.out.println("Received " + image2.getHeight() + "x" + image2.getWidth() + ": " + System.currentTimeMillis());
            //wybrany katalog
            String wybranyKatalog = Datastore.getInstance().getDirectoryPath().toString();
            ImageIO.write(image2, "jpg", new File(wybranyKatalog + "\\twarzePoDetekcji.png"));

            output.close();
            input.close();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
