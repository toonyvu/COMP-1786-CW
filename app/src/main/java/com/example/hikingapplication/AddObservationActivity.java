package com.example.hikingapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private EditText observationText, timeText, commentsText;
    private Button saveBtn, cancelBtn;

    private DatabaseHelper db;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        //Get the databasehelper and its methods.
        db = new DatabaseHelper(this);

        //Get the hike ID that was passed into by using getIntExtra.
        hikeId = getIntent().getIntExtra("hikeId", -1);
        if (hikeId == -1) {
            Toast.makeText(this, "Error: No hike selected.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Defining the inputfields and buttons.
        observationText = findViewById(R.id.inputObservation);
        timeText = findViewById(R.id.inputTime);
        commentsText = findViewById(R.id.inputComments);
        saveBtn = findViewById(R.id.saveObservationBtn);
        cancelBtn = findViewById(R.id.cancelObservationBtn);

        // Auto-fill current date & time
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        timeText.setText(currentDateTime);

        saveBtn.setOnClickListener(v -> saveObservation());
        cancelBtn.setOnClickListener(v -> finish());
    }

    //When save button is clicked, get fields that user typed in, create an observation object and add its fields into the database.
    private void saveObservation() {
        String observation = observationText.getText().toString().trim();
        String time = timeText.getText().toString().trim();
        String comments = commentsText.getText().toString().trim();

        if (observation.isEmpty()) {
            observationText.setError("Required");
            return;
        }

        if (time.isEmpty()) {
            timeText.setError("Required");
            return;
        }

        // Create observation object
        Observation obs = new Observation(
                0,          // ID (auto-generated)
                hikeId,
                observation,
                time,
                comments
        );

        long result = db.addObservation(
                obs.hikeId,
                obs.observationText,
                obs.time,
                obs.comments
        );

        //Error handling.
        if (result != -1) {
            Toast.makeText(this, "Observation added!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save observation.", Toast.LENGTH_SHORT).show();
        }
    }
}
