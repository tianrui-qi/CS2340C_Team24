package com.example.sprint1.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class DestDatabase {

    /* Instance fields */

    private final DatabaseReference destDatabase;

    /* Constructors */

    public DestDatabase() {
        this.destDatabase = FirebaseDatabase.getInstance().getReference("destination");
    }

    /* Feature 2: Log Travel */

    public void addDestination(
            String travelLocation, String startDate, String endDate, String duration,
            Callback<String> callback
    ) {
        String key = this.destDatabase.push().getKey();

        if (key == null) {
            callback.onResult(null);
            return;
        }

        HashMap<String, String> value = new HashMap<>();
        value.put("travelLocation", travelLocation);
        value.put("startDate", startDate);
        value.put("endDate", endDate);
        value.put("duration", duration);

        this.destDatabase.child(key).setValue(value).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(key); // Return the trip ID if successful
            } else {
                callback.onResult(null); // Return null if operation fails
            }
        });
    }

    public void getDestination(
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

        for (String destId : keys) {
            DatabaseReference ref = this.destDatabase.child(destId);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HashMap<String, String> destinationDetails = new HashMap<>();
                        for (DataSnapshot detailSnapshot : dataSnapshot.getChildren()) {
                            String key = detailSnapshot.getKey();
                            String value = detailSnapshot.getValue(String.class);
                            if (key != null && value != null) {
                                destinationDetails.put(key, value);
                            }
                        }
                        // Add the destination details to the result
                        result.put(destId, destinationDetails);
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
