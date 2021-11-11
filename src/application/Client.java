package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client {
    Socket socket;

    public Client(Socket socket){
        this.socket = socket;
        receive();
    }

    //클라이언트로부터 메시지를 전달 받는 메소드
    public void receive(){

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try{
                    //클라이언트로부터 내용을 반복적으로 받기 위함
                    while(true){
                        InputStream in = socket.getInputStream();
                        //한번에 512바이트씩 받는다.
                        byte[] buffer = new byte[512];
                        int length = in.read(buffer);
                        //메세지를 읽다가 오류 처리
                        while(length == -1) throw new IOException();
                        System.out.println("[메시지 수신 성공] " + socket.getRemoteSocketAddress() + ": "
                        + Thread.currentThread().getName());
                        //UTF-8로 한글도 처리가능하게 세팅
                        String message =  new String(buffer, 0, length, "UTF-8");

                        //1:n의 통신을 위해 다른 클라이언트에도 정보를 전송해 주는 반복문
                        for(Client client: Main.clients){
                            client.send(message);
                        }

                    }
                } catch(Exception e){
                        //예외를 구체적으로 처리하기 위해 중첩해서 사용
                    try{
                        System.out.println("[메세지 수신 오류] "
                                + socket.getRemoteSocketAddress()
                                + ": " + Thread.currentThread().getName());
                    } catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
            }
        };

        //threadPool에 스레드를 등록해서 안정적으로 관리하게 해준다.
        Main.threadPool.submit(thread);
    }

    //클라이언트로부터 메시지를 전송하는 메소드
    public void send(String message){

    }
}
