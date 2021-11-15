package application.OopExample;

import java.util.ArrayList;

public class User {

    static ArrayList userList = new ArrayList();

    void add(User user){
        userList.add(user);
    }

    int size(){
        return userList.size();
    }




}
