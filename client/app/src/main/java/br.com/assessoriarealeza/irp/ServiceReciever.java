package br.com.assessoriarealeza.irp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

class ServiceReceiver extends BroadcastReceiver {
    private static final String TAG = ServiceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Check if the RECEIVE_BOOT_COMPLETED permission is granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "RECEIVE_BOOT_COMPLETED permission not granted");
                    return;
                }
            }

            // Start the service
            Intent serviceIntent = new Intent(context, MainService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }
    }
}
