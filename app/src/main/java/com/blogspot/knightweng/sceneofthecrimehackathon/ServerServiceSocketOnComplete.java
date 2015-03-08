package com.blogspot.knightweng.sceneofthecrimehackathon;

import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;

public interface ServerServiceSocketOnComplete {
        public void onComplete(Socket socket, String message);
}
