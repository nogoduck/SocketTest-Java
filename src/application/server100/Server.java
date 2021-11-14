package application.server100;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;
    private Manager manager = new Manager();

    public void start(){
        try{
            server = new ServerSocket(5005);
            System.out.println("Run server...");
            while(true){
                Socket socket = server.accept();
//                new Chat(socket).start;
                manager.sendClientInformation();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

