package application._origin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;


public class Server {
    ServerSocket serverSocket = null;
    Vector<ConnectUser> userAllList;
    Vector<ConnectUser> userWaitList;
    Vector<Room> roomList;

    public static void main(String[] args) {
        Server server = new Server();
        server.userAllList = new Vector<>();
        server.userWaitList = new Vector<>();
        server.roomList = new Vector<>();

        try {
            server.serverSocket = new ServerSocket(5005);
            System.out.println("[ Server ] Running...");

            while(true) {
                Socket socket = server.serverSocket.accept();
                ConnectUser connectUser = new ConnectUser(socket, server);
                connectUser.start();
            }
        } catch(SocketException e) {
            System.out.println("[ Server ] Server socket error >> " + e.toString());
        } catch(IOException e) {
            System.out.println("[ Server ] Input/Output error >> " + e.toString());
        }
    }
}

