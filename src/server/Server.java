package server;

import detection.DetectFaceDemo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;


public class Server extends Thread {
    private Socket server;

    public Server(Socket socket) throws IOException {
        server = socket;

    }

    public void run() {

        try {

            DataInputStream input = new DataInputStream(server.getInputStream());
            DataOutputStream output = new DataOutputStream(server.getOutputStream());

            System.out.println("Reading: " + System.currentTimeMillis());
            //read size of image and all image
            byte[] sizeAr = new byte[4];
            input.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size];
            input.read(imageAr);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
            System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
            File f = new File("D:\\FaceDetector\\src\\resources\\faceToDetectFromClient.png");
            //write image to path of file f
            ImageIO.write(image, "jpg", f);

            //detect faces from recive photo
            String filename = new DetectFaceDemo(f).run();

            //send to client
            BufferedImage image2 = ImageIO.read(new File(filename));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image2, "jpg", byteArrayOutputStream);

            byte[] size2 = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            output.write(size2);
            output.write(byteArrayOutputStream.toByteArray());
            output.flush();
            System.out.println("Flushed: " + System.currentTimeMillis());


            System.out.println(input.readUTF());

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