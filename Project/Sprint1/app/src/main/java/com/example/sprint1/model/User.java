package com.example.sprint1.model;
import java.util.HashMap;

public class User {
    String username;
    String password;
    HashMap<String,String> map = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean checkUser(String username, String password) {
        if(!map.containsKey(username)) {
            return false;
        }
        if (map.get(username) != password) {
            return false;
        }
        return true;
    }

    public boolean createUsers(String username, String password) {
        if(username == null || password  == null || username.trim().isEmpty() || password.trim().isEmpty()) return false;
        if (!map.containsKey(username)){
            User newUser = new User(username, password);
            map.put(newUser.username, newUser.password);
            return true;
        }
        return false;
    }
}
