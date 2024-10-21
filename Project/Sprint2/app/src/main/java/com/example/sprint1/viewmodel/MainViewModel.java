package com.example.sprint1.viewmodel;
import androidx.lifecycle.ViewModel;
import com.example.sprint1.model.MainModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;


public class MainViewModel extends ViewModel {

    private final MainModel mainModel = MainModel.getInstance();

    /* Main Features */

    public void userSignUp(String username, String password, BoolCallback callback) {
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

    public void userSignIn(String username, String password, BoolCallback callback) {
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

    public void addDestination(
        String travelLocation, String startDate, String endDate, BoolCallback callback
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
            duration = String.valueOf(this.calDuration(pStartDate, pEndDate));
        } catch (ParseException e) {
            callback.onResult(false);
        }

        this.mainModel.addDestination(
                travelLocation, startDate, endDate, duration, callback::onResult
        );
    }

    public void getDestinations(DestCallback callback) {
        this.mainModel.getDestinations(callback::onResult);
    }

    public void setVacation(
            String startDate, String endDate, String duration, BoolCallback callback
    ) {
        // check input format
        if (!startDate.isEmpty() && !startDate.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            callback.onResult(false);
            return;
        }
        if (!endDate.isEmpty() && !endDate.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            callback.onResult(false);
            return;
        }

        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            dateFormat.setLenient(false);

            Date pStartDate = (startDate.isEmpty()) ? null : dateFormat.parse(startDate);
            Date pEndDate = (endDate.isEmpty()) ? null : dateFormat.parse(endDate);
            Integer pDuration = (duration.isEmpty()) ? null : Integer.parseInt(duration);

            if (pStartDate != null && pEndDate != null && pDuration != null) {
                // case 1: all three values are present
                if (calDuration(pStartDate, pEndDate) == pDuration) {
                    this.mainModel.setVacation(startDate, endDate, duration);
                    callback.onResult(true);
                } else {
                    callback.onResult(false);
                }
            } else if (pStartDate != null && pEndDate != null) {
                // case 4: missing duration, calculate it
                pDuration = calDuration(pStartDate, pEndDate);
                if (pDuration > 0) {
                    this.mainModel.setVacation(startDate, endDate, String.valueOf(pDuration));
                    callback.onResult(true);
                } else {
                    callback.onResult(false);
                }
            } else if (pStartDate != null && pDuration != null) {
                // case 3: missing endDate, calculate it
                pEndDate = new Date(pStartDate.getTime() + TimeUnit.DAYS.toMillis(pDuration - 1));
                this.mainModel.setVacation(startDate, dateFormat.format(pEndDate), duration);
                callback.onResult(true);
            } else if (pEndDate != null && pDuration != null) {
                // case 2: missing startDate, calculate it
                pStartDate = new Date(pEndDate.getTime() - TimeUnit.DAYS.toMillis(pDuration - 1));
                this.mainModel.setVacation(dateFormat.format(pStartDate), endDate, duration);
                callback.onResult(true);
            } else {
                // case 5: all null
                callback.onResult(false);
            }
        } catch (ParseException | NumberFormatException e) {
            callback.onResult(false);
        }
    }

    /* Help Function */

    private int calDuration(Date startDate, Date endDate) {
        return (int) TimeUnit.MILLISECONDS.toDays(
                endDate.getTime() - startDate.getTime()
        ) + 1;
    }

    /* Callbacks */

    public interface BoolCallback {
        void onResult(boolean success);
    }

    public interface DestCallback {
        void onResult(HashMap<String, HashMap<String, String>> dest);
    }
}
