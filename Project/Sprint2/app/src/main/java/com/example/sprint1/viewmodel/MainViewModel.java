package com.example.sprint1.viewmodel;
import androidx.lifecycle.ViewModel;
import com.example.sprint1.model.MainModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.*;


public class MainViewModel extends ViewModel {

    private final MainModel mainModel = MainModel.getInstance();

    /* Main Features */

    public void userSignUp(String username, String password, CallbackBool callback) {
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

    public void userSignIn(String username, String password, CallbackBool callback) {
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
            String travelLocation, String startDate, String endDate, CallbackBool callback
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
            DateFormat dateFormat = DateFormat.getDateInstance(
                    DateFormat.SHORT, java.util.Locale.US
            );
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

    public void getDestinations(CallbackDestination callback) {
        this.mainModel.getDestinations(callback::onResult);
    }

    public void setVacation(
            String startDate, String endDate, String duration, CallbackBool callback
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
            DateFormat dateFormat = DateFormat.getDateInstance(
                    DateFormat.SHORT, java.util.Locale.US
            );
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

    public void calVacation(CallbackString callback) {
        DateFormat dateFormat = DateFormat.getDateInstance(
                DateFormat.SHORT, java.util.Locale.US
        );
        dateFormat.setLenient(false);

        Set<String> uniqueOccupiedDays = new HashSet<>();

        mainModel.getVacation(vacationData -> {
            mainModel.getDestinations(destinations -> {
                if (destinations == null || destinations.isEmpty()) {
                    callback.onResult("0"); // 没有目的地数据，返回 0
                    return;
                }
                for (HashMap<String, String> destination : destinations.values()) {
                    try {
                        addOccupiedDays(
                                uniqueOccupiedDays, dateFormat,
                                dateFormat.parse(vacationData.get("startDate")),
                                dateFormat.parse(vacationData.get("endDate")),
                                dateFormat.parse(destination.get("startDate")),
                                dateFormat.parse(destination.get("endDate"))
                        );
                    } catch (ParseException e) {
                        e.printStackTrace();
                        callback.onResult(null);
                        return;
                    }
                }
                callback.onResult(String.valueOf(uniqueOccupiedDays.size()));
            });
        });
    }

    /* Help Function */

    private int calDuration(Date startDate, Date endDate) {
        return (int) TimeUnit.MILLISECONDS.toDays(
                endDate.getTime() - startDate.getTime()
        ) + 1;
    }

    private void addOccupiedDays(
            Set<String> uniqueOccupiedDays, DateFormat dateFormat,
            Date vacationStart, Date vacationEnd, Date destStart, Date destEnd
    ) {
        Date latestStart = vacationStart.after(destStart) ? vacationStart : destStart;
        Date earliestEnd = vacationEnd.before(destEnd) ? vacationEnd : destEnd;
        if (!latestStart.after(earliestEnd)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(latestStart);
            while (!calendar.getTime().after(earliestEnd)) {
                String dateStr = dateFormat.format(calendar.getTime());
                uniqueOccupiedDays.add(dateStr);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }

    /* Callbacks */

    public interface CallbackBool {
        void onResult(boolean callback);
    }

    public interface CallbackString {
        void onResult(String callback);
    }

    public interface CallbackDestination {
        void onResult(HashMap<String, HashMap<String, String>> callback);
    }
}
