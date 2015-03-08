package com.blogspot.knightweng.sceneofthecrimehackathon;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerServiceSocket extends AsyncTask<Void, Void, Void> {

    public static final String TAG = ServerServiceSocket.class.getName();

    private static final int SocketServerPORT = 5566;

    private ServerSocket serverSocket;

    private String message;

    private Socket socket;

    public ServerServiceSocketOnComplete completion;

    ServerServiceSocket() {

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Server Listening on port " + serverSocket.getLocalPort());

            //while (true) {
            socket = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            message = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        completion.onComplete(socket, message);
        super.onPostExecute(result);
    }
}