package example.ks.cargonavigation;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    String launch_app_code = "12345";
    private static final ComponentName launcher = new ComponentName("example.ks.cargonavigation", "example.ks.cargonavigation.MainActivity");

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(launch_app_code.equals(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER))){
            Log.d("CallReceiver", "Launch Activity Call Received");
            setResultData(null);
            if(!isLauncherIconVisible(context)){
                Log.d("CallReceiver", "Launcher Icon Invisible");
                PackageManager p = context.getPackageManager();
                ComponentName component = new ComponentName(context, example.ks.cargonavigation.MainActivity.class);
                p.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                Intent appIntent = new Intent(context, MainActivity.class);
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
            }
        }
    }

    private boolean isLauncherIconVisible(Context context) {
        int enabledSetting = context.getPackageManager().getComponentEnabledSetting(launcher);
        return enabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }
}
