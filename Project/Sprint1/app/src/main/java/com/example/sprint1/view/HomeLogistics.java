package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class HomeLogistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_logistics);
        EdgeToEdge.enable(this);

        ImageButton desButton = findViewById(R.id.view_log_button_des);
        ImageButton dinButton = findViewById(R.id.view_log_button_din);
        ImageButton accButton = findViewById(R.id.view_log_button_acc);
        ImageButton traButton = findViewById(R.id.view_log_button_tra);

        desButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeLogistics.this, HomeDestination.class
            );
            startActivity(intent);
        });

        dinButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeLogistics.this, HomeDiningEstablishments.class
            );
            startActivity(intent);
        });

        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeLogistics.this, HomeAccommodations.class
            );
            startActivity(intent);
        });

        traButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeLogistics.this, HomeTravelCommunity.class
            );
            startActivity(intent);
        });
    }
}