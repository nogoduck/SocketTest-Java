package application.server300;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;


public class DemoServer {
    private static ArrayList userList = new ArrayList();
    public static ArrayList clients = new ArrayList();

    static ServerSocket serverSocket;
    static Socket socket;
    static boolean isUser;

    //Client -> Server
    public static void receive(){
        Runnable thread = () -> {
            while(true){
                try{
                    InputStream in = socket.getInputStream();
                    DataInputStream din = new DataInputStream(in);

                    String nickname = din.readUTF();
                    System.out.println("nickname = " + nickname);

                    isUser = false;
                    if(userList.contains(nickname)){
                        isUser = true;
                    } else {
                        userList.add(nickname);
                    }

                    System.out.println("[Input User]");
                    System.out.println("[User IP] " + socket.getInetAddress());
                    System.out.println("[UserList] " + userList);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void send(){
        Runnable thread = () -> {
            while(true) {
                try {
                    OutputStream out = socket.getOutputStream();
                    DataOutputStream dout = new DataOutputStream(out);
                    System.out.println("isUser >> " + isUser);
                    dout.writeBoolean(isUser);


                    System.out.println();
                    dout.flush();
                    dout.close();
                    socket.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void startServer() throws IOException {
        serverSocket = new ServerSocket(5005);
        System.out.println("Running server...");

        while(true){
            socket = serverSocket.accept();
            System.out.println("[Enter client]");
            clients.add(socket);
        }
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}
