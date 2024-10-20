package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;


public class UserLogIn extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_log_in);
        //find button by id
        Button logInButton = findViewById(R.id.btn_login);
        Button createAccountButton = findViewById(R.id.btn_create_account);
        Button quitButton = findViewById(R.id.quit);

        username = findViewById(R.id.text_username_input);
        password = findViewById(R.id.text_password_input);

        MainViewModel mainViewModel = new MainViewModel();

        logInButton.setOnClickListener(v -> {
            boolean validLogIn = mainViewModel.userLogIn(
                    username.getText().toString().trim(), password.getText().toString().trim()
            );

            if (validLogIn) {
                Intent intent = new Intent(UserLogIn.this, HomeLogistics.class);
                //start the second activity
                startActivity(intent);
            }
        });

        createAccountButton.setOnClickListener(v -> {
            //create an intent to go into the Logistic Screen
            Intent intent = new Intent(UserLogIn.this, UserSignUp.class);
            //start the second activity
            startActivity(intent);
        });
        //retrieve the intent that started this activity
        quitButton.setOnClickListener(v -> finishAffinity());
    }
}