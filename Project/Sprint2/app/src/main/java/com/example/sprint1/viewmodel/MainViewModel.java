package com.example.sprint1.viewmodel;
import androidx.lifecycle.ViewModel;
import com.example.sprint1.model.MainModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainViewModel extends ViewModel {

    private final MainModel mainModel = MainModel.getInstance();

    public void userSignUp(String username, String password, AuthCallback callback) {
        // check input format
        if (
                username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()
        ) {
            callback.onResult(false);
            return;
        }

        this.mainModel.userSignUp(username, password, callback::onResult);
    }

    public void userSignIn(String username, String password, AuthCallback callback) {
        // check input format
        if (
                username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()
        ) {
            callback.onResult(false);
            return;
        }

        this.mainModel.userSignIn(username, password, callback::onResult);
    }

    public void destLogTravel(
        String travelLocation, String startDate, String endDate, AuthCallback callback
    ) {
        // check input format
        if (
                travelLocation == null || travelLocation.trim().isEmpty()
                || startDate == null || !startDate.matches("^\\d{2}/\\d{2}/\\d{4}$")
                || endDate == null || !endDate.matches("^\\d{2}/\\d{2}/\\d{4}$")
        ) {
            callback.onResult(false);
            return;
        }

        String duration = null;
        try {
            // check if data valid
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            dateFormat.setLenient(false);
            Date pStartDate = dateFormat.parse(startDate);
            Date pEndDate = dateFormat.parse(endDate);
            if (pEndDate == null || pStartDate == null || pEndDate.before(pStartDate)) {
                callback.onResult(false);
                return;
            }
            // calculate duration
            duration = String.valueOf((int) TimeUnit.MILLISECONDS.toDays(
                    pEndDate.getTime() - pStartDate.getTime()
            ) + 1);
        } catch (ParseException e) {
            callback.onResult(false);
        }

        this.mainModel.destLogTravel(
                travelLocation, startDate, endDate, duration, callback::onResult
        );
    }

    public interface AuthCallback {
        void onResult(boolean success);
    }
}
