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

    /* Main Features */

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

    public void addDining(
            String location, String website, String time,
            Callback<Boolean> callback
    ) {
        // Check input format
        if (
                location == null || location.trim().isEmpty()
                        || website == null || website.trim().isEmpty()
                        || time == null || !time.matches("^\\d{2}/\\d{2}/\\d{4}$")
        ) {
            callback.onResult(false);
            return;
        }

        try {
            // Check if time is valid
            DateFormat dateFormat = DateFormat.getDateInstance(
                    DateFormat.SHORT, java.util.Locale.US
            );
            dateFormat.setLenient(false);
            Date reservationDate = dateFormat.parse(time);
            if (reservationDate == null) {
                callback.onResult(false);
                return;
            }
        } catch (ParseException e) {
            callback.onResult(false);
            return;
        }

        // Check for duplicates using existing dining records
        this.mainModel.getDining(existingDining -> {
            if (existingDining != null) {
                for (HashMap<String, String> dining : existingDining.values()) {
                    if (location.equals(dining.get("location"))
                            && website.equals(dining.get("website"))
                            && time.equals(dining.get("time"))) {
                        // Duplicate found
                        callback.onResult(false);
                        return;
                    }
                }
            }

            // If no duplicates, proceed to add the dining record
            this.mainModel.addDining(location, website, time, callback::onResult);
        });
    }

    public void getDining(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.mainModel.getDining(callback::onResult);
    }

    public void addAccommodation(
            String location, String roomNum, String roomType, String checkIn, String checkOut,
            Callback<Boolean> callback
    ) {
        // Check for null or empty inputs
        if (
                checkIn == null || checkIn.trim().isEmpty()
                        || checkOut == null || checkOut.trim().isEmpty()
                        || location == null || location.trim().isEmpty()
                        || roomNum == null || roomNum.trim().isEmpty()
                        || roomType == null || roomType.trim().isEmpty()
        ) {
            callback.onResult(false);
            return;
        }

        try {
            // Check if check-in and check-out times are valid
            DateFormat dateFormat = DateFormat.getDateInstance(
                    DateFormat.SHORT, java.util.Locale.US
            );
            dateFormat.setLenient(false);
            Date checkInDate = dateFormat.parse(checkIn);
            Date checkOutDate = dateFormat.parse(checkOut);

            if (checkInDate == null || checkOutDate == null || !checkOutDate.after(checkInDate)) {
                callback.onResult(false); // Invalid date range
                return;
            }

            // Calculate the stay duration
            int durationInDays = MainViewModel.calDuration(checkInDate, checkOutDate);
            if (durationInDays < 1) {
                callback.onResult(false); // Stay duration must be at least 1 day
                return;
            }
        } catch (ParseException e) {
            callback.onResult(false); // Invalid date format
            return;
        }

        // Check for duplicates using existing accommodation records
        this.mainModel.getAccommodation(existingAccommodations -> {
            if (existingAccommodations != null) {
                for (HashMap<String, String> accommodation : existingAccommodations.values()) {
                    if (checkIn.equals(accommodation.get("checkIn"))
                            && checkOut.equals(accommodation.get("checkOut"))
                            && location.equals(accommodation.get("location"))
                            && roomNum.equals(accommodation.get("roomNum"))
                            && roomType.equals(accommodation.get("roomType"))) {
                        // Duplicate found
                        callback.onResult(false);
                        return;
                    }
                }
            }

            // If no duplicates, proceed to add the accommodation record
            this.mainModel.addAccommodation(
                    checkIn, checkOut, location, roomNum, roomType, callback::onResult
            );
        });
    }

    public void getAccommodation(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.mainModel.getAccommodation(callback::onResult);
    }

    /* Helper Methods */

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

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
