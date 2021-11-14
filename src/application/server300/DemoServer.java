package application.server300;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//소켓으로 다중 데이터 전송 테스트
public class DemoServer {
    private static ArrayList userList = new ArrayList();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5005);
        System.out.println("Running server...");


        while(true){
            Socket socket = serverSocket.accept();

            //Client -> Server
            InputStream in = socket.getInputStream();
            DataInputStream din = new DataInputStream(in);

            String nickname = din.readUTF();
            System.out.println("nickname = " + nickname);

            boolean isUser = false;
            if(userList.contains(nickname)){
                isUser = true;
            } else {
                userList.add(nickname);

            }

            System.out.println("[Input User]");
            System.out.println("[User IP] " + socket.getInetAddress());
            System.out.println("[UserList] " + userList);

            //Server -> Client
            OutputStream out = socket.getOutputStream();
            DataOutputStream dout = new DataOutputStream(out);
            System.out.println("isUser >> " + isUser);
            dout.writeBoolean(isUser);

            System.out.println();
            dout.flush();
            dout.close();
            socket.close();
            }
        }
}
