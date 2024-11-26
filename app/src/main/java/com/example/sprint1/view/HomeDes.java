package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;

import java.util.HashMap;


public class HomeDes extends AppCompatActivity {

    private final MainViewModel mainViewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_des);

        this.buttonLogTravel();
        this.formLogTravel();
        this.buttonCancel();
        this.buttonCalculate();
        this.formCalculate();
        this.buttonBack();
        this.listDestination();
        this.buttonNavigationBar();
    }

    private void buttonLogTravel() {
        View formLogTravel = findViewById(R.id.HomeDestination_LogTravel_Form);
        View formCalculate = findViewById(R.id.HomeDestination_Calculate_Form);
        View resultCalculate = findViewById(R.id.HomeDestination_Calculate_Result);

        findViewById(R.id.HomeDestination_LogTravel).setOnClickListener(v -> {
            formCalculate.setVisibility(View.GONE);
            resultCalculate.setVisibility(View.GONE);
            if (formLogTravel.getVisibility() == View.VISIBLE) {
                formLogTravel.setVisibility(View.GONE);
            } else {
                formLogTravel.setVisibility(View.VISIBLE);
            }
        });
    }

    private void formLogTravel() {
        EditText travelLocation = findViewById(R.id.HomeDestination_LogTravel_TravelLocation);
        EditText startDate = findViewById(R.id.HomeDestination_LogTravel_StartDate);
        EditText endDate = findViewById(R.id.HomeDestination_LogTravel_EndDate);

        findViewById(R.id.HomeDestination_LogTravel_Submit).setOnClickListener(
                v -> mainViewModel.addDestination(
                        travelLocation.getText().toString().trim(),
                        startDate.getText().toString().trim(),
                        endDate.getText().toString().trim(),
                        success -> {
                            if (success) {
                                travelLocation.setText("");
                                startDate.setText("");
                                endDate.setText("");
                                this.listDestination();
                            }
                        }
                )
        );
    }

    private void buttonCancel() {
        View formLogTravel = findViewById(R.id.HomeDestination_LogTravel_Form);

        findViewById(R.id.HomeDestination_LogTravel_Cancel).setOnClickListener(
                v -> formLogTravel.setVisibility(View.GONE)
        );
    }

    private void buttonCalculate() {
        View formLogTravel = findViewById(R.id.HomeDestination_LogTravel_Form);
        View formCalculate = findViewById(R.id.HomeDestination_Calculate_Form);
        View resultCalculate = findViewById(R.id.HomeDestination_Calculate_Result);

        findViewById(R.id.HomeDestination_Calculate).setOnClickListener(v -> {
            formLogTravel.setVisibility(View.GONE);
            resultCalculate.setVisibility(View.GONE);
            if (formCalculate.getVisibility() == View.VISIBLE) {
                formCalculate.setVisibility(View.GONE);
            } else {
                formCalculate.setVisibility(View.VISIBLE);
            }
        });
    }

    private void formCalculate() {
        EditText startDate = findViewById(R.id.HomeDestination_Calculate_StartDate);
        EditText endDate = findViewById(R.id.HomeDestination_Calculate_EndDate);
        EditText duration = findViewById(R.id.HomeDestination_Calculate_Duration);
        View formCalculate = findViewById(R.id.HomeDestination_Calculate_Form);
        View resultCalculate = findViewById(R.id.HomeDestination_Calculate_Result);
        TextView resultView = findViewById(R.id.HomeDestination_Calculate_Message);

        findViewById(R.id.HomeDestination_Calculate_Calculate).setOnClickListener(
                v -> mainViewModel.setVacation(
                        startDate.getText().toString().trim(),
                        endDate.getText().toString().trim(),
                        duration.getText().toString().trim(),
                        success -> {
                            if (success) {
                                startDate.setText("");
                                endDate.setText("");
                                duration.setText("");
                                mainViewModel.calVacation(occupiedDays -> {
                                    // Update the result view with the occupied days
                                    if (occupiedDays != null) {
                                        resultView.setText(getString(
                                                R.string.HomeDestination_Calculate_Message,
                                                occupiedDays
                                        ));
                                    } else {
                                        resultView.setText(
                                                R.string.HomeDestination_Calculate_Error
                                        );
                                    }
                                });
                                formCalculate.setVisibility(View.GONE);
                                resultCalculate.setVisibility(View.VISIBLE);
                            }
                        })
        );
    }

    private void buttonBack() {
        View formCalculate = findViewById(R.id.HomeDestination_Calculate_Form);
        View resultCalculate = findViewById(R.id.HomeDestination_Calculate_Result);

        findViewById(R.id.HomeDestination_Calculate_Back).setOnClickListener(v -> {
            resultCalculate.setVisibility(View.GONE);
            formCalculate.setVisibility(View.VISIBLE);
        });
    }

    private void listDestination() {
        LinearLayout container = findViewById(R.id.log_travel_records_container);
        container.removeAllViews(); // Clear any previous views

        mainViewModel.getDestination(destinations -> {
            LayoutInflater inflater = LayoutInflater.from(this);

            if (destinations != null && !destinations.isEmpty()) {
                // Loop through each destination and create a card for each
                for (String destination : destinations.keySet()) {
                    HashMap<String, String> detail = destinations.get(destination);
                    String duration = (detail != null) ? detail.get("duration") : null;
                    String travelLocation = (detail != null) ? detail.get("travelLocation") : null;

                    // Inflate the home_des_card layout
                    View cardView = inflater.inflate(R.layout.home_des_card, container, false);

                    // Set the location and duration text
                    TextView locationText = cardView.findViewById(R.id.location_text);
                    TextView durationText = cardView.findViewById(R.id.duration_text);

                    locationText.setText(travelLocation);
                    durationText.setText(String.format("%s days", duration));

                    // Add the card to the container
                    container.addView(cardView);
                }
            }
        });
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_des_button_log).setOnClickListener(
                v -> startActivity(new Intent(HomeDes.this, HomeLog.class))
        );
        findViewById(R.id.view_des_button_din).setOnClickListener(
                v -> startActivity(new Intent(HomeDes.this, HomeDin.class))
        );
        findViewById(R.id.view_des_button_acc).setOnClickListener(
                v -> startActivity(new Intent(HomeDes.this, HomeAcc.class))
        );
        findViewById(R.id.view_des_button_tra).setOnClickListener(
                v -> startActivity(new Intent(HomeDes.this, HomeTra.class))
        );
    }
}
