package com.blogspot.knightweng.sceneofthecrimehackathon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class SceneOfTheCrimeHackathonService extends Service {
    public static final String TAG = SceneOfTheCrimeHackathonService.class.getName();

    private OutputStream outputStream;

    public enum MESSAGETYPE {
        QUERY,
        UPLOAD
    }

    private void socketHandlerStart() {
        ServerServiceSocket serverServiceSocket = new ServerServiceSocket();
        serverServiceSocket.completion = new ServerServiceSocketOnComplete() {
            @Override
            public void onComplete(Socket socket, String message) {
                Log.d("Service", "message = " + message);

                //scanStorageMovieAndNotification(null, null, null);
                MESSAGETYPE type = packetHandler(message);
                packetResponser(type, socket);
                //socketHandlerStart();
            }
        };
        serverServiceSocket.execute();
    }

    @Override
    public void onCreate() {
        socketHandlerStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void packetResponser(MESSAGETYPE type, Socket socket) {
        Log.d(TAG, "packetResponser");

        if(type == null || socket == null)
            return;

        switch (type) {
            case QUERY:
                try {
                    outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print("OK");
                    printStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    //message += "Something wrong! " + e.toString() + "\n";
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case UPLOAD:
                break;
            default:
                break;
        }

        socketHandlerStart();
    }

    MESSAGETYPE packetHandler(String message) {
        Log.d(TAG, "packetHandler");

        JSONObject object = null;
        try {
            object = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(object.has("Type")) {
            MESSAGETYPE type = null;
            try {
                type = MESSAGETYPE.valueOf(object.getString("Type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (type) {
                case QUERY:
                    if (object.has("Lat") && object.has("Lng") && object.has("Time")) {
                        try {
                            scanStorageMovieAndNotification(object.getString("Lat"), object.getString("Lng"), object.getString("Time"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPLOAD:
                    if (object.has("Address"))

                    break;
                default:
                    break;
            }

            return type;
        }

        return null;
    }

    void scanStorageMovieAndNotification(String Lat, String Lng, String Time){
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.drawable.ic_launcher, "Hello",
                    System.currentTimeMillis());

            //Intent notificationIntent = new Intent(this, SceneOfTheCrimeHackathonService.class);
            Intent intent = new Intent(getBaseContext(), MessageBoxActivity.class);
            intent.putExtra("com.blogspot.knightweng.sceneofthecrimehackathon", 5566);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.ledARGB = Color.MAGENTA;
            //PendingIntent contentIntent = PendingIntent.getActivity(this, 1, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setLatestEventInfo(this, "Message from R.O.C Government", "You may have recorded evidence video.", contentIntent);
            nm.notify(1, notification);
    }

}
