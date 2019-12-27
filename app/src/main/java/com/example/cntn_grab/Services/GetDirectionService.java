package com.example.cntn_grab.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.cntn_grab.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDirectionService extends Service {
    public static final String GET_DIRECTION_DONE = "GET_DIRECTION_DONE";

    private final String baseURL = "https://maps.googleapis.com/maps/api/directions/";

    public GetDirectionService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LatLng mOrigin = new LatLng(intent.getDoubleExtra("oLat", 0),
                intent.getDoubleExtra("oLng", 0));
        LatLng mDestination = new LatLng(intent.getDoubleExtra("dLat", 0),
                intent.getDoubleExtra("dLng", 0));

        getJSONdirection(mOrigin, mDestination);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        return this.baseURL + output + "?" + parameters + "&key=" + getString(R.string.map_direction_key);
    }

    private void getJSONdirection(LatLng mOrigin, LatLng mDestination){
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination, "driving");

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private void broadcast(String result) {
        Intent done = new Intent();
        done.setAction(GetDirectionService.GET_DIRECTION_DONE);
        done.putExtra("JSON", result);

        sendBroadcast(done);
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while((line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();
        }catch(Exception e){
            Log.d("Download exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }

            return data;
        }

        // Executes in UI thread, after the execution of doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            broadcast(result);
        }
    }
}
