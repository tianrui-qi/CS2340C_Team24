package com.example.sprint1.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;


public class HomeDes extends AppCompatActivity {

    private final MainViewModel mainViewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_des);

        this.buttonLogTravel();
        this.formLogTravel();
        this.buttonCancel();
        this.buttonCalculate();
        this.buttonNavigationBar();
    }

    private void buttonLogTravel() {
        View formLogTravel = findViewById(R.id.HomeDestination_LogTravel_Form);

        findViewById(R.id.HomeDestination_LogTravel).setOnClickListener(v -> {
            if (formLogTravel.getVisibility() == View.VISIBLE) {
                formLogTravel.setVisibility(View.GONE);
            } else {
                formLogTravel.setVisibility(View.VISIBLE);
            }
        });
    }

    private void formLogTravel() {
        EditText travelLocation = findViewById(R.id.HomeDestination_LogTravel_TravelLocation);
        EditText startDate = findViewById(R.id.HomeDestination_LogTravel_StartDate);
        EditText endDate = findViewById(R.id.HomeDestination_LogTravel_EndDate);

        findViewById(R.id.HomeDestination_LogTravel_Submit).setOnClickListener(v -> {
            mainViewModel.destLogTravel(
                    travelLocation.getText().toString().trim(),
                    startDate.getText().toString().trim(),
                    endDate.getText().toString().trim(),
                    success -> {
                        if (success) {
                            travelLocation.setText("");
                            startDate.setText("");
                            endDate.setText("");
                        }
                    }
            );
        });
    }

    private void buttonCancel() {
        View formLogTravel = findViewById(R.id.HomeDestination_LogTravel_Form);

        findViewById(R.id.HomeDestination_LogTravel_Cancel).setOnClickListener(v -> {
            formLogTravel.setVisibility(View.GONE);
        });
    }

    private void buttonCalculate() {
        View formCalculate = findViewById(R.id.HomeDestination_Calculate_Form);

        findViewById(R.id.HomeDestination_Calculate).setOnClickListener(v -> {
            if (formCalculate.getVisibility() == View.VISIBLE) {
                formCalculate.setVisibility(View.GONE);
            } else {
                formCalculate.setVisibility(View.VISIBLE);
            }
        });
    }

    private void formCalculate() {

    }

    private void listDestination() {

    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_des_button_log).setOnClickListener(v -> {
            startActivity(new Intent(HomeDes.this, HomeLog.class));
        });
        findViewById(R.id.view_des_button_din).setOnClickListener(v -> {
            startActivity(new Intent(HomeDes.this, HomeDin.class));
        });
        findViewById(R.id.view_des_button_acc).setOnClickListener(v -> {
            startActivity(new Intent(HomeDes.this, HomeAcc.class));
        });
        findViewById(R.id.view_des_button_tra).setOnClickListener(v -> {
            startActivity(new Intent(HomeDes.this, HomeTra.class));
        });
    }
}
