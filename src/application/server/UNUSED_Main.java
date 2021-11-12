package application.server;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UNUSED_Main extends Application {

	// 여러개의 스레드를 효율적으로 관리하기 위한 라이브러리
	// threadPool 사용하면 기본적인 스레드 숫자에 제한을 두게 되어 갑장스러운 트래픽에 대응할 수 있다.
	public static ExecutorService threadPool;
	// ArrayList를 사용하지 않고 Vector를 사용하는 이유:
	// 멀티쓰레드 환경에ㅔ서 Vector는 synchronized가 적용되어 있지만 list는 적용되어 있지 않습니다.
	// 즉 Vector는 한 번에 하나의 쓰레드만 접근 가능하며 ArrayList는 동시접근이 가능합니다.
	public static Vector<UNUSED_Client> UNUSEDClients = new Vector<UNUSED_Client>();
	public static ArrayList<String> userList = new ArrayList<String>();
	ServerSocket serverSocket;

	// 서버를 구동시켜서 클라이언트의 연결을 기다리는 메소드
	public void startServer(String IP, int PORT){
		try{
			serverSocket = new ServerSocket();
			//기본적으로 소켓통신은 소켓 객체를 활성화 해준 후
			//서버 컴퓨터 역할을 하는 자신의 컴퓨터가 자신의 IP주소, 자신의 포트 번호로
			//특정한 클라이언트의 접속을 기다릴 수 있다.
			serverSocket.bind(new InetSocketAddress(IP, PORT));
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
						UNUSEDClients.add(new UNUSED_Client(socket));
						System.out.println("socket >> " + socket);
						System.out.println("[클라이언트 접속] "
						+ socket.getRemoteSocketAddress()
						+": " + Thread.currentThread().getName());
						System.out.println("-------------");
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
		//현재 작동 중인 모든 소켓 종료
		try{
			//Iterator를 사용해서 모든 클라이언트에 개별적으로 접근할 수 있게 해준다.
			Iterator<UNUSED_Client> iterator = UNUSEDClients.iterator();
			//hasNext 하나씩 접근 가능하게 한다.
			while(iterator.hasNext()){
				UNUSED_Client UNUSEDClient = iterator.next();
				UNUSEDClient.socket.close();
				iterator.remove();
			}
			//서버 소켓 종료
			if(serverSocket != null && !serverSocket.isClosed()){
				serverSocket.close();
			}
			//스레드 풀 종료
			if(threadPool != null && !threadPool.isShutdown()){
				threadPool.shutdown();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
	// UI를 생성하고, 프로그램을 동작시키는 메소드
	@Override
	public void start(Stage primaryStage){
		BorderPane root = new BorderPane();
		//내부에 5만큼의 패딩 적용
		root.setPadding(new Insets(5));
		TextArea textArea = new TextArea();
		//textarea를 출력만하고 사용자가 수정하지 못하게 하는 속성
		textArea.setEditable(false);
		textArea.setFont(new Font("CookieRun", 15));
		//중간에 textArea를 담는다.
		root.setCenter(textArea);

		Button toggleButton = new Button("시작하기");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);

		final String IP = "127.0.0.1";
		final int PORT = 5005;

		toggleButton.setOnAction(e -> {
			if(toggleButton.getText().equals("시작하기")){
				startServer(IP, PORT);
				//JavaFX는 runLater 함수를 사용하여 GUI 요소를 변경해줘야한다.
				Platform.runLater(() -> {
					String message = String.format("[서버 시작]\n", IP, PORT);
					textArea.appendText(message);
					toggleButton.setText("종료하기");
				});
			} else {
				stopServer();
				Platform.runLater(() -> {
					String message = String.format("[서버 종료]\n", IP, PORT);
					textArea.appendText(message);
					toggleButton.setText("시작하기");
				});
			}
		});

		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[채팅 서버]");
		primaryStage.setOnCloseRequest(e -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
