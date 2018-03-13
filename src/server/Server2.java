package server;

import detection.DetectFaceDemo;
import model.Size;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;


public class Server2 extends Thread {
    private Socket server;

    private int pamiecZdjec;
    private Size mapaRozmiarow;
    private int rozmiarZdjec;

    public Server2(Socket socket, Size mapaRozmiarow) throws IOException {
        server = socket;
        pamiecZdjec = 0;
        this.mapaRozmiarow = mapaRozmiarow;

    }

    public int getPamiecZdjec() {
        return pamiecZdjec;
    }

    public File readImage(DataInputStream input) throws IOException {
        byte[] sizeAr = new byte[8];
        input.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        pamiecZdjec += size;
        rozmiarZdjec = size;
        mapaRozmiarow.dodajSerwer("SERWER2", pamiecZdjec);
        byte[] imageAr = new byte[size];
        input.read(imageAr);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
        System.out.println("Received Server 2    " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
        File f = new File("D:\\FaceDetector\\src\\resources\\faceToDetectFromClientODKLIENTA.png");
        //write image to path of file f
        ImageIO.write(image, "jpg", f);
        return f;
    }

    public void writeImage(String filename, DataOutputStream output) throws IOException {
        BufferedImage image2 = ImageIO.read(new File(filename));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image2, "jpg", byteArrayOutputStream);

        byte[] size2 = ByteBuffer.allocate(8).putInt(byteArrayOutputStream.size()).array();
        int size = ByteBuffer.wrap(size2).asIntBuffer().get();
        pamiecZdjec -= rozmiarZdjec;
        mapaRozmiarow.dodajSerwer("SERWER2", pamiecZdjec);
        output.write(size2);
        output.write(byteArrayOutputStream.toByteArray());
        output.flush();
        System.out.println("Wysyla serwer 2 " + System.currentTimeMillis());
    }

    public void run() {

        try {

            DataInputStream input = new DataInputStream(server.getInputStream());
            DataOutputStream output = new DataOutputStream(server.getOutputStream());

            String pamiecZdjecStr = String.valueOf(this.getPamiecZdjec());
            output.writeUTF(pamiecZdjecStr);

            String decyzja = input.readUTF();


            if (decyzja.equals("RUN")) {
                //Przetworzenie zdjecia
                System.out.println("Decyzja  zdjecia przetwarza serwer 2");
                String filename = new DetectFaceDemo(readImage(input)).run();
                //send to server 1
                writeImage(filename, output);
            }
            output.close();
            input.close();

        } catch (IOException e) {
            System.out.println("Oops: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {

            }
        }

    }
}