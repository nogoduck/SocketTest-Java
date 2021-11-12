package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
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
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding((new Insets(5)));
        //borderPane 위에 넣을 레이아웃
        HBox hbox = new HBox();
        hbox.setSpacing(5);

        TextField userName = new TextField();
        userName.setPrefWidth(150);
        userName.setPromptText("닉네임을 입력하세요.");
        HBox.setHgrow(userName, Priority.ALWAYS);

        TextField IPText = new TextField("127.0.0.1");
        TextField PORTTEXT = new TextField("5005");

        //윗쪽 레이아웃
        hbox.getChildren().addAll(userName, IPText, PORTTEXT);
        root.setTop(hbox);

        //중간
        textArea = new TextArea();
        textArea.setEditable(false);
        root.setCenter(textArea);

        //하단
        TextField input = new TextField();
        input.setPrefWidth(Double.MAX_VALUE);
        input.setDisable(true);

        input.setOnAction(e -> {
            send(userName.getText() + ": " + input.getText() + "\n");
            input.setText("");
            input.requestFocus();
        });

        Button sendButton = new Button("보내기");
        sendButton.setDisable(true);

        sendButton.setOnAction(e -> {
            send(userName.getText() + ": " + input.getText() + "\n");
            input.setText("");
            input.requestFocus();
        });

        Button connectionButton = new Button("접속하기");
        connectionButton.setOnAction(e -> {
            if(connectionButton.getText().equals("접속하기")){
                //기본 포트는 아래와 같이 지정되어 있으며
                //사용자의 입력이 있을 때 포트 번호가 변경된다.
                int PORT = 5005;
                try {
                    PORT = Integer.parseInt(PORTTEXT.getText());
                } catch(Exception e2) {
                    e2.printStackTrace();
                }
                startClient(IPText.getText(), PORT);
                Platform.runLater(() -> {
                    textArea.appendText("[채팅방 접속]\n");
                });
                connectionButton.setText("종료하기");
                input.setDisable(false);
                sendButton.setDisable(false);
                input.requestFocus();
            } else {
                stopClient();
                Platform.runLater(() -> {
                    textArea.appendText("[채팅방 퇴장]\n");
                });
                connectionButton.setText("접속하기");
                input.setDisable(true);
                sendButton.setDisable(true);
            }
        });

        BorderPane pane = new BorderPane();
        pane.setLeft(connectionButton);
        pane.setCenter(input);
        pane.setRight(sendButton);

        root.setBottom(pane);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("클라이언트");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> stopClient());
        primaryStage.show();

        connectionButton.requestFocus();
    }
    public static void main(String[] args) { launch(args); }
}
