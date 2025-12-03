package com.example.hikingapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //DEFINING ALL FIELDS PRESENT IN THE LAYOUT
        Button submit = findViewById(R.id.btnSubmit);
        EditText name = (EditText) findViewById(R.id.hikeEdit);
        EditText location = (EditText) findViewById(R.id.locationEdit);
        EditText date = (EditText) findViewById(R.id.dateEdit);
        CheckBox parking = findViewById(R.id.checkboxPark);
        EditText length = (EditText) findViewById(R.id.lengthEdit);
        Spinner difficulty = findViewById(R.id.editDifficultySpinner);
        EditText description = (EditText) findViewById(R.id.descriptionEdit);
        CheckBox children = findViewById(R.id.childrenCheckbox);

        //POPULATING SPINNER WITH THE STRINGS DEFINED IN STRINGS.XML
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulties,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapter);

        // SETTING UP CLICK LISTENER FOR DATE, WHEN CLICKED, OPEN A DATE PICKER DIALOGUE.
        date.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    InputActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Month is 0-indexed, add +1
                        String stringdate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        date.setText(stringdate);
                    },
                    year, month, day
            );

            datePicker.show();
        });


        // WHEN SUBMIT IS CLICKED, TAKE THE VALUES FROM TEXT FIELDS, VALIDATE (FOR REQUIRED FIELDS)
        // AND INSERT INTO DATABASE. IF SUCCESSFUL, GO TO LIST ACTIVITY.
        submit.setOnClickListener(v -> {
            String hikeName = name.getText().toString().trim();
            String hikeLocation = location.getText().toString().trim();
            String hikeDate = date.getText().toString().trim();
            boolean hasParking = parking.isChecked();
            String hikeLength = length.getText().toString().trim();
            int lengthNumber = 0;
            String selectedDifficulty = difficulty.getSelectedItem().toString().trim();
            String hikeDescription = description.getText().toString().trim();
            boolean hasChildren = children.isChecked();

            // Validation
            if (hikeName.isEmpty()) {
                name.setError("Required.");
                return;
            }
            if (hikeDate.isEmpty()) {
                date.setError("Required.");
                return;
            }
            if (hikeLocation.isEmpty()) {
                location.setError("Required.");
                return;
            }
            if (hikeLength.isEmpty()) {
                length.setError("Required.");
                return;
            } else {
                lengthNumber = Integer.parseInt(hikeLength);
            }

            // Insert into database
            DatabaseHelper db = new DatabaseHelper(this);
            boolean inserted = db.insertHike(
                    hikeName, hikeLocation, hikeDate, lengthNumber,
                    selectedDifficulty, hikeDescription, hasParking, hasChildren
            );

            if (inserted) {
                Toast.makeText(this, "Hike saved!", Toast.LENGTH_SHORT).show();

                // Go directly to ListActivity
                Intent i = new Intent(InputActivity.this, ListActivity.class);
                startActivity(i);

                // Optionally finish InputActivity so user can't go back to it
                finish();
            } else {
                Toast.makeText(this, "Save failed.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}