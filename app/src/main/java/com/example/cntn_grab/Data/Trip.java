package com.example.cntn_grab.Data;

import com.example.cntn_grab.Helpers.StringHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class Trip {
    private String tripID;
    private String passengerID;
    private String driverID;
    private double originLat;
    private double originLng;
    private String originName;
    private double destinationLat;
    private double destinationLng;
    private String destinationName;
    private Long distance;
    private Long amount;
    private double driverLat;
    private double driverLng;
    private String state;
    private String passengerPhoneNumber;


    public Trip() {
        tripID = StringHelper.randomString(20);
        this.state = "FIND_DRIVER";
    }

    public Trip(String passengerID, String driverID, Location origin, Location destination, Long distance, Long amount, String state, String phoneNumber) {
        tripID = StringHelper.randomString(10);
        this.passengerID = passengerID;
        this.driverID = driverID;
        this.destinationLat = destination.lat;
        this.destinationLng = destination.lng;
        this.destinationName = destination.name;
        this.originLat = origin.lat;
        this.originLng = origin.lng;
        this.originName = origin.name;
        this.distance = distance;
        this.amount = amount;
        this.driverLat = 0;
        this.driverLng = 0;
        this.state = state;
        this.passengerPhoneNumber = phoneNumber;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
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

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(double driverLat) {
        this.driverLat = driverLat;
    }

    public double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(double driverLng) {
        this.driverLng = driverLng;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassengerPhoneNumber() {
        return passengerPhoneNumber;
    }

    public void setPassengerPhoneNumber(String passengerPhoneNumber) {
        this.passengerPhoneNumber = passengerPhoneNumber;
    }
}
