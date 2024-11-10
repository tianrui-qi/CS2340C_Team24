package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeAcc extends AppCompatActivity {

    private final MainViewModel mainViewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_acc);

        this.listAccommodations();
        this.buttonAddAccommodation();
        this.buttonNavigationBar();
    }

    private void listAccommodations() {
        // Find the accommodation container
        LinearLayout accContainer = findViewById(R.id.accommodation_records_container);

        // Clear previous records
        accContainer.removeAllViews();

        // Fetch accommodation records from ViewModel
        mainViewModel.getAccommodation(accData -> {
            if (accData == null || accData.isEmpty()) {
                return;
            }

            // Prepare a list of accommodation data with time parsing
            List<Map.Entry<String, HashMap<String, String>>> sortedAccList;
            sortedAccList = new ArrayList<>(accData.entrySet());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            // Sort accommodations by check-in time
            sortedAccList.sort((entry1, entry2) -> {
                try {
                    HashMap<String, String> acc1 = entry1.getValue();
                    HashMap<String, String> acc2 = entry2.getValue();

                    // Null checks for the maps
                    if (acc1 == null || acc2 == null) {
                        return 0;
                    }

                    // Retrieve "checkIn" values safely
                    String checkIn1 = acc1.get("checkIn");
                    String checkIn2 = acc2.get("checkIn");

                    Date date1 = (checkIn1 == null || checkIn1.isEmpty())
                            ? null : dateFormat.parse(checkIn1);
                    Date date2 = (checkIn2 == null || checkIn2.isEmpty())
                            ? null : dateFormat.parse(checkIn2);

                    if (date1 == null && date2 == null) {
                        return 0;
                    } else if (date1 == null) {
                        return 1; // Treat null as greater (push to the end)
                    } else if (date2 == null) {
                        return -1; // Treat null as greater (push to the end)
                    }

                    return date1.compareTo(date2); // Sort by ascending check-in date
                } catch (ParseException e) {
                    return 0;
                }
            });

            // Get current date
            Date currentDate = new Date();

            // Create cards for each accommodation record
            for (Map.Entry<String, HashMap<String, String>> entry : sortedAccList) {
                HashMap<String, String> accInfo = entry.getValue();

                // Parse the check-in and check-out times for comparison
                Date checkInDate = null;
                Date checkOutDate = null;
                try {
                    String checkInStr = accInfo.get("checkIn");
                    String checkOutStr = accInfo.get("checkOut");
                    if (checkInStr != null) {
                        checkInDate = dateFormat.parse(checkInStr);
                    }
                    if (checkOutStr != null) {
                        checkOutDate = dateFormat.parse(checkOutStr);
                    }
                } catch (ParseException ignored) { }

                // Inflate the accommodation card layout
                View accCard = getLayoutInflater().inflate(
                        R.layout.home_acc_card, accContainer, false
                );

                // Set accommodation details
                TextView locationText = accCard.findViewById(
                        R.id.Home_Acc_ListAccommodation_Location
                );
                TextView checkInText = accCard.findViewById(
                        R.id.Home_Acc_ListAccommodation_CheckIn
                );
                TextView checkOutText = accCard.findViewById(
                        R.id.Home_Acc_ListAccommodation_CheckOut
                );
                TextView numRoomsText = accCard.findViewById(
                        R.id.Home_Acc_ListAccommodation_RoomNum
                );
                TextView roomTypesText = accCard.findViewById(
                        R.id.Home_Acc_ListAccommodation_RoomType
                );

                locationText.setText(String.format(
                        "%s %s", getString(R.string.Home_Acc_ListAccommodation_Location),
                        accInfo.get("location")
                ));
                checkInText.setText(String.format(
                        "%s %s", getString(R.string.Home_Acc_ListAccommodation_CheckIn),
                        accInfo.get("checkIn")
                ));
                checkOutText.setText(String.format(
                        "%s %s", getString(R.string.Home_Acc_ListAccommodation_CheckOut),
                        accInfo.get("checkOut")
                ));
                numRoomsText.setText(String.format(
                        "%s %s", getString(R.string.Home_Acc_ListAccommodation_RoomNum),
                        accInfo.get("roomNum")
                ));
                roomTypesText.setText(String.format(
                        "%s %s", getString(R.string.Home_Acc_ListAccommodation_RoomType),
                        accInfo.get("roomType")
                ));

                // Change the color of the check-in text based on whether the check-in is
                // in the past or future
                if (checkInDate != null) {
                    if (checkInDate.before(currentDate)) {
                        checkInText.setTextColor(
                                ContextCompat.getColor(this, android.R.color.holo_red_dark)
                        ); // Past check-in
                    } else {
                        checkInText.setTextColor(
                                ContextCompat.getColor(this, android.R.color.holo_green_dark)
                        ); // Upcoming check-in
                    }
                }

                // Change the color of the check-out text based on whether the check-out is
                // in the past or future
                if (checkOutDate != null) {
                    if (checkOutDate.before(currentDate)) {
                        checkOutText.setTextColor(
                                ContextCompat.getColor(this, android.R.color.holo_red_dark)
                        ); // Past check-out
                    } else {
                        checkOutText.setTextColor(
                                ContextCompat.getColor(this, android.R.color.holo_green_dark)
                        ); // Upcoming check-out
                    }
                }

                // Add the card to the container
                accContainer.addView(accCard);
            }
        });
    }

    private void buttonAddAccommodation() {
        Button addAccButton = findViewById(R.id.Home_Acc_AddAccommodation);
        LinearLayout addAccForm = findViewById(R.id.Home_Acc_AddAccommodation_Form);

        EditText inputLocation = findViewById(R.id.Home_Acc_AddAccommodation_Location);
        EditText inputCheckIn = findViewById(R.id.Home_Acc_AddAccommodation_CheckIn);
        EditText inputCheckOut = findViewById(R.id.Home_Acc_AddAccommodation_CheckOut);
        EditText inputNumRooms = findViewById(R.id.Home_Acc_AddAccommodation_RoomNum);
        EditText inputRoomTypes = findViewById(R.id.Home_Acc_AddAccommodation_RoomType);

        addAccButton.setOnClickListener(v -> {
            if (addAccForm.getVisibility() == View.GONE) {
                addAccForm.setVisibility(View.VISIBLE);
            } else {
                addAccForm.setVisibility(View.GONE);
                inputLocation.setText("");
                inputCheckIn.setText("");
                inputCheckOut.setText("");
                inputNumRooms.setText("");
                inputRoomTypes.setText("");
            }
        });

        findViewById(R.id.Home_Acc_AddAccommodation_Cancel).setOnClickListener(v -> {
            addAccForm.setVisibility(View.GONE);
            inputLocation.setText("");
            inputCheckIn.setText("");
            inputCheckOut.setText("");
            inputNumRooms.setText("");
            inputRoomTypes.setText("");
        });

        findViewById(R.id.Home_Acc_AddAccommodation_Submit).setOnClickListener(v -> {
            String location = inputLocation.getText().toString().trim();
            String checkIn = inputCheckIn.getText().toString().trim();
            String checkOut = inputCheckOut.getText().toString().trim();
            String numRooms = inputNumRooms.getText().toString().trim();
            String roomTypes = inputRoomTypes.getText().toString().trim();
            mainViewModel.addAccommodation(
                    location, numRooms, roomTypes, checkIn, checkOut, success -> {
                        if (success) {
                            this.listAccommodations();
                            addAccForm.setVisibility(View.GONE);
                            inputLocation.setText("");
                            inputCheckIn.setText("");
                            inputCheckOut.setText("");
                            inputNumRooms.setText("");
                            inputRoomTypes.setText("");
                        }
                    }
            );
        });
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_acc_button_log).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeLog.class))
        );
        findViewById(R.id.view_acc_button_des).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeDes.class))
        );
        findViewById(R.id.view_acc_button_din).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeDin.class))
        );
        findViewById(R.id.view_acc_button_tra).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeTra.class))
        );
    }
}