package com.example.cntn_grab.Screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cntn_grab.Business.DriverBusiness.DriverBusiness;
import com.example.cntn_grab.Business.TripBusiness.TripBusiness;
import com.example.cntn_grab.Data.Driver;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Trip;
import com.example.cntn_grab.Helpers.AppConst;
import com.example.cntn_grab.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Vector;

public class DriverStartFragment extends Fragment {

    LinearLayout suportLayout;
    LinearLayout findTripButton;
    ArrayList<Trip> mTrips;
    Trip nearestPickUpTrip;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    DatabaseReference mDatabaseRef;
    ValueEventListener valueEventListener;
    View loadingView;

    Boolean inTrip;

    private Vector<AlertDialog> dialogs = new Vector<AlertDialog>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission( getActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, AppConst.MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        inTrip = false;

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                DriverBusiness.getInstance().getDriver().setLat(location.getLatitude());
                DriverBusiness.getInstance().getDriver().setLng(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_driver_start, container, false);

        suportLayout = view.findViewById(R.id.driver_support_area);
        findTripButton = view.findViewById(R.id.find_trip_button);

        findTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findTripButton.setVisibility(View.GONE);
                loadingView = getLayoutInflater().inflate(R.layout.driver_support_layout, null);
                suportLayout.addView(loadingView);

                findTripForDriver(DriverBusiness.getInstance().getDriver());
            }
        });

        mTrips = new ArrayList<>();
        nearestPickUpTrip = new Trip();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        inTrip = false;
    }

    public void findTripForDriver(final Driver driver) {
        Log.i("TON HIEU", "Find trip for driver button tapped");

        final Location currentLocation = driver.getLocation();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Trip> trips = new ArrayList<>();
                double minDistance = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Trip trip = postSnapshot.getValue(Trip.class);

                    Log.i("TripData", trip.toString());

                    if (trip != null) {
                        trips.add(trip);

                        try {
                            if (!trip.getDriverID().equals(""))
                                continue;
                        } catch (NullPointerException e) {
                            continue;
                        }

                        double distance = getDistance(currentLocation.lat, currentLocation.lng, trip.getOriginLat(), trip.getOriginLng());
                        if (minDistance == 0) {
                            minDistance = distance;
                            nearestPickUpTrip = trip;
                        }  else if (distance < minDistance) {
                            minDistance = distance;
                            nearestPickUpTrip = trip;
                        }
                    }
                }

                mTrips = trips;

                if (mTrips.size() == 0)
                    return;

                try {
                    if (inTrip == false) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("ĐÃ TÌM THẤY CHUYẾN")
                                .setMessage("Lộ trình:\n" + nearestPickUpTrip.getOriginName() + " -> " + nearestPickUpTrip.getDestinationName() + "\nTổng quãng đường: " + nearestPickUpTrip.getDistance() / 1000 + " km\nGiá cuốc: " + nearestPickUpTrip.getAmount() / 1000 + "K")
                                .setNegativeButton("TỪ CHỐI", null)
                                .setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        inTrip = true;

                                        /** Update trip value in Firebase */
                                        nearestPickUpTrip.setDriverID(driver.getId());
                                        mDatabaseRef.child(nearestPickUpTrip.getTripID()).setValue(nearestPickUpTrip);

                                        /** Set that trip to TripBusiness */
                                        TripBusiness.getInstance().setTrip(nearestPickUpTrip);

                                        /** Open DriverInTripActivity */
                                        Intent intent = new Intent(getActivity(), DriverInTripActivity.class);
                                        startActivityForResult(intent, AppConst.DRIVER_IN_TRIP_REQUEST_CODE);
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialogs.add(dialog);

                        dialog.show();
                    }
                } catch (NullPointerException e) {
                    Log.i("TON HIEU", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("trips");
        mDatabaseRef.addValueEventListener(valueEventListener);
    }

    private double getDistance(double aLat, double aLng, double bLat, double bLng) {
        double dentaLat = Math.abs(bLat - aLat);
        double dentaLng = Math.abs(bLng - aLng);
        return Math.sqrt(dentaLat*dentaLat + dentaLng*dentaLng);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConst.DRIVER_IN_TRIP_REQUEST_CODE) {
            findTripButton.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);

            for (AlertDialog dialog : dialogs)
                if (dialog.isShowing())
                    dialog.dismiss();

            mDatabaseRef.removeEventListener(valueEventListener);
        }
    }
}
