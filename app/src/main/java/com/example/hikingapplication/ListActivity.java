package com.example.hikingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HikeAdapter adapter;
    private ArrayList<Hike> hikeList;
    private DatabaseHelper db;
    private Button btnReset;

    private Spinner spinnerSearchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        spinnerSearchField = findViewById(R.id.spinnerSearchField);

        //Load spinner items from strings.xml, the searching criterion: Name, location, length, etc.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.search_fields)
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Attach adapter to spinner
        spinnerSearchField.setAdapter(spinnerAdapter);
        EditText inputSearch = findViewById(R.id.inputSearch);

        //EVERY TIME USER TYPES INTO THE SEARCHBAR, THIS WILL UPDATE THE RESULTS
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHikes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerHikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> confirmReset());

        loadHikes();

        // RESET BUTTON. WHEN CLICKED, CALLS DATABASE QUERY TO DELETE ALL HIKES IN THE DATABASE.
        btnReset.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset Database")
                    .setMessage("Are you sure you want to delete all hikes?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteAll();
                        Toast.makeText(this, "All hikes deleted", Toast.LENGTH_SHORT).show();
                        loadHikes();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // ADD HIKE BUTTON. PRESSING WILL TAKE USER BACK TO INPUTACTIVITY.
        Button btnAddHike = findViewById(R.id.btnAddHike);
        btnAddHike.setOnClickListener(v -> {
            Intent i = new Intent(ListActivity.this, InputActivity.class);
            startActivity(i);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikes();
    }

    //Uses database query too get all hikes inside the database, and populate them into the hikeAdapter
    private void loadHikes() {
        hikeList = db.getAllHikes();

        adapter = new HikeAdapter(hikeList, new HikeAdapter.OnHikeClickListener() {
            @Override
            //Edit button. Takes users to the EditActivity when pressed, and passes in the HikeId to load the hike.
            public void onEdit(Hike hike) {
                Intent i = new Intent(ListActivity.this, EditActivity.class);
                i.putExtra("hikeId", hike.id);
                startActivity(i);
            }

            @Override
            //Deleting a hike.
            public void onDelete(Hike hike) {
                confirmDelete(hike);
            }

            @Override
            // Observation button. Takes user to observation screen, and passes in the hikeId to load.
            public void onViewObservations(Hike hike) {
                Intent i = new Intent(ListActivity.this, ObservationListActivity.class);
                i.putExtra("hikeId", hike.id);
                startActivity(i);
            }
        });

        recyclerView.setAdapter(adapter);
    }


    //Deletes the hike from the database based on the id of the hike.
    private void confirmDelete(Hike hike) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete this hike?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    //Calls query to delete hike from database.
                    db.deleteHike(hike.id);
                    Toast.makeText(this, "Hike deleted", Toast.LENGTH_SHORT).show();
                    loadHikes();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //Delete ALL hikes from the database.
    private void confirmReset() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Database")
                .setMessage("Are you sure you want to delete ALL hikes?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteAll();
                    hikeList.clear();
                    adapter.updateList(hikeList);
                    Toast.makeText(this, "All hikes deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void filterHikes(String query) {
        //Get the searching criteria based on the current selection in the spinner.
        String selectedField = spinnerSearchField.getSelectedItem().toString();

        //Creates a list of hikes that satisfies the criteria.
        ArrayList<Hike> filteredList = new ArrayList<>();

        //Loops through the HikeList
        for (Hike hike : hikeList) {

            //Uses a switch operator depending on the field user want to search in.
            switch (selectedField) {
                //If hike name matches the text typed in the searchbar, add it to the list.
                case "Name":
                    if (hike.name.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(hike);
                    }
                    break;
                //If hike location matches the text typed in the searchbar, add it to the list.

                case "Location":
                    if (hike.location.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(hike);
                    }
                    break;
                case "Date":
                    if (hike.date.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(hike);
                    }
                    break;

                case "Length":
                    if (String.valueOf(hike.length).contains(query)) {
                        filteredList.add(hike);
                    }
                    break;
            }
        }

        //Updating the list, only showing the items that were added to the filtered list.
        adapter.updateList(filteredList);
    }


}
