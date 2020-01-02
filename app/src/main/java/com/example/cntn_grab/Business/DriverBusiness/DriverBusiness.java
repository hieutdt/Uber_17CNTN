package com.example.cntn_grab.Business.DriverBusiness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cntn_grab.Data.Driver;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Trip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DriverBusiness {
    private static DriverBusiness mInstance;

    private Driver driver;

    private DriverBusiness() {
        driver = new Driver();
    }

    public static DriverBusiness getInstance() {
        if (mInstance == null)
            mInstance = new DriverBusiness();
        return mInstance;
    }


    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
