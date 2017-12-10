package app;
import org.opencv.core.Core;
import server.Server;

import java.io.*;
import java.net.ServerSocket;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, OpenCV");

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int port = 9090;
        try (ServerSocket serverSocket = new ServerSocket(port)){

            while(true) {
                new Server(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}



