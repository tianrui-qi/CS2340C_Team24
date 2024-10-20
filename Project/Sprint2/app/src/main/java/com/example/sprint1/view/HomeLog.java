package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class HomeLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_log);

        this.buttonNavigationBar();
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_log_button_des).setOnClickListener(v -> {
            startActivity(new Intent(HomeLog.this, HomeDes.class));
        });
        findViewById(R.id.view_log_button_din).setOnClickListener(v -> {
            startActivity(new Intent(HomeLog.this, HomeDin.class));
        });
        findViewById(R.id.view_log_button_acc).setOnClickListener(v -> {
            startActivity(new Intent(HomeLog.this, HomeAcc.class));
        });
        findViewById(R.id.view_log_button_tra).setOnClickListener(v -> {
            startActivity(new Intent(HomeLog.this, HomeTra.class));
        });
    }
}
