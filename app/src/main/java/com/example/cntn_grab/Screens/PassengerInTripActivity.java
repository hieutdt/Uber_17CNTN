package com.example.cntn_grab.Screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cntn_grab.Business.TripBusiness.TripBusiness;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Data.Trip;
import com.example.cntn_grab.Data.User;
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

public class PassengerInTripActivity extends FragmentActivity implements OnMapReadyCallback, ValueEventListener {
    private DatabaseReference mTripRef;
    private DatabaseReference mDriverRef;
    private GoogleMap mMap;
    private Trip mTrip;
    private Marker mDriverMarker;

    private TextView mDriverNameLabel;
    private TextView mDriverPhoneLabel;
    private TextView mAmountLabel;
    private User mDriver;

    private Button mCallButton;
    private Button mCancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_in_trip);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.passenger_in_trip_map);
        mapFragment.getMapAsync(this);

        mTrip = TripBusiness.getInstance().getTrip();

        mTripRef = FirebaseDatabase.getInstance().getReference().child("trips").child(mTrip.getTripID());
        mDriverRef = FirebaseDatabase.getInstance().getReference().child("users").child(mTrip.getDriverID());

        mTripRef.addValueEventListener(this);

        mDriverNameLabel = findViewById(R.id.passenger_in_trip_driver_name);
        mDriverPhoneLabel = findViewById(R.id.passenger_in_trip_driver_phone);
        mAmountLabel = findViewById(R.id.passenger_in_trip_amount);
        mCallButton = findViewById(R.id.passenger_in_trip_call);
        mCancelButton = findViewById(R.id.passenger_in_trip_cancel);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User driver = dataSnapshot.getValue(User.class);
                mDriver = driver;

                if (driver != null) {
                    mDriverNameLabel.setText(driver.getName());
                    mDriverPhoneLabel.setText(driver.getPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDriverRef.addValueEventListener(valueEventListener);

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("tel:" + mDriver.getPhoneNumber());
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.i("TON HIEU", e.toString());
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerInTripActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("XÁC NHẬN HUỶ CHUYẾN");
                builder.setMessage("Bạn có chắc chắn muốn huỷ chuyến không?");
                builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTrip.setState("CANCEL_BY_PASSENGER");
                        mTripRef.setValue(mTrip);

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                });
                builder.setPositiveButton("Không", null);

                builder.show();
            }
        });
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
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mTrip = dataSnapshot.getValue(Trip.class);

        try {
            mAmountLabel.setText(mTrip.getAmount() + "đ");
        } catch (NullPointerException e) {
            Log.i("TON HIEU", e.toString());
        }

        if (mTrip.getState().equals("FINISH")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PassengerInTripActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("CHUYẾN ĐI ĐÃ KẾT THÚC");
            builder.setMessage("Cảm ơn bạn vì chuyến đi! Mong gặp lại bạn lần sau!");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            });

            builder.show();

        } else if (mTrip.getState().equals("ON_ROAD")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PassengerInTripActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("TÀI XẾ ĐÃ ĐẾN");
            builder.setMessage("Tài xế đã đến điểm đón, hãy di chuyển đến gặp tài xế và bắt đầu chuyến đi!");
            builder.setNegativeButton("OK",null);
            builder.show();

            mCallButton.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.GONE);
        }

        LatLng driverLatLng = new LatLng(mTrip.getDriverLat(), mTrip.getDriverLng());

        /** Remove and update current marker */
        if (mDriverMarker != null)
            mDriverMarker.remove();

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.scooter);
        Bitmap markerIcon = Bitmap.createScaledBitmap(largeIcon, 100, 100, true);

        mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Vị trí của tài xế")
                .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(driverLatLng);
        boundsBuilder.include(new LatLng(mTrip.getOriginLat(), mTrip.getOriginLng()));

        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 0));
        }
    };

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
