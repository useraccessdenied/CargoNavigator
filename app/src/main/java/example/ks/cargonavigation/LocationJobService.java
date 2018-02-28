package example.ks.cargonavigation;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationJobService extends JobService implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private final String server = "http://139.59.93.235:5000/loc";
    private RequestQueue reQueue;

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d("Job Dispatcher", "Job Started");
        reQueue = Volley.newRequestQueue(this);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("Job Dispatcher", "Job Terminated");
        return false;
    }

    @Override
    public void onLocationChanged(Location location){
        String url = server + "?Lat=" + location.getLatitude() + "&Long=" + location.getLongitude() + "&Status=Normal";
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
            reQueue.add(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle){

    }
    @Override
    public void onProviderEnabled(String s){

    }
    @Override
    public void onProviderDisabled(String s){

    }
}
