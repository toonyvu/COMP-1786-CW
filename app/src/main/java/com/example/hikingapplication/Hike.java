package com.example.hikingapplication;

public class Hike {
    public int id;
    public String name;
    public String location;
    public String date;
    public String difficulty;
    public String description;
    public int length;
    public boolean park;
    public boolean children;

    public Hike() {
        // Empty constructor needed for parsing
    }

    public Hike(int id, String name, String location, String date,
                String difficulty, String description, int length,
                boolean park, boolean children) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.difficulty = difficulty;
        this.description = description;
        this.length = length;
        this.park = park;
        this.children = children;
    }

    public int getId() {
        return id;
    }
}
