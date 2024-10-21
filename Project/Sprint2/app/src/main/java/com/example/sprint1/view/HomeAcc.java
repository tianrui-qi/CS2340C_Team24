package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class HomeAcc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_acc);

        this.buttonNavigationBar();
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_acc_button_log).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeLog.class))
        );
        findViewById(R.id.view_acc_button_des).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeDes.class))
        );
        findViewById(R.id.view_acc_button_din).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeDin.class))
        );
        findViewById(R.id.view_acc_button_tra).setOnClickListener(
                v -> startActivity(new Intent(HomeAcc.this, HomeTra.class))
        );
    }
}
