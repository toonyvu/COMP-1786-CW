package com.example.hikingapplication;

public class Observation {

    public int id;
    public int hikeId;
    public String observationText;
    public String time;
    public String comments;

    public Observation(int id, int hikeId, String observationText, String time, String comments) {
        this.id = id;
        this.hikeId = hikeId;
        this.observationText = observationText;
        this.time = time;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public int getHikeId() {
        return hikeId;
    }

    public String getObservationText() {
        return observationText;
    }

    public String getTime() {
        return time;
    }

    public String getComments() {
        return comments;
    }
}