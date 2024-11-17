package com.example.sprint1.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.viewmodel.MainViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class HomeLog extends AppCompatActivity {

    private final MainViewModel mainViewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_log);

        this.buttonVisualization();
        this.listCollaboratorsAndNotes();
        this.buttonAddNote();
        this.buttonAddCollaborator();
        this.buttonNavigationBar();
    }

    private void buttonVisualization() {
        PieChart pieChart = findViewById(R.id.pie_chart);

        findViewById(R.id.Home_Log_Visualization).setOnClickListener(v -> {
            if (pieChart.getVisibility() == View.VISIBLE) {
                pieChart.setVisibility(View.GONE);
            } else {
                pieChart.setVisibility(View.VISIBLE);
            }
        });

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);

        pieChart.setCenterText("Vacation Overview");
        pieChart.setCenterTextSize(16f);

        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.getLegend().setTextSize(10f);

        pieChart.animateY(1000, Easing.EaseInOutQuad);

        mainViewModel.getVacation(vacationData -> {
            if (vacationData != null) {
                mainViewModel.calVacation(arrangedDaysStr -> updateVisualization(
                        Integer.parseInt(arrangedDaysStr),
                        Integer.parseInt(Objects.requireNonNull(vacationData.get("duration")))
                ));
            }
        });
    }

    private void updateVisualization(int arrangedDays, int totalDays) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Calculate percentages
        float arrangedPercentage = (float) arrangedDays / totalDays * 100;
        float remainingPercentage = 100 - arrangedPercentage;

        // Add Pie Entries with only values and percentages for the pie chart
        entries.add(new PieEntry(
                arrangedDays,
                String.format(Locale.US,
                        "Arranged %d days (%.1f%%)",
                        arrangedDays,
                        arrangedPercentage)
        ));
        entries.add(new PieEntry(
                totalDays - arrangedDays,
                String.format(Locale.US,
                        "Remained %d days (%.1f%%)",
                        totalDays - arrangedDays,
                        remainingPercentage)
        ));

        // Create and configure the PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(7f);

        // Set colors for the slices
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(66, 133, 244)); // Blue
        colors.add(Color.rgb(219, 68, 55));  // Red
        dataSet.setColors(colors);

        // Create PieData and configure text properties
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(14f);
        pieData.setValueTextColor(Color.BLACK);

        // Remove value labels from PieData (since labels are directly on the pie)
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Return an empty string for data value labels
                return "";
            }
        });

        // Assign the PieData to the chart and refresh
        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

    private void listCollaboratorsAndNotes() {
        LinearLayout container = findViewById(R.id.collaborators_container);
        container.removeAllViews(); // Clear existing views

        mainViewModel.getNote(notes -> {
            for (Map.Entry<String, String> entry : notes.entrySet()) {
                String collaboratorName = entry.getKey();
                String note = entry.getValue();

                // Inflate the card layout dynamically
                View cardView = getLayoutInflater().inflate(
                        R.layout.home_log_card, container, false
                );

                // Set collaborator name and note
                TextView nameText = cardView.findViewById(R.id.collaborator_name_text);
                TextView noteText = cardView.findViewById(R.id.collaborator_note_text);

                nameText.setText(collaboratorName);
                noteText.setText(note);

                // Add the card to the container
                container.addView(cardView);
            }
        });
    }

    private void buttonAddNote() {
        // Find the "Add Note" button by its ID
        findViewById(R.id.Home_Log_AddNote).setOnClickListener(v -> {
            // Create an EditText to input the note
            EditText noteInput = new EditText(HomeLog.this);
            // Create an AlertDialog to input the note
            new AlertDialog.Builder(HomeLog.this)
                    .setTitle("Add Note")
                    .setView(noteInput)
                    .setPositiveButton("Save", (dialog, which) -> {
                        // Get the note text from the input field
                        String note = noteInput.getText().toString().trim();
                        if (!note.isEmpty()) {
                            // Save the note using the MainViewModel
                            this.mainViewModel.addNote(note, success -> {
                                if (success) {
                                    listCollaboratorsAndNotes();
                                }
                            });
                        } else {
                            Toast.makeText(
                                    HomeLog.this, "Note cannot be empty.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .setNegativeButton("Cancel", null) // Dismiss the dialog on "Cancel"
                    .show();
        });
    }

    private void buttonAddCollaborator() {
        findViewById(R.id.Home_Log_AddCollaborator).setOnClickListener(v -> {
            // Step 1: Get non-collaborators from MainViewModel
            this.mainViewModel.getNonCollaborator(nonCollaborators -> {
                if (nonCollaborators == null || nonCollaborators.isEmpty()) {
                    return;
                }

                // Step 2: Convert the list to an array for the dialog
                String[] usersArray = nonCollaborators.toArray(new String[0]);

                // Step 3: Display a dialog for selecting a user
                new AlertDialog.Builder(HomeLog.this)
                        .setTitle("Add Collaborator")
                        .setItems(usersArray, (dialog, which) -> {
                            String selectedUser = usersArray[which];

                            // Step 4: Add the selected user as a collaborator
                            this.mainViewModel.addCollaborator(selectedUser, success -> {
                                if (success) {
                                    this.listCollaboratorsAndNotes();
                                }
                            });
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
    }

    private void buttonNavigationBar() {
        findViewById(R.id.view_log_button_des).setOnClickListener(
                v -> startActivity(new Intent(HomeLog.this, HomeDes.class))
        );
        findViewById(R.id.view_log_button_din).setOnClickListener(
                v -> startActivity(new Intent(HomeLog.this, HomeDin.class))
        );
        findViewById(R.id.view_log_button_acc).setOnClickListener(
                v -> startActivity(new Intent(HomeLog.this, HomeAcc.class))
        );
        findViewById(R.id.view_log_button_tra).setOnClickListener(
                v -> startActivity(new Intent(HomeLog.this, HomeTra.class))
        );
    }
}
