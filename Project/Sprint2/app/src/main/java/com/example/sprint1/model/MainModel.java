package com.example.sprint1.model;


public class MainModel {

    private static MainModel instance;
    private final UserDatabase userDatabase = new UserDatabase();
    private final DestDatabase destDatabase = new DestDatabase();

    private MainModel() {

    }

    public static synchronized MainModel getInstance() {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    public void userSignUp(
            String username, String password,
            AuthCallback callback
    ) {
        this.userDatabase.userSignUp(username, password, callback);
    }

    public void userSignIn(
            String username, String password,
            AuthCallback callback
    ) {
        this.userDatabase.userSignIn(username, password, success -> {
            if (success) {
                this.destDatabase.setUsernameCurr(this.userDatabase.getUsernameCurr());
            }
            callback.onResult(success);
        });
    }

    public void destLogTravel(
            String travelLocation, String startDate, String endDate, String duration,
            MainModel.AuthCallback callback
    ) {
        this.destDatabase.destLogTravel(travelLocation, startDate, endDate, duration, callback);
    }

    public interface AuthCallback {
        void onResult(boolean success);
    }
}
