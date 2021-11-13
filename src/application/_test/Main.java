package application._test;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        List<UserInfo>  userInfoList = new ArrayList<>();

        System.out.println("userInfoList = " + userInfoList);
        userInfo.setId(1004);

        userInfoList.add(userInfo);

        System.out.println("userInfoList = " + userInfoList);

//        userinfo
//        userInfoList.add(userInfo.set);




    }
}
