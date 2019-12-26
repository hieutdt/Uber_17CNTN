package com.example.cntn_grab.Screens;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.cntn_grab.Helpers.DirectionsJSONParser;
import com.example.cntn_grab.R;
import com.example.cntn_grab.Services.GetDirectionService;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{
    private final float zoom = 15;
    private final int LOCATION_REQUEST_CODE = 1119;

    private GoogleMap mMap;
    private Polyline mPolyline;
    private ReceiveJSON receiveJSON;
    private int distance = 0;
    private int duration = 0;

    public MapFragment() {
        // Required empty public constructor
        receiveJSON = new ReceiveJSON();
    }

    private class ReceiveJSON extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("DRAW", "receive ne");
            String json = intent.getStringExtra("JSON");

            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(json);
        }
    }

    private void registerServiceListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GetDirectionService.GET_DIRECTION_DONE);
        getContext().registerReceiver(this.receiveJSON, filter);
    }

    private void unRegisterServiceListener() {
        getContext().unregisterReceiver(this.receiveJSON);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerServiceListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unRegisterServiceListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);

        return rootView;
    }

    public void moveCamera(double lat, double lon) {
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat ,lon), this.zoom));
    }

    protected void requestPermission(String permissionType,
                                     int requestCode) {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permissionType}, requestCode
        );
    }

    private boolean hasPermission(String permissionType) {
        return ContextCompat.checkSelfPermission(getContext(), permissionType) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;

        mMap.clear(); //clear old markers

        this.showMyLocationButton();
        this.moveCamera(10.8231, 106.6297);
//        this.moveCamera(34.1424369, -117.922066);

        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Unable to show location - permission required", Toast.LENGTH_LONG).show();
                } else {
                    SupportMapFragment mapFragment =
                            (SupportMapFragment) getChildFragmentManager()
                                    .findFragmentById(R.id.frg);
                    mapFragment.getMapAsync(this);
                }
        }
    }

    private void showMyLocationButton() {
        mMap.setMyLocationEnabled(true);
        View locationButton = ((View) this.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        if (locationButton != null) {
//            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//            // position on right bottom
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            rlp.setMargins(0, 0, 30, 370);
//        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                // get distance
                MapFragment.this.distance = parser.getDistance(jObject);
                // get duration
                MapFragment.this.duration = parser.getDuration(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i = 0; i < result.size(); ++i){
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                if (path != null)
                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);
                Toast.makeText(getApplicationContext(),"Distance: " + MapFragment.this.distance + "m", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
            }
        }
    }
}