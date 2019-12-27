package com.example.cntn_grab.Screens;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cntn_grab.Business.PassengerBusiness.PassengerBusiness;
import com.example.cntn_grab.Business.TripBusiness.TripBusiness;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Trip;
import com.example.cntn_grab.Helpers.GIFImageView;
import com.example.cntn_grab.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PassengerFindTripActivity extends Activity {

    private GIFImageView mGifImageView;
    private DatabaseReference mDatabaseRef;
    private TextView mFindTripLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_find_trip);

        mGifImageView = findViewById(R.id.find_trip_gif_image_view);
        mGifImageView.setGifImageResource(R.drawable.ontheroad);

        mFindTripLabel = findViewById(R.id.find_trip_label);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("TON HIEU", "On child changed");
                Log.i("HIEU3", dataSnapshot.toString());

                String key = dataSnapshot.getKey();
                Log.i("HIEU3", "Trip id = " + key);
                Location origin = dataSnapshot.child("origin").getValue(Location.class);

//                if (trip.getDriverID().length() > 0) {
//                    mFindTripLabel.setText("Đã tìm thấy tài xế!");
//
//                    TripBusiness.getInstance().updateTripDriver(trip.getDriverID());
//                    PassengerBusiness.getInstance().setPassengerState("IN_TRIP");
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("trips");
        mDatabaseRef.addChildEventListener(childEventListener);
    }
}
