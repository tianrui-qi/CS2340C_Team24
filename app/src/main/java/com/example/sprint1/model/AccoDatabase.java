package com.example.sprint1.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class AccoDatabase {

    /* Instance fields */

    private final DatabaseReference accoDatabase;

    /* Constructors */

    public AccoDatabase() {
        this.accoDatabase = FirebaseDatabase.getInstance().getReference("accommodation");
    }

    /* Main Features */

    public void addAccommodation(
            String checkIn, String checkOut, String location, String roomNum, String roomType,
            Callback<String> callback
    ) {
        // Generate a unique key for the accommodation
        String key = this.accoDatabase.push().getKey();

        if (key == null) {
            callback.onResult(null);
            return;
        }

        // Create a HashMap to store the accommodation details
        HashMap<String, String> value = new HashMap<>();
        value.put("checkIn", checkIn);
        value.put("checkOut", checkOut);
        value.put("location", location);
        value.put("roomNum", roomNum);
        value.put("roomType", roomType);

        // Save the accommodation data to Firebase
        this.accoDatabase.child(key).setValue(value).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(key); // Return the accommodation ID if successful
            } else {
                callback.onResult(null); // Return null if operation fails
            }
        });
    }

    public void getAccommodation(
            ArrayList<String> keys,
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        if (keys == null || keys.isEmpty()) {
            callback.onResult(null);
            return;
        }

        HashMap<String, HashMap<String, String>> result = new HashMap<>();
        int[] pendingRequests = {keys.size()}; // Counter to track pending Firebase requests
        boolean[] hasFailed = {false}; // To track if any request fails

        for (String accoId : keys) {
            DatabaseReference ref = this.accoDatabase.child(accoId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HashMap<String, String> accommodationDetails = new HashMap<>();
                        for (DataSnapshot detailSnapshot : dataSnapshot.getChildren()) {
                            String key = detailSnapshot.getKey();
                            String value = detailSnapshot.getValue(String.class);
                            if (key != null && value != null) {
                                accommodationDetails.put(key, value);
                            }
                        }
                        // Add the accommodation details to the result
                        result.put(accoId, accommodationDetails);
                    }
                    synchronized (pendingRequests) {
                        pendingRequests[0]--;
                        if (pendingRequests[0] == 0) {
                            // All requests completed
                            callback.onResult(hasFailed[0] ? null : result);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    synchronized (pendingRequests) {
                        pendingRequests[0]--;
                        hasFailed[0] = true; // Mark failure
                        if (pendingRequests[0] == 0) {
                            // All requests completed
                            callback.onResult(null);
                        }
                    }
                }
            });
        }
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}