package br.com.assessoriarealeza.irp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.regex.Pattern;

public class AppInstaller {
    private Context mContext;

    public AppInstaller(Context context) {
        mContext = context;
    }

    public void installApk(File file) {
        try {
            if (file.exists() && Pattern.matches(".*\\.apk", file.getName())) {
                Uri apkUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    apkUri = Uri.parse(file.getAbsolutePath());
                } else {
                    apkUri = Uri.fromFile(file);
                }
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
