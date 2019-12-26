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

    public void setPassenger(Passenger passenger) {
        mPassenger = passenger;
    }

    public Passenger getPassenger() {
        return mPassenger;
    }

}
