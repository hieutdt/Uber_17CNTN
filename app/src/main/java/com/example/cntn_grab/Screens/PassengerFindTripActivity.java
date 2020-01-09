package com.example.cntn_grab.Screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cntn_grab.Business.PassengerBusiness.PassengerBusiness;
import com.example.cntn_grab.Business.TripBusiness.TripBusiness;
import com.example.cntn_grab.Data.Passenger;
import com.example.cntn_grab.Data.Trip;
import com.example.cntn_grab.Helpers.AppContext;
import com.example.cntn_grab.Helpers.GIFImageView;
import com.example.cntn_grab.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PassengerFindTripActivity extends Activity {

    private GIFImageView mGifImageView;
    private DatabaseReference mDatabaseRef;
    private TextView mFindTripLabel;
    private Button mCancelButton;
    private EditText mPickUpText;
    private EditText mDestinationText;
    private TextView mAmoundLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_find_trip);

        mGifImageView = findViewById(R.id.find_trip_gif_image_view);
        mGifImageView.setGifImageResource(R.drawable.ontheroad);

        mFindTripLabel = findViewById(R.id.find_trip_label);

        mPickUpText = findViewById(R.id.passenger_find_trip_pick_up);
        mDestinationText = findViewById(R.id.passenger_find_trip_destination);
        mAmoundLabel = findViewById(R.id.passenger_find_trip_amount);

        try {
            mPickUpText.setText(AppContext.getInstance().getOriginLocation().name);
            mDestinationText.setText(AppContext.getInstance().getDestinationLocation().name);
            mAmoundLabel.setText(TripBusiness.getInstance().getTrip().getAmount() + "đ");
        } catch (NullPointerException e) {
            Log.i("TON HIEU", e.toString());
        }

        mCancelButton = findViewById(R.id.find_trip_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("trips");
                mDatabaseRef.child(PassengerBusiness.getInstance().getPassenger().getTripId()).removeValue();

                finish();
            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);

                if (trip != null && trip.getDriverID().length() > 0) {
                    mFindTripLabel.setText("Đã tìm thấy tài xế!");
                    TripBusiness.getInstance().updateTripDriver(trip.getDriverID());
                    PassengerBusiness.getInstance().setPassengerState("IN_TRIP");

                    AlertDialog.Builder builder = new AlertDialog.Builder(PassengerFindTripActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("ĐÃ TÌM THẤY TÀI XẾ");
                    builder.setMessage("Tài xế đã đồng ý rồi! Bạn đợi một lát, tài xế sẽ đến đón bạn ngay thôi!");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PassengerFindTripActivity.this, PassengerInTripActivity.class);
                            startActivityForResult(intent, 3054);
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("trips").child(PassengerBusiness.getInstance().getPassenger().getTripId());
        mDatabaseRef.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3054) {
            finish();
        }
    }
}
