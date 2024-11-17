package com.example.sprint1.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DiniDatabase {

    /* Instance fields */

    private final DatabaseReference diniDatabase;

    /* Constructors */

    public DiniDatabase() {
        this.diniDatabase = FirebaseDatabase.getInstance().getReference("dining");
    }

    /* Main Features */

    public void addDining(
            String location, String website, String time,
            Callback<String> callback
    ) {
        // Generate a unique key for the dining reservation
        String key = this.diniDatabase.push().getKey();

        if (key == null) {
            callback.onResult(null);
            return;
        }

        // Create a HashMap to store the dining details
        HashMap<String, String> value = new HashMap<>();
        value.put("location", location);
        value.put("website", website);
        value.put("time", time);

        // Save the dining data to Firebase
        this.diniDatabase.child(key).setValue(value).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(key); // Return the dining ID if successful
            } else {
                callback.onResult(null); // Return null if operation fails
            }
        });
    }

    public void getDining(
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

        for (String diningId : keys) {
            DatabaseReference ref = this.diniDatabase.child(diningId);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HashMap<String, String> diningDetails = new HashMap<>();
                        for (DataSnapshot detailSnapshot : dataSnapshot.getChildren()) {
                            String key = detailSnapshot.getKey();
                            String value = detailSnapshot.getValue(String.class);
                            if (key != null && value != null) {
                                diningDetails.put(key, value);
                            }
                        }
                        // Add the dining details to the result
                        result.put(diningId, diningDetails);
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