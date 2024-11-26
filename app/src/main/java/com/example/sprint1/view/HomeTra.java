package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeTra extends AppCompatActivity {

    private final MainViewModel mainViewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tra);

        this.listTravelPosts();
        this.buttonAddTravelPost();
        this.buttonNavigationBar();
    }

    private void listTravelPosts() {
        LinearLayout travelContainer = findViewById(R.id.travel_posts_container);

        // Clear previous records
        travelContainer.removeAllViews();

        // Fetch travel posts from ViewModel
        mainViewModel.getTravel(travelPosts -> {
            if (travelPosts == null || travelPosts.isEmpty()) {
                return;
            }

            // Convert the travel posts into a list for sorting
            List<Map.Entry<String, HashMap<String, String>>> sortedTravelPosts =
                    new ArrayList<>(travelPosts.entrySet());

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            // Sort travel posts by the start date
            sortedTravelPosts.sort((entry1, entry2) -> {
                try {
                    String start1 = entry1.getValue() != null
                            ? entry1.getValue().get("start") : null;
                    String start2 = entry2.getValue() != null
                            ? entry2.getValue().get("start") : null;

                    Date date1 = (start1 != null && !start1.isEmpty())
                            ? dateFormat.parse(start1) : null;
                    Date date2 = (start2 != null && !start2.isEmpty())
                            ? dateFormat.parse(start2) : null;

                    if (date1 == null && date2 == null) {
                        return 0;
                    } else if (date1 == null) {
                        return 1;
                    } else if (date2 == null) {
                        return -1;
                    }

                    return date1.compareTo(date2);
                } catch (Exception e) {
                    return 0;
                }
            });

            // Display sorted travel posts
            for (Map.Entry<String, HashMap<String, String>> entry : sortedTravelPosts) {
                HashMap<String, String> travelInfo = entry.getValue();

                // Inflate the travel post card layout
                View travelCard = getLayoutInflater().inflate(
                        R.layout.home_tra_card, travelContainer, false
                );

                // Populate travel post details
                TextView usernameText = travelCard.findViewById(R.id.Home_Tra_ListPost_Username);
                TextView startText = travelCard.findViewById(R.id.Home_Tra_ListPost_Start);
                TextView endText = travelCard.findViewById(R.id.Home_Tra_ListPost_End);
                TextView destinationText = travelCard.findViewById(
                        R.id.Home_Tra_ListPost_Destination
                );
                TextView accommodationText = travelCard.findViewById(
                        R.id.Home_Tra_ListPost_Accommodation
                );
                TextView diningText = travelCard.findViewById(R.id.Home_Tra_ListPost_Dining);
                TextView noteText = travelCard.findViewById(R.id.Home_Tra_ListPost_Note);

                // Use string resources with placeholders
                usernameText.setText(getString(
                        R.string.Home_Tra_ListPost_Username, travelInfo.get("username")));
                startText.setText(getString(
                        R.string.Home_Tra_ListPost_Start, travelInfo.get("start")));
                endText.setText(getString(
                        R.string.Home_Tra_ListPost_End, travelInfo.get("end")));
                destinationText.setText(getString(
                        R.string.Home_Tra_ListPost_Destination, travelInfo.get("destination")));
                accommodationText.setText(getString(
                        R.string.Home_Tra_ListPost_Accommodation, travelInfo.get("accommodation")));
                diningText.setText(getString(
                        R.string.Home_Tra_ListPost_Dining, travelInfo.get("dining")));
                noteText.setText(getString(
                        R.string.Home_Tra_ListPost_Note, travelInfo.get("note")));

                // Add the card to the container
                travelContainer.addView(travelCard);
            }
        });
    }

    private void buttonAddTravelPost() {
        Button addPostButton = findViewById(R.id.Home_Tra_AddPost);
        LinearLayout addPostForm = findViewById(R.id.Home_Tra_AddTravel_Form);

        EditText inputStart = findViewById(R.id.Home_Tra_AddTravel_Start);
        EditText inputEnd = findViewById(R.id.Home_Tra_AddTravel_End);
        EditText inputDestination = findViewById(R.id.Home_Tra_AddTravel_Destination);
        EditText inputAccommodation = findViewById(R.id.Home_Tra_AddTravel_Accommodation);
        EditText inputDining = findViewById(R.id.Home_Tra_AddTravel_Dining);
        EditText inputNote = findViewById(R.id.Home_Tra_AddTravel_Note);

        addPostButton.setOnClickListener(v -> {
            if (addPostForm.getVisibility() == View.GONE) {
                addPostForm.setVisibility(View.VISIBLE);
            } else {
                addPostForm.setVisibility(View.GONE);
                clearAddPostForm(
                        inputStart, inputEnd,
                        inputDestination, inputAccommodation, inputDining, inputNote
                );
            }
        });

        findViewById(R.id.Home_Tra_AddTravel_Cancel).setOnClickListener(v -> {
            addPostForm.setVisibility(View.GONE);
            clearAddPostForm(
                    inputStart, inputEnd,
                    inputDestination, inputAccommodation, inputDining, inputNote
            );
        });

        findViewById(R.id.Home_Tra_AddTravel_Submit).setOnClickListener(v -> {
            String start = inputStart.getText().toString().trim();
            String end = inputEnd.getText().toString().trim();
            String destination = inputDestination.getText().toString().trim();
            String accommodation = inputAccommodation.getText().toString().trim();
            String dining = inputDining.getText().toString().trim();
            String note = inputNote.getText().toString().trim();

            mainViewModel.addTravel(
                    start, end, destination, accommodation, dining, note, success -> {
                        if (success) {
                            this.listTravelPosts();
                            addPostForm.setVisibility(View.GONE);
                            clearAddPostForm(
                                    inputStart, inputEnd, inputDestination, inputAccommodation,
                                    inputDining, inputNote
                            );
                        }
                    }
            );
        });
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_tra_button_log).setOnClickListener(
                v -> startActivity(new Intent(HomeTra.this, HomeLog.class))
        );
        findViewById(R.id.view_tra_button_des).setOnClickListener(
                v -> startActivity(new Intent(HomeTra.this, HomeDes.class))
        );
        findViewById(R.id.view_tra_button_din).setOnClickListener(
                v -> startActivity(new Intent(HomeTra.this, HomeDin.class))
        );
        findViewById(R.id.view_tra_button_acc).setOnClickListener(
                v -> startActivity(new Intent(HomeTra.this, HomeAcc.class))
        );
    }

    private void clearAddPostForm(EditText... fields) {
        for (EditText field : fields) {
            field.setText("");
        }
    }
}