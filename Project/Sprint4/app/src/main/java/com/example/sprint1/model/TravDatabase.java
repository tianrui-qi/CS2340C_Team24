package com.example.sprint1.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TravDatabase {

    /* Instance fields */

    private final DatabaseReference travelDatabase;

    /* Constructors */

    public TravDatabase() {
        this.travelDatabase = FirebaseDatabase.getInstance().getReference("travel");
    }

    /* Main Features */

    public void addTravel(
            Map<String, String> travelData,
            Callback<String> callback
    ) {
        // Generate a unique key for the travel post
        String key = this.travelDatabase.push().getKey();

        if (key == null) {
            callback.onResult(null);
            return;
        }

        // Save the travel post to Firebase
        this.travelDatabase.child(key).setValue(travelData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(key); // Return the travel post ID if successful
            } else {
                callback.onResult(null); // Return null if the operation fails
            }
        });
    }

    public void getTravel(
            Callback<HashMap<String, HashMap<String, String>>> callback
    ) {
        this.travelDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> travelPosts = new HashMap<>();

                // Iterate through each travel post
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> postDetails = new HashMap<>();
                    for (DataSnapshot detailSnapshot : postSnapshot.getChildren()) {
                        String key = detailSnapshot.getKey();
                        String value = detailSnapshot.getValue(String.class);
                        if (key != null && value != null) {
                            postDetails.put(key, value);
                        }
                    }
                    // Add the travel post details to the result
                    travelPosts.put(postSnapshot.getKey(), postDetails);
                }

                callback.onResult(travelPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(null); // Return null if the operation fails
            }
        });
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}