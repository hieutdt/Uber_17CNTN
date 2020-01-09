package com.example.cntn_grab.Business.TripBusiness;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Passenger;
import com.example.cntn_grab.Data.Trip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TripBusiness {
    private static TripBusiness mInstance;
    private Trip mTrip;

    private CreateNewTripListener mCreateNewTripListener;
    private DatabaseReference mDatabaseRef;

    private TripBusiness() {
    }

    public static TripBusiness getInstance() {
        if (mInstance == null)
            mInstance = new TripBusiness();

        return mInstance;
    }

    public Trip getTrip() {
        return mTrip;
    }

    public void setTrip(Trip trip) {
        mTrip = trip;
    }

    public void createNewTrip(Passenger passenger, Location origin, Location destination, int distance, int amount) {
        mCreateNewTripListener.createNewTripDidStart();

        final Trip trip = new Trip(passenger.getId(), "", origin, destination, Long.valueOf(distance), Long.valueOf(amount), "FIND_DRIVER", passenger.getPhoneNumber());

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("trips");
        mDatabaseRef.child(trip.getTripID()).setValue(trip)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mCreateNewTripListener.createNewTripDidEnd(true, trip.getTripID());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TON HIEU", e.toString());
                        mCreateNewTripListener.createNewTripDidEnd(false, "");
                    }
                });

        mTrip = trip;
    }

    public void setCreateNewTripListener(CreateNewTripListener listener) {
        mCreateNewTripListener = listener;
    }

    public void updateTripDriver(String driverID) {
        mTrip.setDriverID(driverID);
        mTrip.setState("ON_TRIP");
    }
}
