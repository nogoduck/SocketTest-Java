package application.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStart {
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(5005);
            System.out.println("Running server...");

            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                Thread thread = new PersonalServer(socket);
                System.out.println("Connected server");
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
