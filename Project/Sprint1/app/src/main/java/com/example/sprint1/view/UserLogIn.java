package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;

public class UserLogIn extends AppCompatActivity {

    private final String TAG = "LogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_log_in);
        Log.d(TAG, "onCreate");
        //find button by id
        Button logInButton = findViewById(R.id.btn_login);
        Button createAccountButton = findViewById(R.id.btn_create_account);
        Button quitButton = findViewById(R.id.quit);

        //set onclicklistener to the button
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent to go into the Logistic Screen
                Intent intent = new Intent(UserLogIn.this, HomeLogistics.class);

                //start the second activity
                startActivity(intent);

            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent to go into the Logistic Screen
                Intent intent = new Intent(UserLogIn.this, UserSignUp.class);

                //start the second activity
                startActivity(intent);
            }
        });
        //retrieve the intent that started this activity

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}