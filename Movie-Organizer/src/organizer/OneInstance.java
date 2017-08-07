package organizer;

import java.io.IOException;
import java.net.ServerSocket;

public class OneInstance {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1334);
            System.out.println("OK to continue running.");
            System.out.println("Press any key to exit.");
            System.in.read();
            serverSocket.close();
        } catch (IOException x) {
            System.out.println("Another instance already running... exit.");
        }
    }
}