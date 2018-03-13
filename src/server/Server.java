package server;

import detection.DetectFaceDemo;
import model.Size;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;


public class Server extends Thread {
    private Socket server;

    private int pamiecZdjec;

    private int rozmiarZdjec;

    private Size mapaRozmiarow;

    public Server(Socket socket, Size mapaRozmiarow) throws IOException {
        server = socket;
        pamiecZdjec = 0;
        this.mapaRozmiarow = mapaRozmiarow;
    }

    public int getPamiecZdjec() {
        return pamiecZdjec;
    }

    public File readImage(String filepath, DataInputStream input) throws IOException {
        byte[] sizeAr = new byte[8];
        input.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        rozmiarZdjec = size;
        byte[] imageAr = new byte[size];
        input.read(imageAr);
        System.out.println("\nCzytany rozmiar zdj " + size);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
        System.out.println("Received Server 1    " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());

        File f = new File(filepath);
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
        System.out.println("\nPisze rozmiar zdj " + size);

        pamiecZdjec -= rozmiarZdjec;
        mapaRozmiarow.dodajSerwer("SERWER1", pamiecZdjec);
        output.write(size2);
        output.write(byteArrayOutputStream.toByteArray());
        output.flush();

    }

    public void run() {

        try {

            Socket socketServer2 = new Socket("localhost", 1010);

            DataInputStream inputsocketServer2 = new DataInputStream(socketServer2.getInputStream());
            DataOutputStream outputsocketServer2 = new DataOutputStream(socketServer2.getOutputStream());

            DataInputStream input = new DataInputStream(server.getInputStream());
            DataOutputStream output = new DataOutputStream(server.getOutputStream());

            //read size of image and all image

            File file = readImage("D:\\FaceDetector\\src\\resources\\faceToDetectFromClientODKLIENTA.png", input).getAbsoluteFile();
            String path = file.getAbsolutePath();

            System.out.println("Pamiec zdjec sewer 1 " + getPamiecZdjec());
            Integer i = Integer.parseInt(inputsocketServer2.readUTF());
            mapaRozmiarow.dodajSerwer("SERWER2", i);
            System.out.println("Mapa informacje przed " + mapaRozmiarow.getSize("SERWER1"));
            System.out.println("Mapa informacje przed " + mapaRozmiarow.getSize("SERWER2"));

            System.out.println("PamiecZdjec od Serwera 2   " + i);
            if (mapaRozmiarow.getBestSerwer().equals("SERWER2")) {
                System.out.println("-------------------------------");
                System.out.println("PRZETWARZA SERWER2");

                outputsocketServer2.writeUTF("RUN");

                //wysylam zdjecia do serwera 2 lub 3
                writeImage(path, outputsocketServer2);

                ///odbieranie od serwera 2
                String filepathS = "D:\\FaceDetector\\src\\resources\\odServera2poPrzetworzeniu.png";
                readImage(filepathS, inputsocketServer2);

                //do klienta ta przetworzone
                writeImage(filepathS, output);

            } else {
                System.out.println("-------------------------------");
                System.out.println("PRZETWARZA SERWER1");
                mapaRozmiarow.dodajSerwer("SERWER1", rozmiarZdjec);
                System.out.println("Mapa informacje" + mapaRozmiarow.getSize("SERWER1"));
                pamiecZdjec += rozmiarZdjec;
                String filename = new DetectFaceDemo(file).run();
                //send to client
                writeImage(filename, output);
                output.close();
                input.close();
            }
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