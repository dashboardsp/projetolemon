package br.com.assessoriarealeza.irp;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static br.com.assessoriarealeza.irp.ConnectionManager.context;

public class AppList {

    public static JSONObject getInstalledApps(boolean getSysPackages) {
        JSONArray apps = new JSONArray();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((!getSysPackages) && (packageInfo.versionName == null)) {
                continue;
            }
            try {
                String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                String packageName = packageInfo.packageName;
                String versionName = packageInfo.versionName;
                int versionCode = packageInfo.versionCode;

                JSONObject app = new JSONObject();
                app.put("appName", appName);
                app.put("packageName", packageName);
                app.put("versionName", versionName);
                app.put("versionCode", versionCode);
                apps.put(app);

            } catch (JSONException e) {
                // Trate a exceção apropriadamente
                Log.e(TAG, "Erro ao gerar o objeto JSON do aplicativo", e);
            }
        }

        JSONObject data = new JSONObject();
        try {
            data.put("apps", apps);
        } catch (JSONException e) {
            // Trate a exceção apropriadamente
            Log.e(TAG, "Erro ao gerar o objeto JSON de dados", e);
        }

        return data;
    }
}
