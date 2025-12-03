package com.example.hikingapplication;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText hikeEdit, locationEdit, dateEdit, lengthEdit, descriptionEdit;
    Spinner difficultySpinner;
    CheckBox parkingCheck, childrenCheck;
    Button btnUpdate;

    DatabaseHelper db;
    int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = new DatabaseHelper(this);

        // Get hikeId from Intent
        hikeId = getIntent().getIntExtra("hikeId", -1);
        if (hikeId == -1) {
            Toast.makeText(this, "Invalid Hike ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        hikeEdit = findViewById(R.id.hikeEdit);
        locationEdit = findViewById(R.id.locationEdit);
        dateEdit = findViewById(R.id.dateEdit);
        lengthEdit = findViewById(R.id.lengthEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        parkingCheck = findViewById(R.id.checkboxPark);
        childrenCheck = findViewById(R.id.childrenCheckbox);
        difficultySpinner = findViewById(R.id.editDifficultySpinner);
        btnUpdate = findViewById(R.id.btnSubmit);

        // Load spinner values from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulties,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        // Load hike details
        loadHikeDetails(adapter);

        btnUpdate.setOnClickListener(v -> updateHike());
    }

    private void loadHikeDetails(ArrayAdapter<CharSequence> adapter) {
        Hike hike = db.getHikeById(hikeId);

        if (hike == null) {
            Toast.makeText(this, "Hike not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        hikeEdit.setText(hike.name);
        locationEdit.setText(hike.location);
        dateEdit.setText(hike.date);
        lengthEdit.setText(String.valueOf(hike.length));
        descriptionEdit.setText(hike.description);
        parkingCheck.setChecked(hike.park);
        childrenCheck.setChecked(hike.children);

        // Set spinner selection safely
        if (hike.difficulty != null) {
            int position = adapter.getPosition(hike.difficulty);
            if (position >= 0) {
                difficultySpinner.setSelection(position);
            }
        }
    }

    private void updateHike() {

        // --- Collect input ---
        String name = hikeEdit.getText().toString().trim();
        String location = locationEdit.getText().toString().trim();
        String date = dateEdit.getText().toString().trim();
        String lengthStr = lengthEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();
        boolean park = parkingCheck.isChecked();
        boolean children = childrenCheck.isChecked();

        // --- VALIDATION FOR REQUIRED FIELDS ---

        if (name.isEmpty()) {
            hikeEdit.setError("Hike name is required");
            hikeEdit.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            locationEdit.setError("Location is required");
            locationEdit.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            dateEdit.setError("Date is required");
            dateEdit.requestFocus();
            return;
        }

        // Parking is a required field (Yes/No)
        // With a single checkbox, unchecked = "No", checked = "Yes"

        if (lengthStr.isEmpty()) {
            lengthEdit.setError("Length is required");
            lengthEdit.requestFocus();
            return;
        }

        int length;
        try {
            length = Integer.parseInt(lengthStr);
        } catch (NumberFormatException e) {
            lengthEdit.setError("Length must be a number");
            lengthEdit.requestFocus();
            return;
        }

        // Difficulty must also be selected (Required)
        if (difficultySpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Difficulty is required", Toast.LENGTH_SHORT).show();
            return;
        }

        String difficulty = difficultySpinner.getSelectedItem().toString();

        // --- SAVE UPDATED VALUES INTO DATABASE ---
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_NAME, name);
        cv.put(DatabaseHelper.COL_LOCATION, location);
        cv.put(DatabaseHelper.COL_DATE, date);
        cv.put(DatabaseHelper.COL_LENGTH, length);
        cv.put(DatabaseHelper.COL_DIFFICULTY, difficulty);
        cv.put(DatabaseHelper.COL_DESCRIPTION, description);
        cv.put(DatabaseHelper.COL_PARK, park ? 1 : 0);
        cv.put(DatabaseHelper.COL_CHILDREN, children ? 1 : 0);

        if (db.updateHike(hikeId, cv)) {
            Toast.makeText(this, "Hike updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }

}
