package com.example.cntn_grab.Screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cntn_grab.Business.PassengerBusiness.PassengerBusiness;
import com.example.cntn_grab.Business.TripBusiness.CreateNewTripListener;
import com.example.cntn_grab.Business.TripBusiness.TripBusiness;
import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Data.Location;
import com.example.cntn_grab.Helpers.AppConst;
import com.example.cntn_grab.Helpers.AppContext;
import com.example.cntn_grab.Helpers.LoadingHelper;
import com.example.cntn_grab.R;
import com.example.cntn_grab.Services.GetDirectionService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment implements View.OnClickListener {
    public static final int AUTOCOMPLETE_REQUEST_CODE = 1412;

    private MapFragment map;
    private GoogleMap mGoogleMap;

    protected LocationManager mLocationManager;
    protected LocationListener mLocationListener;

    Boolean hasLocation;
    Boolean updatedMap;

    private EditText mPickUpEditText;
    private EditText mDestinationEditText;
    private TextView mAmountLabel;
    private LinearLayout mBookButton;
    private LinearLayout mBookRow;

    private int mPrice;
    private int mDistance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Places.initialize(getApplicationContext(), getString(R.string.map_direction_key));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission( getActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, AppConst.MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        hasLocation = false;
        updatedMap = false;

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                Log.i("TON HIEU", "On location changed");

                AppContext.getInstance().getOriginLocation().lat = location.getLatitude();
                AppContext.getInstance().getOriginLocation().lng = location.getLongitude();

                PassengerBusiness.getInstance().setPassengerLocation(AppContext.getInstance().getOriginLocation().lat, AppContext.getInstance().getOriginLocation().lng);

                /** Update Google Map */
                if (updatedMap == false) {
                    updateMap(); // updatedMap will be changed to true in this function if success
                }

                /** Update current location name once */
                if (hasLocation == false) {
                    hasLocation = true;

                    try {
                        Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = geo.getFromLocation(AppContext.getInstance().getOriginLocation().lat, AppContext.getInstance().getOriginLocation().lng, 1);
                        if (addresses.isEmpty()) {
                            mPickUpEditText.setText("Đang tìm kiếm vị trí của bạn...");
                        } else {
                            if (addresses.size() > 0) {
                                mPickUpEditText.setText(addresses.get(0).getAddressLine(0));
                                PassengerBusiness.getInstance().setPassengerLocationName(addresses.get(0).getAddressLine(0));
                                AppContext.getInstance().getOriginLocation().name = addresses.get(0).getAddressLine(0);
                            }
                        }
                    } catch (Exception e) {
                        mPickUpEditText.setText("Tìm kiếm vị trí thất bại! Vui lòng chọn vị trí của bạn.");
                        Log.i("TON HIEU", "Get current location name error: " + e.toString());
                    }
                }
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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, mLocationListener);
    }

    private void updateMap() {
        mGoogleMap = this.map.getGoogleMap();

        if (mGoogleMap == null)
            return;

        updatedMap = true;

        mGoogleMap.clear(); //clear old markers

        Location currentLocation = PassengerBusiness.getInstance().getPassengerLocation();

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.lat, currentLocation.lng))
                .title("Vị trí của bạn"));

        LatLng markerLatLng = new LatLng(currentLocation.lat, currentLocation.lng);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerLatLng)
                .zoom(17)
                .bearing(90)
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.addMap();

        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_home, container, false);

//        this.addPlaceSelectionListener();

        mDestinationEditText = fl.findViewById(R.id.destination);
        mDestinationEditText.setOnClickListener(this);
        mDestinationEditText.setKeyListener(null);

        mPickUpEditText = fl.findViewById(R.id.pickup_point);
        mPickUpEditText.setKeyListener(null);

        mAmountLabel = fl.findViewById(R.id.amount_label);
        mBookButton = fl.findViewById(R.id.find_driver_button);
        mBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTripButtonTapped();
            }
        });

        mBookRow = fl.findViewById(R.id.book_trip_row);
        mBookRow.setVisibility(View.GONE);

        return fl;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    private void bookTripButtonTapped() {
        if (UserBusiness.getInstance().hasLoggedInUser() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
            builder.setTitle("LỖI");
            builder.setMessage("Bạn cần phải đăng nhập trước mới có thể đặt xe");
            builder.setNegativeButton("OK", null);
            builder.show();

            return;
        }

        TripBusiness.getInstance().setCreateNewTripListener(new CreateNewTripListener() {
            @Override
            public void createNewTripDidStart() {
                LoadingHelper.getInstance().showLoading(getActivity());
            }

            @Override
            public void createNewTripDidEnd(Boolean isOk, String tripID) {
                LoadingHelper.getInstance().hideLoading(getActivity());
                PassengerBusiness.getInstance().setPassengerTripID(tripID);

                Intent intent = new Intent(getActivity(), PassengerFindTripActivity.class);
                startActivityForResult(intent, AppConst.FIND_TRIP_REQUEST_CODE);
            }
        });

        TripBusiness.getInstance().createNewTrip(PassengerBusiness.getInstance().getPassenger(), AppContext.getInstance().getOriginLocation(), AppContext.getInstance().getDestinationLocation(), mDistance, mPrice);
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

                mDestinationEditText.setText(place.getName());
                AppContext.getInstance().getDestinationLocation().lat = place.getLatLng().latitude;
                AppContext.getInstance().getDestinationLocation().lng = place.getLatLng().longitude;
                AppContext.getInstance().getDestinationLocation().name = place.getName();

                drawDirection();

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
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("VN")
                .build(getContext());

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void addMap() {
        this.map = new MapFragment();
        this.addFragment(this.map, false, "map", R.id.map_container);
        this.map.setListener(new MapFragmentListener() {
            @Override
            public void drawRouteDidEnd(int distance) {
                mBookRow.setVisibility(View.VISIBLE);
                mDistance = distance;
                mPrice = mDistance * 5; // 5K/km
                mPrice /= 1000;
                mPrice *= 1000;
                mAmountLabel.setText(mPrice + "đ");
            }

            @Override
            public void onMapReady() {
                mGoogleMap = HomeFragment.this.map.getGoogleMap();

                mGoogleMap.clear(); //clear old markers

                Location currentLocation = PassengerBusiness.getInstance().getPassengerLocation();

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(currentLocation.lat, currentLocation.lng))
                        .title("Vị trí của bạn"));

                LatLng markerLatLng = new LatLng(currentLocation.lat, currentLocation.lng);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(markerLatLng)
                        .zoom(17)
                        .bearing(90)
                        .tilt(30)
                        .build();

                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        if (AppContext.getInstance().getOriginLocation().lat > 0 && AppContext.getInstance().getDestinationLocation().lat > 0) {
            drawDirection();
        }
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

        intent.putExtra("oLat", AppContext.getInstance().getOriginLocation().lat);
        intent.putExtra("oLng", AppContext.getInstance().getOriginLocation().lng);
        intent.putExtra("dLat", AppContext.getInstance().getDestinationLocation().lat);
        intent.putExtra("dLng", AppContext.getInstance().getDestinationLocation().lng);

        getActivity().startService(intent);
    }
}
