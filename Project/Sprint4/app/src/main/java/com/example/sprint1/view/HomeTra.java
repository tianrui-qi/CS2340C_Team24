package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;


public class HomeTra extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tra);

        this.buttonNavigationBar();
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
}
