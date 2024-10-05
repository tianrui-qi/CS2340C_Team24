package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;

public class UserSignUp extends AppCompatActivity {
    private final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        Log.d(TAG, "onCreate");
        //find button by id
        Button registerButton = findViewById(R.id.btn_create_account_register);
        Button quitButton = findViewById(R.id.quit);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent to go into the Logistic Screen
                Intent intent = new Intent(UserSignUp.this, UserLogIn.class);

                //start the second activity
                startActivity(intent);

            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}