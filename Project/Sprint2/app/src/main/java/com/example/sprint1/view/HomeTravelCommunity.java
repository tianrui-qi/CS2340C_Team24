package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class HomeTravelCommunity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_travel_community);
        EdgeToEdge.enable(this);

        ImageButton logButton = findViewById(R.id.view_tra_button_log);
        ImageButton desButton = findViewById(R.id.view_tra_button_des);
        ImageButton dinButton = findViewById(R.id.view_tra_button_din);
        ImageButton accButton = findViewById(R.id.view_tra_button_acc);

        logButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeTravelCommunity.this, HomeLogistics.class
            );
            startActivity(intent);
        });

        desButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeTravelCommunity.this, HomeDestination.class
            );
            startActivity(intent);
        });

        dinButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeTravelCommunity.this, HomeDiningEstablishments.class
            );
            startActivity(intent);
        });

        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeTravelCommunity.this, HomeAccommodations.class
            );
            startActivity(intent);
        });
    }
}