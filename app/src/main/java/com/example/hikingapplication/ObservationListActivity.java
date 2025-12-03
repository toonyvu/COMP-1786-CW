package com.example.hikingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObservationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ObservationAdapter adapter;
    private ArrayList<Observation> observationList;

    private DatabaseHelper db;
    private int hikeId;

    private Button btnAddObservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        db = new DatabaseHelper(this);

        // Get hikeId from previous screen
        hikeId = getIntent().getIntExtra("hikeId", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error: No hike selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        recyclerView = findViewById(R.id.recyclerObservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        btnAddObservation = findViewById(R.id.btnAddObservation);

        //Adding an observation. Pressing on the button takes the user to the add observation screen based on hike ID.
        btnAddObservation.setOnClickListener(v -> {
            Intent i = new Intent(this, AddObservationActivity.class);
            i.putExtra("hikeId", hikeId);
            startActivity(i);
        });

        loadObservations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadObservations(); // Reload list when returning to this page
    }

    private void loadObservations() {
        //Get all observations of a hike and load it into the observation adapter.
        observationList = db.getObservationsForHike(hikeId);

        adapter = new ObservationAdapter(observationList, new ObservationAdapter.OnObservationClickListener() {
            @Override
            //Edit button, clicking on it redirects user to the observation edit based on ID.
            public void onEdit(Observation obs) {
                Intent i = new Intent(ObservationListActivity.this, EditObservationActivity.class);
                i.putExtra("obsId", obs.id);
                startActivity(i);
            }

            @Override
            public void onDelete(Observation obs) {
                confirmDelete(obs);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    // Pressing on delete observation button runs this method that deletes the observation based on ID.
    private void confirmDelete(Observation obs) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Are you sure you want to delete this observation?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteObservation(obs.id);
                    Toast.makeText(this, "Observation deleted", Toast.LENGTH_SHORT).show();
                    loadObservations();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
