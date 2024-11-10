package com.example.sprint1.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class UserDatabase {

    /* Instance fields */

    private final DatabaseReference userDatabase;
    private String usernameCurr;

    /* Constructors */

    public UserDatabase() {
        this.userDatabase = FirebaseDatabase.getInstance().getReference("user");
    }

    /* Feature 1: User Account */

    public void userSignUp(
            String username, String password,
            Callback<Boolean> callback
    ) {
        DatabaseReference ref = this.userDatabase.child(username);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference ref = this.userDatabase.child(username);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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

    /* Feature 2: Log Travel */

    public void addDestination(
            String key,
            Callback<Boolean> callback
    ) {
        this.getCollaborator(collaborator -> {
            // Prepare a list of users to update
            ArrayList<String> usersToUpdate;
            if (collaborator == null) {
                usersToUpdate = new ArrayList<>();
            } else {
                usersToUpdate = new ArrayList<>(collaborator);
            }
            usersToUpdate.add(this.usernameCurr);

            // Update destinations for all users in the list
            int[] pendingUpdates = {usersToUpdate.size()};
            boolean[] hasFailed = {false};
            for (String username : usersToUpdate) {
                addDestination(key, username, success -> {
                    synchronized (pendingUpdates) {
                        if (!success) {
                            hasFailed[0] = true; // Mark as failed if any update fails
                        }
                        pendingUpdates[0]--;
                        // If all updates are done
                        if (pendingUpdates[0] == 0) {
                            callback.onResult(!hasFailed[0]); // Success if no failures occurred
                        }
                    }
                });
            }
        });
    }

    private void addDestination(
            String key, String username,
            Callback<Boolean> callback
    ) {
        DatabaseReference ref = this.userDatabase.child(username).child("destination");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> value = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    // Parse the raw data into a list
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String destinationKey = child.getValue(String.class);
                        if (destinationKey != null) {
                            value.add(destinationKey);
                        }
                    }
                }

                // Check if the key is already in the list
                if (!value.contains(key)) {
                    value.add(key); // Add the new key if it's not already in the list
                }

                // Update the destinations list in Firebase
                ref.setValue(value).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(true); // Success
                    } else {
                        callback.onResult(false); // Failure
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public void getDestination(
            Callback<ArrayList<String>> callback
    ) {
        DatabaseReference ref = this.userDatabase.child(this.usernameCurr).child("destination");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.onResult(null);
                    return;
                }

                ArrayList<String> destinations = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String destinationId = child.getValue(String.class);
                    if (destinationId != null) {
                        destinations.add(destinationId);
                    }
                }
                callback.onResult(destinations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(null);
            }
        });
    }

    /* Feature 3: Calculate Vacation Time */

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

    /* Feature 4: Collaboration */

    public void addCollaborator(
            String username,
            Callback<Boolean> callback
    ) {
        this.getCollaborator(collaborator -> {
            // Prepare a list of users to update
            ArrayList<String> usersToUpdate;
            if (collaborator == null) {
                usersToUpdate = new ArrayList<>();
            } else {
                usersToUpdate = new ArrayList<>(collaborator);
            }
            usersToUpdate.add(username);
            usersToUpdate.add(this.usernameCurr);

            // Track total updates (collaborators + destination copy)
            int[] pendingUpdates = {usersToUpdate.size() + 1};
            boolean[] hasFailed = {false};

            // Update "destination" for the new collaborator
            this.getDestination(destinations -> {
                DatabaseReference destRef = this.userDatabase.child(username).child("destination");
                destRef.setValue(destinations).addOnCompleteListener(task -> {
                    synchronized (pendingUpdates) {
                        if (!task.isSuccessful()) {
                            hasFailed[0] = true;
                        }
                        pendingUpdates[0]--;
                        if (pendingUpdates[0] == 0) {
                            callback.onResult(!hasFailed[0]);
                        }
                    }
                });
            });

            // Update "collaborator" for all users in the list
            for (String user : usersToUpdate) {
                // Create a unique collaborator list for each user
                ArrayList<String> temp = new ArrayList<>(usersToUpdate);
                temp.remove(user); // Remove the user themselves

                DatabaseReference ref = this.userDatabase.child(user).child("collaborator");
                ref.setValue(temp).addOnCompleteListener(task -> {
                    synchronized (pendingUpdates) {
                        if (!task.isSuccessful()) {
                            hasFailed[0] = true;
                        }
                        pendingUpdates[0]--;
                        if (pendingUpdates[0] == 0) {
                            callback.onResult(!hasFailed[0]);
                        }
                    }
                });
            }
        });
    }

    public void getCollaborator(
            Callback<ArrayList<String>> callback
    ) {
        DatabaseReference ref = this.userDatabase.child(this.usernameCurr).child("collaborator");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.onResult(null);
                    return;
                }

                ArrayList<String> collaborator = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String user = snapshot.getValue(String.class);
                    if (user != null) {
                        collaborator.add(user);
                    }
                }
                callback.onResult(collaborator); // Return the list of shared users
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(null); // Return null on error
            }
        });
    }

    public void getNonCollaborator(
            Callback<ArrayList<String>> callback
    ) {
        // Fetch the current user's "contributor" list
        this.getCollaborator(contributors -> {
            // Fetch all users from the database
            this.userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> userList = new ArrayList<>();
                    // Iterate through all users in the database
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String user = userSnapshot.getKey();
                        // Exclude the current user and users in the "contributor" list
                        if (user != null && !user.equals(usernameCurr)
                                && (contributors == null || !contributors.contains(user))) {
                            userList.add(user);
                        }
                    }
                    callback.onResult(userList); // Return the filtered user list
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onResult(null); // Return null in case of an error
                }
            });
        });
    }

    public void addNote(
            String note,
            Callback<Boolean> callback
    ) {
        DatabaseReference ref = this.userDatabase.child(this.usernameCurr).child("note");
        ref.setValue(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true); // Successfully added the note
            } else {
                callback.onResult(false); // Failed to add the note
            }
        });
    }

    public void getNote(
            Callback<HashMap<String, String>> callback
    ) {
        // Fetch the current user's collaborator list
        this.getCollaborator(collaborator -> {
            ArrayList<String> usersToFetch;
            if (collaborator == null) {
                usersToFetch = new ArrayList<>();
            } else {
                usersToFetch = new ArrayList<>(collaborator);
            }
            usersToFetch.add(this.usernameCurr); // Include current user

            // Create a map to store notes
            HashMap<String, String> notesMap = new HashMap<>();
            int[] pendingRequests = {usersToFetch.size()};
            boolean[] hasFailed = {false};

            for (String user : usersToFetch) {
                DatabaseReference ref = this.userDatabase.child(user).child("note");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        synchronized (pendingRequests) {
                            if (dataSnapshot.exists()) {
                                String note = dataSnapshot.getValue(String.class);
                                // Add note or empty string
                                notesMap.put(user, note != null ? note : "");
                            } else {
                                notesMap.put(user, ""); // No note found, add empty string
                            }

                            pendingRequests[0]--;
                            if (pendingRequests[0] == 0) {
                                callback.onResult(hasFailed[0] ? null : notesMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        synchronized (pendingRequests) {
                            hasFailed[0] = true;
                            pendingRequests[0]--;
                            if (pendingRequests[0] == 0) {
                                callback.onResult(null);
                            }
                        }
                    }
                });
            }
        });
    }

    /* Callbacks */

    public interface Callback<T> {
        void onResult(T callback);
    }
}
