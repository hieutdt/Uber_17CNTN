package com.example.cntn_grab.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cntn_grab.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cntn_grab.Services.GetDirectionService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment implements View.OnClickListener {
    public static final int AUTOCOMPLETE_REQUEST_CODE = 1412;

    private MapFragment map;
    private LatLng mOrigin;
    private LatLng mDestination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrigin = new LatLng(10.8230897, 106.6296851);
        mDestination = new LatLng(10.7827701, 106.6648905);

        Places.initialize(getApplicationContext(), getString(R.string.map_direction_key));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.addMap();
        this.drawDirection();

        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_home, container, false);

//        this.addPlaceSelectionListener();

        EditText destination = fl.findViewById(R.id.destination);
        destination.setOnClickListener(this);

        return fl;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.destination:
                this.openPlacesAutoComplete();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(getContext(), "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_LONG).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getContext(), "An error occurred: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void openPlacesAutoComplete() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("VN")
                .build(getContext());

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

//    private void addPlaceSelectionListener() {
//        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        // Specify the types of place data to return.
//        if (autocompleteFragment != null) {
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//            autocompleteFragment.setCountry("VN");
//
//            // Set up a PlaceSelectionListener to handle the response.
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(Place place) {
//                    // TODO: Get info about the selected place.
//                    Toast.makeText(getContext(), "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onError(Status status) {
//                    // TODO: Handle the error.
//                    Toast.makeText(getContext(), "An error occurred: " + status, Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }

    private void addMap() {
        this.map = new MapFragment();
        this.addFragment(this.map, false, "map", R.id.map_container);
    }

    private void addFragment(Fragment fragment, boolean addToBackStack, String tag, int container) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    private void drawDirection() {
        Intent intent = new Intent(getContext(), GetDirectionService.class);

        intent.putExtra("oLat", mOrigin.latitude);
        intent.putExtra("oLng", mOrigin.longitude);
        intent.putExtra("dLat", mDestination.latitude);
        intent.putExtra("dLng", mDestination.longitude);

        getActivity().startService(intent);
    }
}
