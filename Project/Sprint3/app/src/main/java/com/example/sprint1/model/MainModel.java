package com.example.sprint1.model;


import java.util.HashMap;

public class MainModel {

    /* Singleton Design */

    private static MainModel instance;
    private final UserDatabase userDatabase = new UserDatabase();
    private final DestDatabase destDatabase = new DestDatabase();

    /* Instance fields */

    private MainModel() {

    }

    public static synchronized MainModel getInstance() {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    /* Main Features */

    public void userSignUp(
            String username, String password,
            Callback<Boolean> callback
    ) {
        this.userDatabase.userSignUp(username, password, callback::onResult);
    }

    public void userSignIn(
            String username, String password,
            Callback<Boolean> callback
    ) {
        this.userDatabase.userSignIn(username, password, success -> {
            if (success) {
                this.destDatabase.setUsernameCurr(this.userDatabase.getUsernameCurr());
            }
            callback.onResult(success);
        });
    }

    public void addDestination(
            String travelLocation, String startDate, String endDate, String duration,
            Callback<Boolean> callback
    ) {
        this.destDatabase.addDestination(
                travelLocation, startDate, endDate, duration, callback::onResult
        );
    }

    public void getDestinations(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.destDatabase.getDestinations(callback::onResult);
    }

    public void setVacation(
            String startDate, String endDate, String duration,
            Callback<Boolean> callback
    ) {
        this.userDatabase.setVacation(startDate, endDate, duration, callback::onResult);
    }

    public void getVacation(
            Callback<HashMap<String, String>> callback
    ) {
        this.userDatabase.getVacation(callback::onResult);
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
