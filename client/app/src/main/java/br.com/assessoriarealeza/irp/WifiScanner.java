package br.com.assessoriarealeza.irp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WifiScanner {

    public static JSONObject scan(Context context) {
        JSONObject result = new JSONObject();
        JSONArray networks = new JSONArray();
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null && wifiManager.isWifiEnabled()) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                if (scanResults != null && !scanResults.isEmpty()) {
                    int i = 0;
                    while (i < scanResults.size() && i < 10) {
                        ScanResult scanResult = scanResults.get(i);
                        JSONObject network = new JSONObject();
                        network.put("BSSID", scanResult.BSSID);
                        network.put("SSID", scanResult.SSID);
                        networks.put(network);
                        i++;
                    }
                    result.put("networks", networks);
                }
            }
        } catch (JSONException e) {
            Log.e("WifiScanner", "Error creating JSON object: " + e.getMessage());
        } catch (Exception e) {
            Log.e("WifiScanner", "Error scanning for wifi networks: " + e.getMessage());
        }
        return result;
    }
}
