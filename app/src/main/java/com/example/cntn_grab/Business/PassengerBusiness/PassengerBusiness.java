package com.example.cntn_grab.Business.PassengerBusiness;

import com.example.cntn_grab.Data.Passenger;

public class PassengerBusiness {
    private static PassengerBusiness mInstance;
    private Passenger mPassenger;

    private PassengerBusiness() {}

    public static PassengerBusiness getInstance() {
        if (mInstance == null)
            mInstance = new PassengerBusiness();

        return mInstance;
    }



}
