package com.example.cntn_grab.Screens;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;

public class DriverInTripActivity extends Activity {
    private String mPassengerID;
    private DatabaseReference mDatabaseRef;
    private MapFragment mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_in_trip);

        mPassengerID = getIntent().getStringExtra("passengerID");


    }

//    private void addMap() {
//        mMap = new MapFragment();
//        this.addFragment(this.map, false, "map", R.id.map_container);
//        this.map.setListener(new MapFragmentListener() {
//        }
//    }

//    private void addFragment(Fragment fragment, boolean addToBackStack, String tag, int container) {
//        FragmentManager manager = getChildFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        if (addToBackStack) {
//            ft.addToBackStack(tag);
//        }
//        ft.replace(container, fragment, tag);
//        ft.commitAllowingStateLoss();
//    }
}
