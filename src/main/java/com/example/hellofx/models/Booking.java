package com.example.hellofx.models;

public class Booking {
    private int booking_id;
    private int user_id;
    private int trip_id;
    private int seat_number;
    private String booking_time;

    public Booking() {
    }

    public Booking(int booking_id, int user_id, int trip_id, int seat_number, String booking_time) {
        this.booking_id = booking_id;
        this.user_id = user_id;
        this.trip_id = trip_id;
        this.seat_number = seat_number;
        this.booking_time = booking_time;
    }

    public Booking(Booking booking) {
        this.booking_id = booking.booking_id;
        this.user_id = booking.user_id;
        this.trip_id = booking.trip_id;
        this.seat_number = booking.seat_number;
        this.booking_time = booking.booking_time;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }
}
