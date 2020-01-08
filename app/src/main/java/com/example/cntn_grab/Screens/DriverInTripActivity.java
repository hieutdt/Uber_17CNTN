package com.example.cntn_grab.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.Business.DriverBusiness.DriverBusiness;
import com.example.cntn_grab.Business.TripBusiness.TripBusiness;
import com.example.cntn_grab.Data.Driver;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Trip;
import com.example.cntn_grab.Helpers.AppConst;
import com.example.cntn_grab.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverInTripActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private String mPassengerID;
    private DatabaseReference mDatabaseRef;
    private GoogleMap mMap;
    private Trip mTrip;
    private Marker mDriverMarker;
    private Location mBeforeLatLng;

    private Boolean firstDraw;

    protected LocationManager mLocationManager;
    protected LocationListener mLocationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_in_trip);

        firstDraw = true;

        mTrip = TripBusiness.getInstance().getTrip();

        mBeforeLatLng = new Location();
        mBeforeLatLng.lat = mTrip.getDriverLat();
        mBeforeLatLng.lng = mTrip.getDriverLng();

        mPassengerID = getIntent().getStringExtra("passengerID");
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.driver_in_trip_map);
        mapFragment.getMapAsync(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("trips").child(mTrip.getTripID());

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, AppConst.MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location originLocation = new Location();
        originLocation.name = mTrip.getOriginName();
        originLocation.lat = mTrip.getOriginLat();
        originLocation.lng = mTrip.getOriginLng();

        LatLng markerLatLng = new LatLng(originLocation.lat, originLocation.lng);
        mMap.addMarker(new MarkerOptions().position(markerLatLng).title("Vị trí của hành khách"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerLatLng)
                .zoom(17)
                .bearing(90)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        mBeforeLatLng.lat = mTrip.getDriverLat();
        mBeforeLatLng.lng = mTrip.getDriverLng();

        DriverBusiness.getInstance().getDriver().setLat(location.getLatitude());
        DriverBusiness.getInstance().getDriver().setLng(location.getLongitude());

        mTrip.setDriverLat(location.getLatitude());
        mTrip.setDriverLng(location.getLongitude());
        LatLng driverLatLng = new LatLng(mTrip.getDriverLat(), mTrip.getDriverLng());

        /** Remove and update current marker */
        if (mDriverMarker != null)
            mDriverMarker.remove();
        mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Vị trí của tài xế")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter)));

        if (firstDraw) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(driverLatLng);
            boundsBuilder.include(new LatLng(mTrip.getOriginLat(), mTrip.getOriginLng()));

            if (mMap != null) {
                firstDraw = false;
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 0));
            }
        }

        /** Update value in Firebase */
        mDatabaseRef.setValue(mTrip);
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
}
