package application.server300;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

//소켓으로 다중 데이터 전송 테스트
public class DemoServer {

    public static void main(String[] args) throws IOException {

        int PORT = 5005;
//        int number = Integer.parseInt(args[0]);
//        String str = new String(args[1]);

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Running server...");

        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("[Input User]");
            System.out.println("[User IP] " + socket.getInetAddress());

            OutputStream out = socket.getOutputStream();
            DataOutputStream dout = new DataOutputStream(out);

            dout.writeInt(12321);
            dout.writeUTF("받아라");

            dout.flush();
            dout.close();
            socket.close();
            }
        }
}
