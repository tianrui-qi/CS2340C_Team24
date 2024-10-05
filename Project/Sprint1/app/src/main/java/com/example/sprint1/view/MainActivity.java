package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;

public class MainActivity extends AppCompatActivity {
    private static final int DELAY_MILLIS = 1000;
    private final String TAG = "WelcomeScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Log.d(TAG, "onCreate");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, UserLogIn.class);

                startActivity(intent);

                finish();
            }
        }, DELAY_MILLIS);
    }
}