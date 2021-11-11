package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {

	// 여러개의 스레드를 효율적으로 관리하기 위한 라이브러리
	// threadPool 사용하면 기본적인 스레드 숫자에 제한을 두게 되어 갑장스러운 트래픽에 대응할 수 있다.
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();

	ServerSocket serverSocket;

	// 서버를 구동시켜서 클라이언트의 연결을 기다리는 메소드
	public void startServer(String IP, int port){
		try{
			serverSocket = new ServerSocket();
			//기본적으로 소켓통신은 소켓 객체를 활성화 해준 후
			//서버 컴퓨터 역할을 하는 자신의 컴퓨터가 자신의 IP주소, 자신의 포트 번호로
			//특정한 클라이언트의 접속을 기다릴 수 있다.
			serverSocket.bind(new InetSocketAddress(IP, port));
		} catch (Exception e) {
			e.printStackTrace();
			//에러가 발생 후 서버가 닫혀있지 않다면 서버를 종료해 준다.
			if(!serverSocket.isClosed()){
				stopServer();
			}
			return;
		}

		//에러가 발생하지 않았다면 클라이언트가 접속할때까지 기다리는 쓰레드
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				//무한으로 반복함으로써 새로운 클라이언트가 접속할 수 있게 해준다.
				while(true){
					try {
						//클라이언트 접속 확인
						Socket socket = serverSocket.accept();
						//접속을 했다면 클라이언트 배열에 클라이언트 추가
						clients.add(new Client(socket));
						System.out.println("[클라이언트 접속] "
						+ socket.getRemoteSocketAddress()
						+": " + Thread.currentThread().getName());
					} catch (IOException e) {
						//서버소켓에 오류가 발생했다면 서버 종료
						if(!serverSocket.isClosed()){
							stopServer();
						}
						break;
					}
				}
			}
		};
		//threadPool 초기화
		threadPool = Executors.newCachedThreadPool();
		//threadPool에 현재 클라이언트를 기다리는 스레드를 담을 수 있게 한다.
		threadPool.submit(thread);
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
