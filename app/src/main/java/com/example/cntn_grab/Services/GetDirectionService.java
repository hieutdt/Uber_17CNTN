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
    public static final String sample = "{\"geocoded_waypoints\":[{\"geocoder_status\":\"OK\",\"place_id\":\"ChIJPdIEh2IpdTERjh6SUUpHqm4\",\"types\":[\"route\"]},{\"geocoder_status\":\"OK\",\"place_id\":\"ChIJ2-hqLNAudTERrcbpsaoENMg\",\"types\":[\"bank\",\"establishment\",\"finance\",\"point_of_interest\"]}],\"routes\":[{\"bounds\":{\"northeast\":{\"lat\":10.8232157,\"lng\":106.6665275},\"southwest\":{\"lat\":10.7827701,\"lng\":106.6294588}},\"copyrights\":\"Map data ©2019\",\"legs\":[{\"distance\":{\"text\":\"6.6 km\",\"value\":6588},\"duration\":{\"text\":\"20 mins\",\"value\":1170},\"end_address\":\"2 Trường Sơn Cư xá Bắc Hải Trường Sơn, Phường 15, Quận 10, Hồ Chí Minh, Vietnam\",\"end_location\":{\"lat\":10.7827701,\"lng\":106.6648905},\"start_address\":\"Trường Chinh, Phường 15, Tân Bình, Hồ Chí Minh, Vietnam\",\"start_location\":{\"lat\":10.8230897,\"lng\":106.6296851},\"steps\":[{\"distance\":{\"text\":\"18 m\",\"value\":18},\"duration\":{\"text\":\"1 min\",\"value\":3},\"end_location\":{\"lat\":10.8232157,\"lng\":106.6295864},\"html_instructions\":\"Head <b>northwest</b> on <b>Trường Chinh</b> toward <b>Phan Huy Ích</b>\",\"polyline\":{\"points\":\"i{`aAqbyiSGDQL\"},\"start_location\":{\"lat\":10.8230897,\"lng\":106.6296851},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"1.8 km\",\"value\":1789},\"duration\":{\"text\":\"4 mins\",\"value\":253},\"end_location\":{\"lat\":10.8079988,\"lng\":106.6344139},\"html_instructions\":\"Make a <b>U-turn</b> at Bác Sĩ Ở Nhà<div style=\\\"font-size:0.9em\\\">Pass by TrueMoney Vietnam - Nguyễn Thị Tâm Ngọc Mỹ (on the left)</div>\",\"maneuver\":\"uturn-left\",\"polyline\":{\"points\":\"c|`aA}ayiSPXRQl@]b@W|@_@pAa@vA]n@MVEzBc@NEt@S`@KpDs@hO}CfE}@rEaAtCm@dE{@nAWfMeCnBc@FAPE~@ShBa@vA[\"},\"start_location\":{\"lat\":10.8232157,\"lng\":106.6295864},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"0.7 km\",\"value\":672},\"duration\":{\"text\":\"2 mins\",\"value\":114},\"end_location\":{\"lat\":10.8032628,\"lng\":106.6381734},\"html_instructions\":\"Slight <b>left</b> at Diệp Lục Collagen- Diệp lục Lysine Quận Tân Bình onto <b>Cộng Hòa</b><div style=\\\"font-size:0.9em\\\">Pass by Công Ty TNHH TTC (on the right)</div>\",\"maneuver\":\"turn-slight-left\",\"polyline\":{\"points\":\"_}}`Aa`ziSr@]r@_@b@]LGLO@ANQf@e@zBoCVYb@a@`C{A|AaAPIdBeA^UnBkAp@e@\"},\"start_location\":{\"lat\":10.8079988,\"lng\":106.6344139},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"0.4 km\",\"value\":446},\"duration\":{\"text\":\"1 min\",\"value\":74},\"end_location\":{\"lat\":10.799917,\"lng\":106.640313},\"html_instructions\":\"Slight <b>right</b> at Công Ty Tnhh Văn Việt onto <b>Hẻm 508 Trường Chinh</b>/<wbr/><b>Tân Hải</b><div style=\\\"font-size:0.9em\\\">Continue to follow Tân Hải</div><div style=\\\"font-size:0.9em\\\">Pass by CÔNG TY TNHH MỘC STYLE (on the right)</div>\",\"maneuver\":\"turn-slight-right\",\"polyline\":{\"points\":\"k_}`AqwziSx@Wj@SNGzDaCnAy@rAy@n@e@|@}@\\\\WHEFABAF?ZB\"},\"start_location\":{\"lat\":10.8032628,\"lng\":106.6381734},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"1.6 km\",\"value\":1632},\"duration\":{\"text\":\"6 mins\",\"value\":351},\"end_location\":{\"lat\":10.7928926,\"lng\":106.6533187},\"html_instructions\":\"Turn <b>left</b> at CỬA HÀNG BẾP GAS PHƯƠNG QUYÊN onto <b>Trường Chinh</b><div style=\\\"font-size:0.9em\\\">Pass by Quán Phở Hương Việt (on the right in 850&nbsp;m)</div>\",\"maneuver\":\"turn-left\",\"polyline\":{\"points\":\"oj|`A}d{iSNFVq@N[Rc@^{@^{@n@{AbB}DN]HOXo@f@uAZs@HSJQRc@b@eAn@uATg@p@aBN]R]FOz@iBJ]N]f@qAHS^}@b@cAXq@BGLYdDkHPc@p@yAn@wATg@V_Al@qAZw@P_@r@}AHQDIPY\"},\"start_location\":{\"lat\":10.799917,\"lng\":106.640313},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"1.6 km\",\"value\":1642},\"duration\":{\"text\":\"5 mins\",\"value\":300},\"end_location\":{\"lat\":10.7858793,\"lng\":106.6665275},\"html_instructions\":\"At Ngã 4 Bảy Hiền, continue onto <b>Cách Mạng Tháng Tám</b><div style=\\\"font-size:0.9em\\\">Pass by Yeah1Shop-SnapBack (on the right in 1.4&nbsp;km)</div>\",\"polyline\":{\"points\":\"q~z`Agv}iSHSV{@LW|@wBh@iA|@uBHSx@iBxAiD^{@Xm@FQTg@BIv@mBXw@Te@d@cAp@_BDI\\\\y@Na@Pa@j@iAN[Vm@Vo@Vo@Vs@FMNWHQDKDK@EZo@^y@`@_AdAiCLGDMh@iAZs@NYTk@^w@d@eAj@qAj@qAPm@BG\"},\"start_location\":{\"lat\":10.7928926,\"lng\":106.6533187},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"0.4 km\",\"value\":389},\"duration\":{\"text\":\"1 min\",\"value\":75},\"end_location\":{\"lat\":10.7827701,\"lng\":106.6648905},\"html_instructions\":\"Turn <b>right</b> at ATSM Shop onto <b>Trường Sơn</b><div style=\\\"font-size:0.9em\\\">Pass by Quản Lý (on the right)</div><div style=\\\"font-size:0.9em\\\">Destination will be on the left</div>\",\"maneuver\":\"turn-right\",\"polyline\":{\"points\":\"wry`Ayh`jSXPn@XzAl@XLp@XlAd@p@XTH|CrAfAf@\"},\"start_location\":{\"lat\":10.7858793,\"lng\":106.6665275},\"travel_mode\":\"DRIVING\"}],\"traffic_speed_entry\":[],\"via_waypoint\":[]}],\"overview_polyline\":{\"points\":\"i{`aAqbyiSYRPXRQpAu@|@_@pAa@fCk@rCi@dAYrE_Az`@kItGsAvPiDbE}@vA[r@]r@_@b@]ZWPSf@e@zBoCz@{@vImFnCaBp@e@x@Wz@[jG{DbC_BzAuAPGJAZBNFf@mAbCwFvC{GbAiCTe@|BgF|AmDfAgCv@oBfBgEhEqJ`BqDTg@V_AhAiCdA}Bj@iAd@sAfBaEzE}KvAcDjBuEzBgF`@cAz@eB~AaEjB_EfBiELGDMdA}Bd@eA|CaHTu@hAj@tFzBfAb@dFzB\"},\"summary\":\"Trường Chinh\",\"warnings\":[],\"waypoint_order\":[]}],\"status\":\"OK\"}";
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
//            String data = "";
//
//            try{
//                // Fetching the data from web service
//                data = downloadUrl(url[0]);
//                Log.d("DownloadTask","DownloadTask : " + data);
//            }catch(Exception e){
//                Log.d("Background Task",e.toString());
//            }
//
//            return data;
            return GetDirectionService.sample;
        }

        // Executes in UI thread, after the execution of doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            broadcast(result);
        }
    }
}
