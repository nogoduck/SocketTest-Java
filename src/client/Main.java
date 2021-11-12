package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    //Client는 Server와 다르게 여러개의 스레드가 계속 생겨날 일이 없기 때문에
    //threadPool을 사용할 필요가 없습니다.
    //해당 클라이언트에는 서버로부터 메시지를 전달받는 메서드, 서버로 메시지를 전송하기 위한 메서드
    //총 2개의 스레드를 사용하고 있습니다.
    Socket socket;
    TextArea textArea;

    //클라이언트 프로그램 동작 메서드
    public void startClient(String IP, int PORT){
        Thread thread = new Thread(){
            public void run(){
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
            }};
    }

    //클라이언트 프로그램 종료 메서드
    public void stopClient(){
        try {
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        } catch(IOException e){
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
    
    //서버로 메시지를 전송하느 메서드
    public void send(String message){
        Thread thread = new Thread(){
            public void run(){
                try {
                    OutputStream out = socket.getOutputStream();
                    //보내고자 하는 정보를 UTF-8로 인코딩해서 보내준다
                    //서버도 UTF-8로 받을 수 있게 되있음
                    byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
                    out.write(buffer);
                    //메시지 전송의 끝을 알림
                    out.flush();
                } catch(Exception e){
                    stopClient();
                }
            }
        };
    }

    
    //클라이언트 플그램을 동작시키는 메서드
    @Override
    public void start(Stage primaryStage) {

    }


    public static void main(String[] args) { launch(args); }
}
