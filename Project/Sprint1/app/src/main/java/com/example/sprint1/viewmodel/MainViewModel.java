package com.example.sprint1.viewmodel;
import com.example.sprint1.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import java.util.Map;
import java.util.HashMap;


public class MainViewModel extends ViewModel {
    private final Users users = new Users();
    private final DatabaseReference databaseReference;

    public MainViewModel() {
        // init Users users by data in firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // get current user's username and password
                        String username = userSnapshot.child("username").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);
                        // add user to Users users
                        users.addUser(username, password);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public boolean userSignUp(String username, String password) {
        // 1. input username and password invalid, return false
        if (username == null || username.trim().isEmpty()
                || password  == null || password.trim().isEmpty()) {
            return false;
        }
        // 2. check if username already exist
        if (this.users.getUser(username) != null) {
            return false;
        }
        // 2. update Users users
        this.users.addUser(username, password);
        // 3. update DatabaseReference databaseReference
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        databaseReference.child("users").child(username).setValue(user);
        return true;    // means user sign up success
    }

    public boolean userLogIn(String username, String password) {
        // 1. check input username and password
        if (username == null || username.trim().isEmpty()
                || password  == null || password.trim().isEmpty()) {
            return false;
        }
        // 2. check if username and password valid
        return this.users.checkUser(username, password);
    }
}
