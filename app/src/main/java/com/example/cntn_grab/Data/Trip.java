package com.example.cntn_grab.Data;

import com.example.cntn_grab.Helpers.StringHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class Trip {
    private String tripID;
    private String passengerID;
    private String driverID;
    private Location origin;
    private Location destination;
    private int distance;
    private int amount;


    public Trip() {
        tripID = StringHelper.randomString(10);
    }

    public Trip(String passengerID, String driverID, Location origin, Location destination, int distance, int amount) {
        tripID = StringHelper.randomString(10);
        this.passengerID = passengerID;
        this.driverID = driverID;
        this.destination = destination;
        this.origin = origin;
        this.distance = distance;
        this.amount = amount;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        tripID = tripID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
