package com.example.hellofx.models;

public class Trip {
    private int trip_id;
    private String from;
    private String to;
    private String date;
    private String time;

    public Trip() {
    }

    public Trip(int trip_id, String from, String to, String date, String time) {
        this.trip_id = trip_id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
    }

    public Trip(String from, String to, String date, String time) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
    }

    public Trip(Trip tmpTrip) {
        this.trip_id = tmpTrip.trip_id;
        this.from = tmpTrip.from;
        this.to = tmpTrip.to;
        this.date = tmpTrip.date;
        this.time = tmpTrip.time;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
