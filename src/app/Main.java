package app;

import model.Size;
import org.opencv.core.Core;
import server.Server;
import server.Server2;

import java.io.*;
import java.net.ServerSocket;


public class Main {
    private static Size mapaRozmiarow=new Size();

    public static void main(String[] args) {
        mapaRozmiarow.dodajSerwer("SERWER1", 0);
        mapaRozmiarow.dodajSerwer("SERWER2", 0);
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int port = 9090;
        int port2 = 1010;
        int port3 = 9091 ;

        Server server1;
        Server2 server2;

        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            ServerSocket serverSocket2 = new ServerSocket(port2);

            while (true) {
                server1 = new Server(serverSocket.accept(),mapaRozmiarow);
                server1.start();

                server2 = new Server2(serverSocket2.accept(),mapaRozmiarow);
                server2.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}



