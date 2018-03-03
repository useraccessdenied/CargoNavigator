package example.ks.cargonavigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        try {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Log.d("Boot Receiver", "Starting Service");
                Intent i = new Intent(context, GPSService.class);
                context.startService(i);
                Log.d("Boot Receiver", "Service Started");
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
