package com.example.sprint1.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainModel {

    /* Instance fields */

    private final UserDatabase userDatabase = new UserDatabase();
    private final DestDatabase destDatabase = new DestDatabase();
    private final DiniDatabase diniDatabase = new DiniDatabase();
    private final AccoDatabase accoDatabase = new AccoDatabase();
    private final TravDatabase travDatabase = new TravDatabase();

    /* Singleton Design */

    private static MainModel instance;

    private MainModel() {

    }

    public static synchronized MainModel getInstance() {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    /* Observer Methods */

    private final ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String eventType, Object data) {
        for (Observer observer : observers) {
            observer.onUpdate(eventType, data);
        }
    }

    /* Main Functions */

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

    public void addDining(
            String location, String website, String time,
            Callback<Boolean> callback
    ) {
        this.diniDatabase.addDining(location, website, time, key -> {
            if (key != null) {
                this.userDatabase.addDining(key, success -> {
                    if (success) {
                        notifyObservers("DiningAdded", key); // Notify observers
                    }
                    callback.onResult(success);
                });
            } else {
                callback.onResult(false);
            }
        });
    }

    public void getDining(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.userDatabase.getDining(keys -> {
            if (keys == null || keys.isEmpty()) {
                callback.onResult(null);
            } else {
                this.diniDatabase.getDining(keys, data -> {
                    notifyObservers("DiningFetched", data); // Notify observers
                    callback.onResult(data);
                });
            }
        });
    }

    public void addAccommodation(
            String checkIn, String checkOut, String location, String roomNum, String roomType,
            Callback<Boolean> callback
    ) {
        this.accoDatabase.addAccommodation(
                checkIn, checkOut, location, roomNum, roomType, key -> {
                    if (key != null) {
                        this.userDatabase.addAccommodation(key, success -> {
                            if (success) {
                                notifyObservers("AccommodationAdded", key); // Notify observers
                            }
                            callback.onResult(success);
                        });
                    } else {
                        callback.onResult(false);
                    }
                }
        );
    }

    public void getAccommodation(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.userDatabase.getAccommodation(keys -> {
            if (keys == null || keys.isEmpty()) {
                callback.onResult(null);
            } else {
                this.accoDatabase.getAccommodation(keys, data -> {
                    notifyObservers("AccommodationFetched", data); // Notify observers
                    callback.onResult(data);
                });
            }
        });
    }

    public void addTravel(
            String start, String end,
            String destination, String accommodation, String dining, String note,
            Callback<Boolean> callback
    ) {
        // Retrieve the current username
        String username = this.userDatabase.getUsernameCurr();
        if (username == null || username.isEmpty()) {
            callback.onResult(false); // Fail if the current username is not set
            return;
        }

        Map<String, String> travelData = new HashMap<>();
        travelData.put("username", username);
        travelData.put("start", start);
        travelData.put("end", end);
        travelData.put("destination", destination);
        travelData.put("accommodation", accommodation);
        travelData.put("dining", dining);
        travelData.put("note", note);

        // Add the travel post to the TravelDatabase
        this.travDatabase.addTravel(
                travelData, key -> {
                    if (key != null) {
                        // Add the travel key to the current user's travel list
                        this.userDatabase.addTravel(key, success -> {
                            if (success) {
                                notifyObservers("TravelAdded", key);
                            }
                            callback.onResult(success);
                        });
                    } else {
                        callback.onResult(false); // Return failure if the key is null
                    }
                }
        );
    }

    public void getTravel(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        // Fetch all travel posts from the TravelDatabase
        this.travDatabase.getTravel(data -> {
            if (data != null) {
                notifyObservers("TravelFetched", data);
            }
            callback.onResult(data); // Return the fetched data
        });
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
