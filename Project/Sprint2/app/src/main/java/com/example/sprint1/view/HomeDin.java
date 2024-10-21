package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;


public class HomeDin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_din);

        this.buttonNavigationBar();
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_din_button_log).setOnClickListener(
                v -> startActivity(new Intent(HomeDin.this, HomeLog.class))
        );
        findViewById(R.id.view_din_button_des).setOnClickListener(
                v -> startActivity(new Intent(HomeDin.this, HomeDes.class))
        );
        findViewById(R.id.view_din_button_acc).setOnClickListener(
                v -> startActivity(new Intent(HomeDin.this, HomeAcc.class))
        );
        findViewById(R.id.view_din_button_tra).setOnClickListener(
                v -> startActivity(new Intent(HomeDin.this, HomeTra.class))
        );
    }
}