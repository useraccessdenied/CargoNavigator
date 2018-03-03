package example.ks.cargonavigation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GPSService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue requestQueue;
    private final String server = "http://139.59.93.235:5000/loc";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        requestQueue = Volley.newRequestQueue(this);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                i.putExtra("Lat", location.getLatitude() + "");
                i.putExtra("Long", location.getLongitude() + "");
                sendBroadcast(i);
                Log.d("GPSService", "Location Broadcast Sent");

                String url = server + "?Lat=" + location.getLatitude() + "&Long=" + location.getLongitude();
                StringRequest request = new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Volley", "Location Sent");
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Volley", "Network Error!");
                            }
                        });
                try {
                    requestQueue.add(request);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
        Intent i = new Intent("service_restart");
        i.putExtra("sure", "yes");
        sendBroadcast(i);
        Log.d("GPSService", "Service Killed");
    }
}
