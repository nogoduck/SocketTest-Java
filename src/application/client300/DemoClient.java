package application.client300;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DemoClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5005);

        System.out.println("Connect server...");

        InputStream in = socket.getInputStream();
        DataInputStream din = new DataInputStream(in);

        int num = din.readInt();
        String str = din.readUTF();
        System.out.println("Server to Int >> " +  num);
        System.out.println("Server to String >> " +  str);

        din.close();
        in.close();
        socket.close();

    }
}
