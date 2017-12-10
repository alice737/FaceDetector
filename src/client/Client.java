package client;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client  {




    public static void main(String[] args) {

        try {



            Socket socket= new Socket("localhost", 9090);


            DataOutputStream output=new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            BufferedImage image = ImageIO.read(new File("D:\\FaceDetector\\src\\resources\\faceToDetectClient2.png"));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            output.write(size);
            output.write(byteArrayOutputStream.toByteArray());
            output.flush();
            System.out.println("Flushed: " + System.currentTimeMillis());




            System.out.println("Reading: " + System.currentTimeMillis());
            byte[] sizeAr = new byte[4];
            input.read(sizeAr);
            int size2 = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size2];
            input.read(imageAr);

            BufferedImage image2 = ImageIO.read(new ByteArrayInputStream(imageAr));

            System.out.println("Received " + image2.getHeight() + "x" + image2.getWidth() + ": " + System.currentTimeMillis());
            ImageIO.write(image2, "jpg", new File("D:\\FaceDetector\\src\\resources\\faceDetectionFromServer.png"));


            output.writeUTF("pierwsze zdjęcie przetworzone");

            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

