package example.ks.cargonavigation;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    TextView txtLat, txtLong;
    Button btnStart, btnStop, btnHide, btnDeviceAdmin;
    private BroadcastReceiver broadcastReceiver;
    public static final int PERMISSION_REQUEST_CODE = 69;
    public static final int OUT_CALL_PERMISSION_REQUEST_CODE = 6;
    private static final ComponentName launcher = new ComponentName("example.ks.cargonavigation", "example.ks.cargonavigation.MainActivity");
    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        btnStart = findViewById(R.id.btnStartService);
        btnStop = findViewById(R.id.btnStopService);
        btnHide = findViewById(R.id.btnHide);
        btnDeviceAdmin = findViewById(R.id.btnDeviceAdmin);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(this, DeviceAdmin.class);

        btnDeviceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(devicePolicyManager.isAdminActive(deviceAdmin)){
                    finish();
                    Toast.makeText(MainActivity.this, "Device Admin Active", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Please enable Device Admin and return", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
                    i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please enable Device Admin from Settings");
                    startActivity(i);
                }
            }
        });

        if(!checkPermissions()){
            enableButtons();
            enableHideButton();
        }
        Log.d("MainActivity", "Location Permissions Checked");
        if(isLauncherIconVisible()){
            btnHide.setText("Hide Icon");
        }else{
            btnHide.setText("Unhide Icon");
        }
        Log.d("MainActivity", "Launcher Visibility Checked");

    }

    private void enableHideButton() {
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager p = getPackageManager();
                if(isLauncherIconVisible()){
                    Log.d("MainActivity", "Hiding App Icon");
                    p.setComponentEnabledSetting(launcher, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    finish();
                }else{
                    p.setComponentEnabledSetting(launcher, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                }
            }
        });
    }

    private void enableButtons(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), GPSService.class));
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), GPSService.class));
            }
        });
    }

    private boolean isLauncherIconVisible() {
        int enabledSetting = getPackageManager().getComponentEnabledSetting(launcher);
        return enabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

    private boolean checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
                )
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.PROCESS_OUTGOING_CALLS},
                    PERMISSION_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED){
                enableButtons();
                enableHideButton();
            }else{
                checkPermissions();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    txtLat.setText("" + intent.getExtras().get("Lat"));
                    txtLong.setText("" + intent.getExtras().get("Long"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

}