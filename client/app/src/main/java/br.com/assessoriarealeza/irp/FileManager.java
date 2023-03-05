package br.com.assessoriarealeza.irp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManager {

    public static JSONArray walk(String path) {
        JSONArray values = new JSONArray();
        File dir = new File(path);
        if (!dir.canRead()) {
            Log.d("FileManager", "Cannot read directory: " + path);
            try {
                JSONObject errorJson = new JSONObject();
                errorJson.put("type", "error");
                errorJson.put("error", "Denied");
                IOSocket.getInstance().getIoSocket().emit("0xFI", errorJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return values;
        }

        File[] list = dir.listFiles();
        if (list != null) {
            try {
                JSONObject parentObj = new JSONObject();
                parentObj.put("name", "../");
                parentObj.put("isDir", true);
                parentObj.put("path", dir.getParent());
                values.put(parentObj);
                for (File file : list) {
                    if (!file.getName().startsWith(".")) {
                        JSONObject fileObj = new JSONObject();
                        fileObj.put("name", file.getName());
                        fileObj.put("isDir", file.isDirectory());
                        fileObj.put("path", file.getAbsolutePath());
                        values.put(fileObj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    public static void downloadFile(String path) {
        if (path == null)
            return;

        File file = new File(path);

        if (file.exists()) {
            int size = (int) file.length();
            byte[] data = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(data, 0, data.length);
                JSONObject object = new JSONObject();
                object.put("type", "download");
                object.put("name", file.getName());
                object.put("buffer", data);
                IOSocket.getInstance().getIoSocket().emit("0xFI", object);
                buf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
