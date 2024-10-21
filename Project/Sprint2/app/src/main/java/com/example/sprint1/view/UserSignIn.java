package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;


public class UserSignIn extends AppCompatActivity {

    private final MainViewModel mainViewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signin);

        this.buttonSignIn();
        this.buttonCreateAccount();
        this.buttonQuit();
    }

    private void buttonSignIn() {
        EditText username = findViewById(R.id.text_username_input);
        EditText password = findViewById(R.id.text_password_input);

        findViewById(R.id.btn_login).setOnClickListener(
                v -> mainViewModel.userSignIn(
                        username.getText().toString().trim(),
                        password.getText().toString().trim(),
                        success -> {
                            if (success) {
                                startActivity(new Intent(UserSignIn.this, HomeLog.class));
                                username.setText("");
                                password.setText("");
                            }
                        }
                )
        );
    }

    private void buttonCreateAccount() {
        findViewById(R.id.btn_create_account).setOnClickListener(
                v -> startActivity(new Intent(UserSignIn.this, UserSignUp.class))
        );
    }

    private void buttonQuit() {
        findViewById(R.id.quit).setOnClickListener(v -> finishAffinity());
    }
}