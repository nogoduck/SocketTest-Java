package application.client300;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class DemoClient extends Application  {
    Socket socket;

    @FXML
    private TextField tfNickname;

    @FXML
    void onClickSubmit(ActionEvent e) throws IOException {
            clientStart();
            send(tfNickname.getText());
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ClientUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void send(String str) throws IOException {
        //Client -> Server
            OutputStream out = socket.getOutputStream();
            DataOutputStream dout = new DataOutputStream(out);

            dout.writeUTF(str);
            System.out.println("send >> " +  str);

            dout.close();
            out.close();
            socket.close();
    }

    public void receive()  {
        //Server -> Client
        while(true){
            try{
                InputStream in = socket.getInputStream();
                DataInputStream din = new DataInputStream(in);

                Boolean bool = din.readBoolean();
                System.out.println("response to Server >> " +  bool);

                din.close();
                in.close();
                socket.close();
            } catch(Exception e){
                break;
            }
        }

    }
    public void clientStart() throws IOException {
        socket = new Socket("localhost", 5005);
        receive(); //주석처리하면 DemoServer에서 처리과정 볼 수 있습니다.
    }
    public static void main(String[] args)  { launch(args); }
}
