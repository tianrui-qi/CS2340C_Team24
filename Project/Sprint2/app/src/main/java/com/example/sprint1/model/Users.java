package com.example.sprint1.model;
import java.util.HashMap;


public class Users {
    private final HashMap<String, User> users = new HashMap<>();

    public User getUser(String username) {
        return this.users.get(username);
    }

    public void addUser(String username, String password) {
        // note that username and password need to be check before call this function
        this.users.put(username, new User(username, password));
    }

    public boolean checkUser(String username, String password) {
        // note that username and password need to be check before call this function
        if (!this.users.containsKey(username)) {
            return false;
        }
        User user = this.users.get(username);
        if (user == null) {
            return false;
        }
        if (!user.getUsername().equals(username)) {
            // can be delete in the future
            return false;
        }
        return user.getPassword().equals(password);
    }
}
