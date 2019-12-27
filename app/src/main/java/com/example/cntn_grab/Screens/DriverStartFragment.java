package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.cntn_grab.R;

public class DriverStartFragment extends Fragment {
    LinearLayout suportLayout;
    LinearLayout findTripButton;

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
            }
        });

        return view;
    }
}
