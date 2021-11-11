package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;


public class Main extends Application {

	// 여러개의 스레드를 효율적으로 관리하기 위한 라이브러리
	// threadPool 사용하면 기본적인 스레드 숫자에 제한을 두게 되어 갑장스러운 트래픽에 대응할 수 있다.
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();

	ServerSocket serverSocket;

	// 서버를 구동시켜서 클라이언트의 연결을 기다리는 메소드
	public void startServer(String IP, int port){
		
	}

	// 서버의 작동을 중지하는 메소드
	public void stopServer(){

	}
	
	// UI를 생성하고, 프로그램을 동작시키는 메소드
	@Override
	public void start(Stage primaryStage){
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
