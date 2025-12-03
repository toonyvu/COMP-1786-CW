package com.example.hikingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //DEFINING THE HIKES DATABASE
    public static final String DATABASE_NAME = "hikes.db";
    public static final String HIKE_TABLE = "hike_table";

    //DEFINING FIELDS IN THE HIKES DATABASE
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_LOCATION = "location";
    public static final String COL_DATE = "date";
    public static final String COL_LENGTH = "length";
    public static final String COL_DIFFICULTY = "difficulty";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PARK = "park";
    public static final String COL_CHILDREN = "children";


    //DEFINING THE OBSERVATION TABLE
    private static final String TABLE_OBSERVATION = "observations";

    //DEFINING THE OBSERVATION FIELDS
    private static final String COL_OBS_ID = "obs_id";
    private static final String COL_OBS_HIKE_ID = "hike_id";
    private static final String COL_OBS_TEXT = "observation";
    private static final String COL_OBS_TIME = "time";
    private static final String COL_OBS_COMMENTS = "comments";


    //String to create observation table.
    String createObservationsTable =
            "CREATE TABLE " + TABLE_OBSERVATION + " (" +
                    COL_OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_OBS_HIKE_ID + " INTEGER, " +
                    COL_OBS_TEXT + " TEXT NOT NULL, " +
                    COL_OBS_TIME + " TEXT NOT NULL, " +
                    COL_OBS_COMMENTS + " TEXT, " +
                    "FOREIGN KEY(" + COL_OBS_HIKE_ID + ") REFERENCES hikes(id) ON DELETE CASCADE" +
                    ");";


    // When you change the database schema, you must increment the database version.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + HIKE_TABLE + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT, " +
                        COL_LOCATION + " TEXT, " +
                        COL_DATE + " TEXT, " +
                        COL_LENGTH + " INTEGER, " +
                        COL_DIFFICULTY + " TEXT, " +
                        COL_DESCRIPTION + " TEXT, " +
                        COL_PARK + " INTEGER, " +
                        COL_CHILDREN + " INTEGER)"
        );

        db.execSQL(createObservationsTable);
    }


    @Override
    // Drop the table when the database version is upgraded.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS observations");
        db.execSQL("DROP TABLE IF EXISTS " + HIKE_TABLE);
        onCreate(db);
    }

    // Insert a record
    public boolean insertHike(String name, String location, String date, int length,
                              String difficulty, String description, boolean park, boolean children) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, name);
        cv.put(COL_LOCATION, location);
        cv.put(COL_DATE, date);
        cv.put(COL_LENGTH, length);
        cv.put(COL_DIFFICULTY, difficulty);
        cv.put(COL_DESCRIPTION, description);
        cv.put(COL_PARK, park ? 1 : 0);
        cv.put(COL_CHILDREN, children ? 1 : 0);

        return db.insert(HIKE_TABLE, null, cv) != -1;
    }

    // Get all hikes
    // Get all hikes as ArrayList<Hike>
    public ArrayList<Hike> getAllHikes() {
        ArrayList<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + HIKE_TABLE + " ORDER BY " + COL_ID + " DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                hike.name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                hike.location = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION));
                hike.date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
                hike.length = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LENGTH));
                hike.difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIFFICULTY));
                hike.description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                hike.park = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PARK)) == 1;
                hike.children = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CHILDREN)) == 1;

                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return hikeList;
    }


    // Update hike
    public boolean updateHike(int id, ContentValues cv) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(HIKE_TABLE, cv, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Delete one
    public boolean deleteHike(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(HIKE_TABLE, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Delete everything
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HIKE_TABLE, null, null);
    }

    //Get the specific hike by querying the database by Id and returning that hike object.
    public Hike getHikeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HIKE_TABLE + " WHERE id=?", new String[]{String.valueOf(id)});
        Hike hike = null;
        if (cursor.moveToFirst()) {
            hike = new Hike();
            hike.id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            hike.name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
            hike.location = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION));
            hike.date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            hike.length = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LENGTH));
            hike.difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIFFICULTY));
            hike.description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
            hike.park = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PARK)) == 1;
            hike.children = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CHILDREN)) == 1;
        }
        cursor.close();
        return hike;
    }

    // Adding an observation.
    public long addObservation(int hikeId, String observation, String time, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_OBS_HIKE_ID, hikeId);
        values.put(COL_OBS_TEXT, observation);
        values.put(COL_OBS_TIME, time);
        values.put(COL_OBS_COMMENTS, comments);

        return db.insert(TABLE_OBSERVATION, null, values);
    }

    // Get all observations for a hike.

    public ArrayList<Observation> getObservationsForHike(int hikeId) {
        ArrayList<Observation> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //Querying the database to get all observations based on the hikeId.
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_OBSERVATION +
                        " WHERE " + COL_OBS_HIKE_ID + "=? " +
                        " ORDER BY " + COL_OBS_ID + " DESC",
                new String[]{String.valueOf(hikeId)}
        );

        if (cursor.moveToFirst()) {
            do {
                Observation obs = new Observation(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_OBS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_OBS_HIKE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_COMMENTS))
                );

                //Adding the observation to the list.
                list.add(obs);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    //Deleting the observation.
    public int deleteObservation(int obsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OBSERVATION, COL_OBS_ID + "=?", new String[]{String.valueOf(obsId)});
    }

    //Getting an observation by it's ID. Used when editing individual observations.
    public Observation getObservationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_OBSERVATION,
                null,
                COL_OBS_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            Observation obs = new Observation(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_OBS_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_OBS_HIKE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_TEXT)),   // observationText
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_TIME)),   // time
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_COMMENTS)) // comments
            );
            cursor.close();
            return obs;
        }


        cursor.close();
        return null;
    }

    //Update observation based on the values the user entered.
    public boolean updateObservation(int id, String observation, String time, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_OBS_TEXT, observation);
        values.put(COL_OBS_TIME, time);
        values.put(COL_OBS_COMMENTS, comments);

        int rows = db.update(TABLE_OBSERVATION, values, COL_OBS_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

}
