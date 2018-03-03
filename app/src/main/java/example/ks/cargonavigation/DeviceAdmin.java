package example.ks.cargonavigation;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Kapil on 29-02-2018.
 */

public class DeviceAdmin extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d("DeviceAdminReceiver", "Device Admin Activated");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.d("DeviceAdminReceiver", "Device Admin Deactivated");
        //Implement Log Sending Logic here
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return super.onDisableRequested(context, intent);
        //Implement Some Password activity here but it is not secure and can be bypassed.
        //Starting from sdk 27, android disallows such aggressive DeviceAdmin behaviour
        //but still it can be overcome by some non-typical mechanisms
        //(such as killing Settings activity if password is wrong).
    }
}
