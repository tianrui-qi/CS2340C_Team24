package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;

public class HomeAccommodations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_accommodations);
        EdgeToEdge.enable(this);

        ImageButton logButton = findViewById(R.id.view_acc_button_log);
        ImageButton desButton = findViewById(R.id.view_acc_button_des);
        ImageButton dinButton = findViewById(R.id.view_acc_button_din);
        ImageButton traButton = findViewById(R.id.view_acc_button_tra);

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HomeAccommodations.this, HomeLogistics.class
                );
                startActivity(intent);
            }
        });

        desButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HomeAccommodations.this, HomeDestination.class
                );
                startActivity(intent);
            }
        });

        dinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HomeAccommodations.this, HomeDiningEstablishments.class
                );
                startActivity(intent);
            }
        });

        traButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HomeAccommodations.this, HomeTravelCommunity.class
                );
                startActivity(intent);
            }
        });
    }
}