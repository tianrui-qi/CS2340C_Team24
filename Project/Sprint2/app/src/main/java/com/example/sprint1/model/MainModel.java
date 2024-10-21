package com.example.sprint1.model;


import java.util.HashMap;

public class MainModel {

    private static MainModel instance;
    private final UserDatabase userDatabase = new UserDatabase();
    private final DestDatabase destDatabase = new DestDatabase();

    /* Singleton Design */

    private MainModel() {

    }

    public static synchronized MainModel getInstance() {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    /* Main Features */

    public void userSignUp(String username, String password, CallbackBool callback) {
        this.userDatabase.userSignUp(username, password, callback);
    }

    public void userSignIn(String username, String password, CallbackBool callback) {
        this.userDatabase.userSignIn(username, password, success -> {
            if (success) {
                this.destDatabase.setUsernameCurr(this.userDatabase.getUsernameCurr());
            }
            callback.onResult(success);
        });
    }

    public void addDestination(
            String travelLocation, String startDate, String endDate, String duration,
            CallbackBool callback
    ) {
        this.destDatabase.addDestination(travelLocation, startDate, endDate, duration, callback);
    }

    public void getDestinations(CallbackDestination callback) {
        this.destDatabase.getDestinations(callback);
    }

    public void setVacation(
            String startDate, String endDate, String duration, CallbackBool callback) {
        this.userDatabase.setVacation(startDate, endDate, duration, callback);
    }

    public void getVacation(CallbackVacation callback) {
        this.userDatabase.getVacation(callback);
    }

    /* Callbacks */

    public interface CallbackBool {
        void onResult(boolean callback);
    }

    public interface CallbackDestination {
        void onResult(HashMap<String, HashMap<String, String>> callback);
    }

    public interface CallbackVacation {
        void onResult(HashMap<String, String> callback);
    }
}
