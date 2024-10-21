package com.example.sprint1.model;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


public class UserDatabase {

    private final DatabaseReference userDatabase;
    private String usernameCurr;

    public UserDatabase() {
        this.userDatabase = FirebaseDatabase.getInstance().getReference("user");
    }

    public String getUsernameCurr() {
        return this.usernameCurr;
    }

    public void userSignUp(String username, String password, MainModel.BoolCallback callback) {
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

    public void userSignIn(String username, String password, MainModel.BoolCallback callback) {
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

    public void setVacation(String startDate, String endDate, String duration) {
        DatabaseReference refer = this.userDatabase.child(this.usernameCurr).child("vacation");
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> value = new HashMap<>();
                value.put("startDate", startDate);
                value.put("endDate", endDate);
                value.put("duration", duration);
                refer.setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
