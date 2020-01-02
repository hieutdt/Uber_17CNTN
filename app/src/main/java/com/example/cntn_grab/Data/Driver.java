package com.example.cntn_grab.Data;

public class Driver extends User {
    private String state;
    private Location location;
    private String id;

    public Driver() {
        location = new Location();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLat(double lat) {
        this.location.lat = lat;
    }

    public void setLng(double lng) {
        this.location.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
