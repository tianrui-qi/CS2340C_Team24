package com.example.sprint1.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class UserDatabase {

    /* Instance fields */

    private final DatabaseReference userDatabase;
    private String usernameCurr;

    /* Constructors */

    public UserDatabase() {
        this.userDatabase = FirebaseDatabase.getInstance().getReference("user");
    }

    /* Main Features */

    public void userSignUp(
            String username, String password,
            Callback<Boolean> callback
    ) {
        DatabaseReference refer = this.userDatabase.child(username);
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onResult(false);
                } else {
                    HashMap<String, String> value = new HashMap<>();
                    value.put("username", username);
                    value.put("password", password);
                    userDatabase.child(username).setValue(value);
                    callback.onResult(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public void userSignIn(
            String username, String password,
            Callback<Boolean> callback
    ) {
        DatabaseReference refer = this.userDatabase.child(username);
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String passwordStored = dataSnapshot.child("password").getValue(String.class);
                if (dataSnapshot.exists() && password.equals(passwordStored)) {
                    usernameCurr = username;
                    callback.onResult(true);
                } else {
                    callback.onResult(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public void setVacation(
            String startDate, String endDate, String duration,
            Callback<Boolean> callback
    ) {
        DatabaseReference refer = this.userDatabase.child(this.usernameCurr).child("vacation");
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> vacationData = new HashMap<>();
                vacationData.put("startDate", startDate);
                vacationData.put("endDate", endDate);
                vacationData.put("duration", duration);
                refer.setValue(vacationData);
                callback.onResult(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public void getVacation(
            Callback<HashMap<String, String>> callback
    ) {
        DatabaseReference refer = this.userDatabase.child(this.usernameCurr).child("vacation");
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Prepare a HashMap to store the vacation data
                    HashMap<String, String> vacationData = new HashMap<>();
                    // Extract startDate, endDate, and duration from the snapshot
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String key = child.getKey();
                        String value = child.getValue(String.class);
                        if (key != null && value != null) {
                            vacationData.put(key, value);
                        }
                    }
                    // Pass the result to the callback
                    callback.onResult(vacationData);
                } else {
                    // If no vacation data exists, return an empty HashMap
                    callback.onResult(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(null);
            }
        });
    }

    /* Getters and Setters */

    public String getUsernameCurr() {
        return this.usernameCurr;
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
