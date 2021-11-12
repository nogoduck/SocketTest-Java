package application._test;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String>  user = new ArrayList<String>();

        System.out.println("user = " + user);

        user.add("1Sens");
        user.add("2Sens");
        System.out.println("user = " + user);
        user.add("3Sens");
        System.out.println("user = " + user);

        user.remove("2Sens");
        System.out.println("user = " + user);
        user.clear();
        System.out.println("user = " + user);
    }
}
