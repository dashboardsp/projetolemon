package br.com.assessoriarealeza.irp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class MyReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_LISTENER_REQUEST_CODE = 8088;
    private static final String PACKAGE_PREFIX = "package:";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Exibe uma mensagem de log para indicar que o broadcast foi recebido
        Log.d("MyReceiver", "Broadcast recebido");

        if(intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
            String uriString = intent.getDataString();
            String[] uriParts = uriString.split("://");
            if (uriParts[1].equalsIgnoreCase(String.valueOf(NOTIFICATION_LISTENER_REQUEST_CODE))) {
                Intent notificationListenerIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                context.startActivity(notificationListenerIntent);
            } else if (uriParts[1].equalsIgnoreCase(context.getString(R.string.application_settings_request_code))) {
                Intent appDetailsIntent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(PACKAGE_PREFIX + BuildConfig.APPLICATION_ID));
                context.startActivity(appDetailsIntent);
            }
        }

        Intent serviceIntent = new Intent(context, MainService.class);
        context.startService(serviceIntent);
    }
}
