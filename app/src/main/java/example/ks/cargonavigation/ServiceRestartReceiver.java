package example.ks.cargonavigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceRestartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ServiceRestartReceiver", "Restarting Service");
        Intent i = new Intent(context, GPSService.class);
        context.startService(i);
    }
}
