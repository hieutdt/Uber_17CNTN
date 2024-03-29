package com.example.cntn_grab.Business.PassengerBusiness;

import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Passenger;

public class PassengerBusiness {
    private static PassengerBusiness mInstance;
    private Passenger mPassenger;

    private PassengerBusiness() {
        mPassenger = new Passenger();
    }

    public static PassengerBusiness getInstance() {
        if (mInstance == null)
            mInstance = new PassengerBusiness();

        return mInstance;
    }

    public Location getPassengerLocation() {
        return mPassenger.getLocation();
    }

    public void setPassengerLocation(double lat, double lng) {
        mPassenger.getLocation().lat = lat;
        mPassenger.getLocation().lng = lng;
    }

    public void setPassengerLocationName(String name) {
        mPassenger.getLocation().name = name;
    }

    public void setPassenger(Passenger passenger) {
        mPassenger = passenger;
    }

    public void setPassengerTripID(String tripID) {
        mPassenger.setTripId(tripID);
    }

    public void setPassengerState(String state) {
        mPassenger.setState(state);
    }

    public Passenger getPassenger() {
        return mPassenger;
    }

}
