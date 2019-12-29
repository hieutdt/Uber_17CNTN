package com.example.cntn_grab.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cntn_grab.Business.DriverBusiness.DriverBusiness;
import com.example.cntn_grab.Data.Driver;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Trip;
import com.example.cntn_grab.Helpers.AppConst;
import com.example.cntn_grab.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DriverStartFragment extends Fragment {
    LinearLayout suportLayout;
    LinearLayout findTripButton;
    ArrayList<Trip> mTrips;
    Trip nearestPickUpTrip;

    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_driver_start, container, false);

        suportLayout = view.findViewById(R.id.driver_support_area);
        findTripButton = view.findViewById(R.id.find_trip_button);

        findTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suportLayout.removeAllViews();
                View child = getLayoutInflater().inflate(R.layout.driver_support_layout, null);
                suportLayout.addView(child);

                findTripForDriver(DriverBusiness.getInstance().getDriver());
            }
        });

        mTrips = new ArrayList<>();
        nearestPickUpTrip = new Trip();

        return view;
    }

    public void findTripForDriver(Driver driver) {
        final Location currentLocation = driver.getLocation();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("trips");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<Trip> trips = new ArrayList<>();
                double minDistance = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Trip trip = postSnapshot.getValue(Trip.class);
                    if (trip != null) {
                        trips.add(trip);

                        if (!trip.getDriverID().equals(""))
                            continue;

                        double distance = getDistance(currentLocation.lat, currentLocation.lng, trip.getOriginLat(), trip.getOriginLng());
                        if (minDistance == 0) {
                            minDistance = distance;
                            nearestPickUpTrip = trip;
                        }
                        else if (distance < minDistance) {
                            minDistance = distance;
                            nearestPickUpTrip = trip;
                        }
                    }
                }

                mTrips = trips;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setMessage("ĐÃ TÌM THẤY CHUYẾN")
                        .setMessage("Lộ trình:\n" + nearestPickUpTrip.getOriginName() + " -> " + nearestPickUpTrip.getDestinationName() + "\nTổng quãng đường: " + nearestPickUpTrip.getDistance()/1000 + " km\nGiá cuốc: " + nearestPickUpTrip.getAmount()/1000 + "K")
                        .setNegativeButton("TỪ CHỐI", null)
                        .setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), DriverInTripActivity.class);
                                intent.putExtra("passengerId", nearestPickUpTrip.getPassengerID());
                                startActivityForResult(intent, AppConst.DRIVER_IN_TRIP_REQUEST_CODE);
                            }
                        });
                builder.show();
                return;
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
    }

    private double getDistance(double aLat, double aLng, double bLat, double bLng) {
        double dentaLat = Math.abs(bLat - aLat);
        double dentaLng = Math.abs(bLng - aLng);
        return Math.sqrt(dentaLat*dentaLat + dentaLng*dentaLng);
    }
}
