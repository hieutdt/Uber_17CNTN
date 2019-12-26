package com.example.cntn_grab.Data;

public class Passenger extends User {
    private String state;
    private String tripId;
    private Location location;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
