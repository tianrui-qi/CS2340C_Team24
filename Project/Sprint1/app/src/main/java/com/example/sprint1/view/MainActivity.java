package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class MainActivity extends AppCompatActivity {
    private static final int DELAY_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, UserLogIn.class);
            startActivity(intent);
            finish();
        }, DELAY_MILLIS);
    }
}