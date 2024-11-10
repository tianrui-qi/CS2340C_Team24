package com.example.sprint1.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.MainModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class MainViewModel extends ViewModel {

    /* Instance fields */

    private final MainModel mainModel = MainModel.getInstance();

    /* Feature 1: User Account */

    private static int calDuration(Date startDate, Date endDate) {
        return (int) TimeUnit.MILLISECONDS.toDays(
                endDate.getTime() - startDate.getTime()
        ) + 1;
    }

    private static void addOccupiedDays(
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

    /* Feature 2: Log Travel */

    public void userSignUp(
            String username, String password,
            Callback<Boolean> callback
    ) {
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

    public void userSignIn(
            String username, String password,
            Callback<Boolean> callback
    ) {
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

    /* Feature 3: Calculate Vacation Time */

    public void addDestination(
            String travelLocation, String startDate, String endDate,
            Callback<Boolean> callback
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

        String duration;
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
            duration = String.valueOf(MainViewModel.calDuration(pStartDate, pEndDate));
        } catch (ParseException e) {
            callback.onResult(false);
            return;
        }

        this.mainModel.addDestination(
                travelLocation, startDate, endDate, duration, callback::onResult
        );
    }

    public void getDestination(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.mainModel.getDestination(callback::onResult);
    }

    public void setVacation(
            String startDate, String endDate, String duration,
            Callback<Boolean> callback
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
                if (MainViewModel.calDuration(pStartDate, pEndDate) == pDuration) {
                    this.mainModel.setVacation(startDate, endDate, duration, callback::onResult);
                } else {
                    callback.onResult(false);
                }
            } else if (pStartDate != null && pEndDate != null) {
                // case 4: missing duration, calculate it
                pDuration = MainViewModel.calDuration(pStartDate, pEndDate);
                if (pDuration > 0) {
                    this.mainModel.setVacation(
                            startDate, endDate, String.valueOf(pDuration), callback::onResult
                    );
                } else {
                    callback.onResult(false);
                }
            } else if (pStartDate != null && pDuration != null) {
                // case 3: missing endDate, calculate it
                pEndDate = new Date(pStartDate.getTime() + TimeUnit.DAYS.toMillis(pDuration - 1));
                this.mainModel.setVacation(
                        startDate, dateFormat.format(pEndDate), duration, callback::onResult
                );
            } else if (pEndDate != null && pDuration != null) {
                // case 2: missing startDate, calculate it
                pStartDate = new Date(pEndDate.getTime() - TimeUnit.DAYS.toMillis(pDuration - 1));
                this.mainModel.setVacation(
                        dateFormat.format(pStartDate), endDate, duration, callback::onResult
                );
            } else {
                // case 5: all null
                callback.onResult(false);
            }
        } catch (ParseException | NumberFormatException e) {
            callback.onResult(false);
        }
    }

    /* Feature 4: Collaboration */

    public void getVacation(
            Callback<HashMap<String, String>> callback
    ) {
        this.mainModel.getVacation(callback::onResult);
    }

    public void calVacation(
            Callback<String> callback
    ) {
        DateFormat dateFormat = DateFormat.getDateInstance(
                DateFormat.SHORT, java.util.Locale.US
        );
        dateFormat.setLenient(false);

        Set<String> uniqueOccupiedDays = new HashSet<>();

        this.mainModel.getVacation(vacationData -> this.mainModel.getDestination(destinations -> {
            if (destinations == null || destinations.isEmpty()) {
                callback.onResult("0"); // 没有目的地数据，返回 0
                return;
            }
            for (HashMap<String, String> destination : destinations.values()) {
                try {
                    String vacationStart = vacationData.get("startDate");
                    String vacationEnd = vacationData.get("endDate");
                    String destStart = destination.get("startDate");
                    String destEnd = destination.get("endDate");
                    if (vacationStart == null || vacationEnd == null
                            || destStart == null || destEnd == null) {
                        callback.onResult(null);
                        return;
                    }

                    Date pVacationStart = dateFormat.parse(vacationStart);
                    Date pVacationEnd = dateFormat.parse(vacationEnd);
                    if (pVacationStart == null || pVacationEnd == null) {
                        callback.onResult(null);
                        return;
                    }

                    MainViewModel.addOccupiedDays(
                            uniqueOccupiedDays, dateFormat,
                            pVacationStart, pVacationEnd,
                            dateFormat.parse(destStart), dateFormat.parse(destEnd)
                    );
                } catch (ParseException e) {
                    callback.onResult(null);
                    return;
                }
            }
            callback.onResult(String.valueOf(uniqueOccupiedDays.size()));
        }));
    }

    public void addCollaborator(
            String collaborator,
            Callback<Boolean> callback
    ) {
        this.mainModel.addCollaborator(collaborator, callback::onResult);
    }

    public void getNonCollaborator(
            Callback<ArrayList<String>> callback
    ) {
        this.mainModel.getNonCollaborator(callback::onResult);
    }

    /* Helper Methods */

    public void addNote(
            String note,
            Callback<Boolean> callback
    ) {
        this.mainModel.addNote(note, callback::onResult);
    }

    public void getNote(
            Callback<HashMap<String, String>> callback
    ) {
        this.mainModel.getNote(callback::onResult);
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
