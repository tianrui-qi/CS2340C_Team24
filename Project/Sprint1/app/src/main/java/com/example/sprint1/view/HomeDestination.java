package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;

public class HomeDestination extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_destination);
        EdgeToEdge.enable(this);

        ImageButton logButton = findViewById(R.id.view_des_button_log);
        ImageButton dinButton = findViewById(R.id.view_des_button_din);
        ImageButton accButton = findViewById(R.id.view_des_button_acc);
        ImageButton traButton = findViewById(R.id.view_des_button_tra);

        logButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDestination.this, HomeLogistics.class
            );
            startActivity(intent);
        });

        dinButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDestination.this, HomeDiningEstablishments.class
            );
            startActivity(intent);
        });

        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDestination.this, HomeAccommodations.class
            );
            startActivity(intent);
        });

        traButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDestination.this, HomeTravelCommunity.class
            );
            startActivity(intent);
        });
    }
}