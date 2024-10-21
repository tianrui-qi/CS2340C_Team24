package com.example.sprint1.model;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


public class DestDatabase {

    private final DatabaseReference destDatabase;
    private String usernameCurr;

    public DestDatabase() {
        this.destDatabase = FirebaseDatabase.getInstance().getReference("destination");
    }

    public void setUsernameCurr(String username) {
        this.usernameCurr = username;
    }

    public void addDestination(
            String travelLocation, String startDate, String endDate, String duration,
            MainModel.BoolCallback callback
    ) {
        DatabaseReference refer = this.destDatabase.child(this.usernameCurr).child(travelLocation);
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onResult(false);
                } else {
                    HashMap<String, String> value = new HashMap<>();
                    value.put("travelLocation", travelLocation);
                    value.put("startDate", startDate);
                    value.put("endDate", endDate);
                    value.put("duration", duration);
                    destDatabase.child(usernameCurr).child(travelLocation).setValue(value);
                    callback.onResult(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public void getDestinations(MainModel.DestCallback callback) {
        DatabaseReference refer = this.destDatabase.child(this.usernameCurr);
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, HashMap<String, String>> dest = new HashMap<>();
                    for (DataSnapshot destinationSnapshot : dataSnapshot.getChildren()) {
                        String travelLocation = destinationSnapshot.getKey();
                        HashMap<String, String> destinationDetails = new HashMap<>();
                        for (DataSnapshot detailSnapshot : destinationSnapshot.getChildren()) {
                            String key = detailSnapshot.getKey();
                            String value = detailSnapshot.getValue(String.class);
                            destinationDetails.put(key, value);
                        }
                        dest.put(travelLocation, destinationDetails);
                    }
                    callback.onResult(dest);
                } else {
                    callback.onResult(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(null);
            }
        });
    }
}
