package com.example.hikingapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditObservationActivity extends AppCompatActivity {

    private EditText inputObservation, inputTime, inputComments;
    private Button btnSave, btnCancel;

    private DatabaseHelper db;
    private int obsId;
    private Observation observation; // The object that's being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        db = new DatabaseHelper(this);

        // Get observation ID
        obsId = getIntent().getIntExtra("obsId", -1);
        if (obsId == -1) {
            Toast.makeText(this, "Error: Invalid observation", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get UI elements
        inputObservation = findViewById(R.id.inputEditObservation);
        inputTime = findViewById(R.id.inputEditTime);
        inputComments = findViewById(R.id.inputEditComments);

        btnSave = findViewById(R.id.btnSaveObservationEdit);
        btnCancel = findViewById(R.id.btnCancelObservationEdit);

        loadObservation();
        setupButtons();
    }

    private void loadObservation() {
        observation = db.getObservationById(obsId);  // Use DbHelper to fetch the observation by ID.

        if (observation == null) {
            Toast.makeText(this, "Error: Observation not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields with the observation's data
        inputObservation.setText(observation.getObservationText());
        inputTime.setText(observation.getTime());
        inputComments.setText(observation.getComments());
    }


    //Adding observation button.
    private void setupButtons() {
        btnSave.setOnClickListener(v -> {
            //Fetch text from the fields.
            String obsText = inputObservation.getText().toString().trim();
            String time = inputTime.getText().toString().trim();
            String comments = inputComments.getText().toString().trim();

            //Validation for empty fields.
            if (obsText.isEmpty()) {
                Toast.makeText(this, "Observation cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            //Uses the updateObservation method in DbHelper to add a new observation entry.
            boolean success = db.updateObservation(obsId, obsText, time, comments);

            if (success) {
                Toast.makeText(this, "Observation updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
