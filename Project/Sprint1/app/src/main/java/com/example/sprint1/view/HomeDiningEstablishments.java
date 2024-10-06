package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class HomeDiningEstablishments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dining_establishments);
        EdgeToEdge.enable(this);

        ImageButton logButton = findViewById(R.id.view_din_button_log);
        ImageButton desButton = findViewById(R.id.view_din_button_des);
        ImageButton accButton = findViewById(R.id.view_din_button_acc);
        ImageButton traButton = findViewById(R.id.view_din_button_tra);

        logButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDiningEstablishments.this, HomeLogistics.class
            );
            startActivity(intent);
        });

        desButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDiningEstablishments.this, HomeDestination.class
            );
            startActivity(intent);
        });

        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDiningEstablishments.this, HomeAccommodations.class
            );
            startActivity(intent);
        });

        traButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeDiningEstablishments.this, HomeTravelCommunity.class
            );
            startActivity(intent);
        });
    }
}