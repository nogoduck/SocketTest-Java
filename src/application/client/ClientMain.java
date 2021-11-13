package application.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClientMain extends Application {

    //Client는 Server와 다르게 여러개의 스레드가 계속 생겨날 일이 없기 때문에
    //threadPool을 사용할 필요가 없습니다.
    //해당 클라이언트에는 서버로부터 메시지를 전달받는 메서드, 서버로 메시지를 전송하기 위한 메서드
    //총 2개의 스레드를 사용하고 있습니다.
    Socket socket;
    TextArea textArea;

    ArrayList<String> userList = new ArrayList<String>();

    //클라이언트 프로그램 동작 메서드
    public void startClient(String IP, int PORT){
        Thread thread = new Thread(() -> {
            try {
                socket = new Socket(IP, PORT);
                //서버로부터 메시지를 전달받기 위한 메서드
                receive();
            } catch(Exception e) {
                if(!socket.isClosed()){
                    stopClient();
                    System.out.println("[서버 접속 실패]");
                    //프로그램 종료
                    Platform.exit();
                }
            }
        });
        thread.start();
    }

    //클라이언트 프로그램 종료 메서드
    public void stopClient(){
        try {
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //서버로부터 메시지를 전달받는 메서드
    public void receive(){
        while(true){
            try {
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[512];
                int length = in.read(buffer);
                if(length == -1) throw new IOException();
                String message = new String(buffer, 0, length, "UTF-8");
                Platform.runLater(() -> {
                    textArea.appendText(message);
                });
            } catch(Exception e) {
                stopClient();
                break;
            }
        }
    }
    
    //서버로 메시지를 전송하는 메서드
    public void send(String message){
        Thread thread = new Thread(){
            public void run(){
                try {
                    OutputStream out = socket.getOutputStream();
                    //보내고자 하는 정보를 UTF-8로 인코딩해서 보내준다
                    //서버도 UTF-8로 받을 수 있게 되있음
                    byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
                    System.out.println("message >> " + message);
                    out.write(buffer);
                    //메시지 전송의 끝을 알림
                    out.flush();
                } catch(Exception e){
                    stopClient();
                }
            }};
        thread.start();
    }

    
    //클라이언트 프로그램을 동작시키는 메서드
    @Override
    public void start(Stage primaryStage) throws IOException {
        startClient("localhost", 5005);
        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
//        Parent root = FXMLLoader.load(MainController.class.getResource("MainUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("클라이언트");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> stopClient());
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
