package com.example.sprint1.model;

import java.util.ArrayList;
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

    /* Feature 1: User Account */

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
        this.userDatabase.userSignIn(username, password, callback::onResult);
    }

    /* Feature 2: Log Travel */

    public void addDestination(
            String travelLocation, String startDate, String endDate, String duration,
            Callback<Boolean> callback
    ) {
        this.destDatabase.addDestination(travelLocation, startDate, endDate, duration, key -> {
            if (key != null) {
                this.userDatabase.addDestination(key, callback::onResult);
            } else {
                callback.onResult(false);
            }
        });
    }

    public void getDestination(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.userDatabase.getDestination(keys -> {
            if (keys == null || keys.isEmpty()) {
                callback.onResult(null);
            } else {
                this.destDatabase.getDestination(keys, callback::onResult);
            }
        });
    }

    /* Feature 3: Calculate Vacation Time */

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

    /* Feature 4: Collaboration */

    public void addCollaborator(
            String collaborator,
            Callback<Boolean> callback
    ) {
        this.userDatabase.addCollaborator(collaborator, callback::onResult);
    }

    public void getNonCollaborator(
            Callback<ArrayList<String>> callback
    ) {
        this.userDatabase.getNonCollaborator(callback::onResult);
    }

    public void addNote(
            String note,
            Callback<Boolean> callback
    ) {
        this.userDatabase.addNote(note, callback::onResult);
    }

    public void getNote(
            Callback<HashMap<String, String>> callback
    ) {
        this.userDatabase.getNote(callback::onResult);
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
