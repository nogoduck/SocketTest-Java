package application._origin;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private MouseEvent me;
    public void setMouseEvent(MouseEvent e){
        me = e;
        node = (Node)me.getSource();
        stage = (Stage) node.getScene().getWindow();
    }

    Node node;
    Stage stage;
    Socket mySocket;
    OutputStream out = null;
    DataOutputStream dout = null;

    public static void main(String[] args) {
        Client client = new Client();
        try {
//            client.socket = new Socket("localhost", 5005);
            client.mySocket = new Socket("localhost", 5005);



            client.out = client.mySocket.getOutputStream();
            client.dout = new DataOutputStream(client.out);
            System.out.println("[ Client ] Running...");


            System.out.println("client.out >> " + client.out);
            System.out.println("client.dout >> " + client.dout);


            ClientManager clientManager = new ClientManager(client, client.mySocket);
            clientManager.start();

        } catch(SocketException e) {
            System.out.println("[ Client ] Client socket error >> " + e.toString());
        } catch(IOException e) {
            System.out.println("[ Client ] Input/Output error >> " + e.toString());
        }
    }


    public void send(String str) {
        try {
//            byte[] buffer = str.getBytes(StandardCharsets.UTF_8);
//            out.write(buffer);
//            dout.write(str.getBytes(StandardCharsets.UTF_8));

            dout.writeUTF(str);
            System.out.println("[ Send ] Succeed >> " + str);
        } catch (Exception e) {
            System.out.println("[ Send ] Failed >> " + e.toString());
        }
    }
}
