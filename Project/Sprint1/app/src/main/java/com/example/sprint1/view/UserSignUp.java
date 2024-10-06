package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;


public class UserSignUp extends AppCompatActivity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        //find button by id
        Button registerButton = findViewById(R.id.btn_create_account_register);
        Button quitButton = findViewById(R.id.quit);

        username = findViewById(R.id.text_username_input);
        password = findViewById(R.id.text_password_input);

        MainViewModel mainViewModel = new MainViewModel();

        registerButton.setOnClickListener(v -> {
            //create an intent to go into the Logistic Screen
            boolean validSignUp = mainViewModel.userSignUp(
                    username.getText().toString().trim(), password.getText().toString().trim()
            );

            if (validSignUp) {
                Intent intent = new Intent(UserSignUp.this, UserLogIn.class);
                startActivity(intent);
            }
        });

        quitButton.setOnClickListener(v -> finishAffinity());
    }
}