package application._origin;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ClientManager extends Thread{
    Socket socket;
    Client client;

    InputStream in;
    DataInputStream din;

    String message;	//수신 메시지 저장

    /* 각 메시지를 구분하기 위한 태그 */
    final String checkNicknameTag = "checkNickname";
    final String createRoomTag = "createRoom";
    final String enterRoomTag = "enterRoom";
    final String leaveRoomTag = "leaveRoom";

    ClientManager(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    public void run() {
        try {
            in = this.socket.getInputStream();
            din = new DataInputStream(in);

            while(true) {
                message = din.readUTF();
                System.out.println("[ Receive ] Succeed >> " + message);




                if (message.contains("@@payload:")) {
                    String payload[] = message.replace("@@payload:##", "").split("##");
                    printPayload(payload);

                    switch (payload[0]) {
                        case checkNicknameTag:
                            System.out.println("[ Server ] 닉네임 중복 체크");
                            //payload[] >> [0]checkNickname, [1]nickname
                            checkNickname(payload);
                            break;
                        case createRoomTag:
                            System.out.println("[ Server ] 방 생성");
                            //payload[] >> [0]createRoom, [1]roomTitle
//                            createRoom(payload);
                            break;
                        case enterRoomTag:
                            System.out.println("[ Server ] 방 입장");
                            //payload[] >> [0]enterRoom, [1]roomTitle
//                            enterRoom(payload);
                            break;
                        case leaveRoomTag:
                            System.out.println("[ Server ] 방 퇴장");
                            //payload[] >> [0]leaveRoom, [1]roomTitle
//                            leaveRoom(payload);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[ Receive ] Failed >> " + e.toString());
        }
    }

    void checkNickname(String payload[]){
        System.out.println("닉네임 확인 ");
//        Platform.runLater(() -> {
//            Stage stage = new Stage();
//            FXMLLoader loader = new FXMLLoader(GameLobbyController.class.getResource("GameLobbyUI.fxml"));
//            Parent root = null;
//            try {
//                root = (Parent)loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            GameLobbyController controller = loader.<GameLobbyController>getController();
//            controller.setResMsg(payload);
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        });
    }

    void printPayload(String str[]) {
        System.out.printf("[ Receive ] Payload[] >> ");
        for (String s : str) {
            System.out.printf("%s ", s);
        }
        System.out.println();
    }


}