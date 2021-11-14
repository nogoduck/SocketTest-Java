package application.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainController extends ClientMain{

    ClientMain clientMain = new ClientMain();

    @FXML
    private Button nicknameButton;

    @FXML
    private TextField tfNickname;

    @FXML
    void onClickNicknameCheck(ActionEvent e) {
        String nickname = tfNickname.getText();
        System.out.println("nickname >> " + nickname);
        clientMain.send("#" + nickname);
        clientMain.send("GoodJob");

        System.out.println(clientMain.userList);
        clientMain.userList.add(nickname);
        System.out.println(clientMain.userList);

    }
}
