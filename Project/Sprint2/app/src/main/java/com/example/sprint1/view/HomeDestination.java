package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;

public class HomeDestination extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_destination);
        EdgeToEdge.enable(this);

        // Log Travel

        View button_LogTravel = findViewById(R.id.HomeDestination_LogTravel);
        View view_LogTravel_Form = findViewById(R.id.HomeDestination_LogTravel_Form);
        View button_LogTravel_Cancel = findViewById(R.id.HomeDestination_LogTravel_Cancel);
        View button_LogTravel_Submit = findViewById(R.id.HomeDestination_LogTravel_Submit);
        // show the form section after click LogTravel Bottom
        // hide the form section after click LogTravel Bottom again
        button_LogTravel.setOnClickListener(v -> {
            if (view_LogTravel_Form.getVisibility() == View.VISIBLE) {
                view_LogTravel_Form.setVisibility(View.GONE);
            } else {
                view_LogTravel_Form.setVisibility(View.VISIBLE);
            }
        });
        // hide the form section after click Cancel Bottom
        button_LogTravel_Cancel.setOnClickListener(v -> {view_LogTravel_Form.setVisibility(View.GONE);});

        // Calculate Vacation Time

        View button_Calculate = findViewById(R.id.HomeDestination_Calculate);
        View view_Calculate_Form = findViewById(R.id.HomeDestination_Calculate_Form);
        button_Calculate.setOnClickListener(v -> {
            if (view_Calculate_Form.getVisibility() == View.VISIBLE) {
                view_Calculate_Form.setVisibility(View.GONE);
            } else {
                view_Calculate_Form.setVisibility(View.VISIBLE);
            }
        });


        // Navigation Bar

        View logButton = findViewById(R.id.view_des_button_log);
        View dinButton = findViewById(R.id.view_des_button_din);
        View accButton = findViewById(R.id.view_des_button_acc);
        View traButton = findViewById(R.id.view_des_button_tra);
        logButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDestination.this, HomeLogistics.class);
            startActivity(intent);
        });
        dinButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDestination.this, HomeDiningEstablishments.class);
            startActivity(intent);
        });
        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDestination.this, HomeAccommodations.class);
            startActivity(intent);
        });
        traButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDestination.this, HomeTravelCommunity.class);
            startActivity(intent);
        });
    }
}